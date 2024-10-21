package me.javivi.kindlykeys.shared.net.s2c;

import java.util.UUID;
import java.util.function.Supplier;
import me.javivi.kindlykeys.client.io.LockedKeysIO;
import me.javivi.kindlykeys.shared.net.ModPacketHandler;
import me.javivi.kindlykeys.shared.net.c2s.SendDisabledKeysC2S;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

public class DisabledKeyListRequestS2C {
    private final UUID requester;

    // Constructor que recibe el UUID del solicitante
    public DisabledKeyListRequestS2C(UUID requester) {
        this.requester = requester;
    }

    // Constructor que deserializa el UUID desde el buffer
    public DisabledKeyListRequestS2C(FriendlyByteBuf buf) {
        this.requester = buf.readUUID();
    }

    // Serialización del UUID hacia el buffer
    public void toBytes(FriendlyByteBuf buf) {
        buf.writeUUID(this.requester);
    }

    // Manejo del paquete al recibirlo
    public boolean handle(Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();

        context.enqueueWork(() -> {
            // Envía el paquete con las teclas deshabilitadas al servidor
            ModPacketHandler.sendToServer(
                    new SendDisabledKeysC2S(LockedKeysIO.INSTANCE.getDisabledKeys(), this.requester)
            );
        });

        return true;
    }
}

