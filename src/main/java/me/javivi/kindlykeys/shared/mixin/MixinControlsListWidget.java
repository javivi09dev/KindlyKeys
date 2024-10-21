package me.javivi.kindlykeys.shared.mixin;

import java.util.ArrayList;
import me.javivi.kindlykeys.client.io.LockedKeysIO;
import me.javivi.kindlykeys.shared.objects.IServerSideChecker;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.controls.KeyBindsList;
import net.minecraft.client.player.LocalPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(KeyBindsList.class)
public class MixinControlsListWidget {

    @Inject(method = "<init>", at = @At("RETURN"), cancellable = true)
    public void onInit(CallbackInfo ci) {
        Minecraft client = Minecraft.getInstance();
        ArrayList<KeyMapping> allowedKeyBindings = new ArrayList<>();

        if (client.player != null) {
            LocalPlayer player = client.player;
            if (player instanceof IServerSideChecker) {
                IServerSideChecker checker = (IServerSideChecker) player;
                if (checker.keyChanger_Shared$isServerSidePresent()) {
                    return; // Si el servidor tiene soporte, no hacemos nada
                }
            }
        }

        KeyMapping[] keyBindings = client.options.keyMappings;
        for (KeyMapping keyBinding : keyBindings) {
            if (!LockedKeysIO.INSTANCE.isKeyDisabled(keyBinding.getName())) {
                allowedKeyBindings.add(keyBinding);
            }
        }

        // Reemplazar la lista original de mapeos de teclas con la lista filtrada
        // Aquí tendrías que ajustar el acceso a la lista interna, si es posible.
    }
}
