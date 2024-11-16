// src/main/java/me/javivi/kindlykeys/shared/net/s2c/KeyDebugS2C.java
package me.javivi.kindlykeys.shared.net.s2c;

import java.util.function.Supplier;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

public class KeyDebugS2C {
    private final boolean debugEnabled;

    public KeyDebugS2C(boolean debugEnabled) {
        this.debugEnabled = debugEnabled;
    }

    public KeyDebugS2C(FriendlyByteBuf buf) {
        this.debugEnabled = buf.readBoolean();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeBoolean(this.debugEnabled);
    }

    public static boolean handle(KeyDebugS2C message, Supplier<NetworkEvent.Context> contextSupplier) {
        if (contextSupplier.get().getDirection().getReceptionSide().isClient()) {
            me.javivi.kindlykeys.client.net.ClientHandler.handleKeyDebug(message, contextSupplier);
        }
        contextSupplier.get().setPacketHandled(true);
        return true;
    }

    public boolean isDebugEnabled() {
        return debugEnabled;
    }
}