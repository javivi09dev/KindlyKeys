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

    // Constructor que recibe el UUID de la solicitud
    public AvailableKeyListRequestS2C(UUID reqUUID) {
        this.reqUUID = reqUUID;
    }

    // Constructor que deserializa los datos desde el buffer
    public AvailableKeyListRequestS2C(FriendlyByteBuf buf) {
        this.reqUUID = buf.readUUID();
    }

    // Serialización de los datos hacia el buffer
    public void toBytes(FriendlyByteBuf buf) {
        buf.writeUUID(this.reqUUID);
    }

    // Método que maneja la recepción del paquete
    public boolean handle(Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();

        context.enqueueWork(() -> {
            // Obtener las mapeos de teclas (KeyMappings) desde Minecraft
            KeyMapping[] mappings = Minecraft.getInstance().options.keyMappings;

            // Crear un set con los nombres de las teclas disponibles
            Set<String> mappingNames = new HashSet<>();
            for (KeyMapping mapping : mappings) {
                mappingNames.add(mapping.getName());
            }

            // Enviar un paquete al servidor con las teclas disponibles
            ModPacketHandler.sendToServer(new AvailableKeysC2S(mappingNames));
        });

        return true;
    }
}

