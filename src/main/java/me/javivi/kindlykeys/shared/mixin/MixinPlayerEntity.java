package me.javivi.kindlykeys.shared.mixin;

import me.javivi.kindlykeys.shared.objects.IServerSideChecker;
import me.javivi.kindlykeys.shared.objects.Reference;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.SynchedEntityData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Player.class)
public abstract class MixinPlayerEntity extends LivingEntity implements IServerSideChecker {
    private static final EntityDataAccessor<Boolean> SERVERSIDE_PRESENT = SynchedEntityData.defineId(Player.class, Reference.SERVERSIDE_PRESENT);

    protected MixinPlayerEntity(EntityType<? extends LivingEntity> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(method = "defineSynchedData", at = @At("HEAD"))
    private void defineSynchedData(CallbackInfo ci) {
        this.entityData.define(SERVERSIDE_PRESENT, false);
    }

    @Override
    public boolean keyChanger_Shared$isServerSidePresent() {
        return this.entityData.get(SERVERSIDE_PRESENT);
    }

    @Override
    public void keyChanger_Shared$setServerSidePresent(boolean serverSidePresent) {
        this.entityData.set(SERVERSIDE_PRESENT, serverSidePresent);
    }
}