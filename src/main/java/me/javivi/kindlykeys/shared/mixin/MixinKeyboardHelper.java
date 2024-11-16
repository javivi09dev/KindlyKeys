// src/main/java/me/javivi/kindlykeys/shared/mixin/MixinKeyboardHelper.java
package me.javivi.kindlykeys.shared.mixin;

import me.javivi.kindlykeys.client.io.LockedKeysIO;
import net.minecraft.client.KeyboardHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({KeyboardHandler.class})
public class MixinKeyboardHelper {
    public MixinKeyboardHelper() {
    }

    @Inject(
            method = {"keyPress"},
            at = {@At("HEAD")},
            cancellable = true
    )
    private void onKeyPress(long window, int key, int scancode, int i, int j, CallbackInfo ci) {
        LockedKeysIO.cancelKeyIfRequired(key, scancode, ci);
    }

    @Inject(
            method = {"handleDebugKeys"},
            at = {@At("HEAD")},
            cancellable = true
    )
    private void onDebugPress(int p_90933_, CallbackInfoReturnable<Boolean> cir) {
        if (LockedKeysIO.INSTANCE.isKeyDisabled("extra.f3")) {
            cir.setReturnValue(false);
        }
    }
}