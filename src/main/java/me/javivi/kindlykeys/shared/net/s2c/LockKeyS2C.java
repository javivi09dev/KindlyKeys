// src/main/java/me/javivi/kindlykeys/shared/net/s2c/LockKeyS2C.java
package me.javivi.kindlykeys.shared.net.s2c;

import java.util.function.Supplier;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

public class LockKeyS2C {
    private final String keyName;
    private final boolean locked;

    public LockKeyS2C(String keyName, boolean locked) {
        this.keyName = keyName;
        this.locked = locked;
    }

    public LockKeyS2C(FriendlyByteBuf buf) {
        this.keyName = buf.readUtf();
        this.locked = buf.readBoolean();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeUtf(this.keyName);
        buf.writeBoolean(this.locked);
    }

    public static void handle(LockKeyS2C message, Supplier<NetworkEvent.Context> contextSupplier) {
        if (contextSupplier.get().getDirection().getReceptionSide().isClient()) {
            me.javivi.kindlykeys.client.net.ClientHandler.handleLockKey(message, contextSupplier);
        }
    }

    public String getKeyName() {
        return keyName;
    }

    public boolean isLocked() {
        return locked;
    }
}