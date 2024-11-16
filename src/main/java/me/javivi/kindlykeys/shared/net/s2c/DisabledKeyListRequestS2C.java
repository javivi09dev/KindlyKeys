// src/main/java/me/javivi/kindlykeys/shared/net/s2c/DisabledKeyListRequestS2C.java
package me.javivi.kindlykeys.shared.net.s2c;

import java.util.UUID;
import java.util.function.Supplier;
import me.javivi.kindlykeys.shared.net.ModPacketHandler;
import me.javivi.kindlykeys.shared.net.c2s.SendDisabledKeysC2S;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

public class DisabledKeyListRequestS2C {
    private final UUID requester;

    public DisabledKeyListRequestS2C(UUID requester) {
        this.requester = requester;
    }

    public DisabledKeyListRequestS2C(FriendlyByteBuf buf) {
        this.requester = buf.readUUID();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeUUID(this.requester);
    }

    public static boolean handle(DisabledKeyListRequestS2C message, Supplier<NetworkEvent.Context> contextSupplier) {
        if (contextSupplier.get().getDirection().getReceptionSide().isClient()) {
            contextSupplier.get().enqueueWork(() -> {
                DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> () -> ClientOnly.sendDisabledKeys(message.requester));
            });
            contextSupplier.get().setPacketHandled(true);
        }
        return true;
    }

    // Inner class to handle client-only code
    private static class ClientOnly {
        public static void sendDisabledKeys(UUID requester) {
            ModPacketHandler.sendToServer(
                    new SendDisabledKeysC2S(me.javivi.kindlykeys.client.io.LockedKeysIO.INSTANCE.getDisabledKeys(), requester)
            );
        }
    }
}