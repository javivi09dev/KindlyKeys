package me.javivi.kindlykeys.shared.net.s2c;

import java.util.function.Supplier;
import me.javivi.kindlykeys.client.io.LockedKeysIO;
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

    public boolean handle(Supplier<NetworkEvent.Context> contextSupplier) {
        ((NetworkEvent.Context)contextSupplier.get()).enqueueWork(() -> {
            LockedKeysIO.INSTANCE.setDebugEnabled(this.debugEnabled);
        });
        return true;
    }
}