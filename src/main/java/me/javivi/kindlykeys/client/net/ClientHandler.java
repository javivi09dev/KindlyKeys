// src/main/java/me/javivi/kindlykeys/client/net/ClientHandler.java
package me.javivi.kindlykeys.client.net;

import me.javivi.kindlykeys.client.io.LockedKeysIO;
import me.javivi.kindlykeys.shared.net.s2c.KeyDebugS2C;
import me.javivi.kindlykeys.shared.net.s2c.LockKeyS2C;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

@OnlyIn(Dist.CLIENT)
public class ClientHandler {
    public static void handleLockKey(LockKeyS2C message, Supplier<NetworkEvent.Context> contextSupplier) {
        contextSupplier.get().enqueueWork(() -> {
            LockedKeysIO.INSTANCE.setKeyLocked(message.getKeyName(), message.isLocked());
        });
        contextSupplier.get().setPacketHandled(true);
    }

    public static void handleKeyDebug(KeyDebugS2C message, Supplier<NetworkEvent.Context> contextSupplier) {
        contextSupplier.get().enqueueWork(() -> {
            LockedKeysIO.INSTANCE.setDebugEnabled(message.isDebugEnabled());
        });
        contextSupplier.get().setPacketHandled(true);
    }
}