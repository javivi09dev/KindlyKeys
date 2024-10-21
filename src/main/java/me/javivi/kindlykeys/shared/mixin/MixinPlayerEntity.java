package me.javivi.kindlykeys.shared.mixin;

import me.javivi.kindlykeys.shared.objects.IServerSideChecker;
import me.javivi.kindlykeys.shared.objects.Reference;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Player.class)
public abstract class MixinPlayerEntity extends LivingEntity implements IServerSideChecker {
    protected MixinPlayerEntity(EntityType<? extends LivingEntity> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(
            method = "defineSynchedData",
            at = @At("HEAD")
    )
    private void defineSynchedData(CallbackInfo ci) {
        // Asegúrate de que f_19804_ se inicialice correctamente
        this.entityData.set(Reference.SERVERSIDE_PRESENT, false);
    }

    @Override
    public boolean keyChanger_Shared$isServerSidePresent() {
        return this.entityData.get(Reference.SERVERSIDE_PRESENT);
    }

    @Override
    public void keyChanger_Shared$setServerSidePresent(boolean serverSidePresent) {
        this.entityData.set(Reference.SERVERSIDE_PRESENT, serverSidePresent);
    }
}

