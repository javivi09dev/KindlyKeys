// src/main/java/me/javivi/kindlykeys/shared/net/s2c/AvailableKeyListRequestS2C.java
package me.javivi.kindlykeys.shared.net.s2c;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.function.Supplier;
import me.javivi.kindlykeys.shared.net.ModPacketHandler;
import me.javivi.kindlykeys.shared.net.c2s.AvailableKeysC2S;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

public class AvailableKeyListRequestS2C {
    private final UUID reqUUID;

    public AvailableKeyListRequestS2C(UUID reqUUID) {
        this.reqUUID = reqUUID;
    }

    public AvailableKeyListRequestS2C(FriendlyByteBuf buf) {
        this.reqUUID = buf.readUUID();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeUUID(this.reqUUID);
    }

    public static boolean handle(AvailableKeyListRequestS2C message, Supplier<NetworkEvent.Context> contextSupplier) {
        if (contextSupplier.get().getDirection().getReceptionSide().isClient()) {
            contextSupplier.get().enqueueWork(() -> {
                KeyMapping[] mappings = Minecraft.getInstance().options.keyMappings;
                Set<String> mappingNames = new HashSet<>();
                for (KeyMapping mapping : mappings) {
                    mappingNames.add(mapping.getName());
                }
                ModPacketHandler.sendToServer(new AvailableKeysC2S(mappingNames));
            });
            contextSupplier.get().setPacketHandled(true);
        }
        return true;
    }
}