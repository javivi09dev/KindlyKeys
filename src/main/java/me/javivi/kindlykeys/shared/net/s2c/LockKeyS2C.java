package me.javivi.kindlykeys.shared.net.s2c;

import java.util.function.Supplier;
import me.javivi.kindlykeys.client.io.LockedKeysIO;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

public class LockKeyS2C {
    private final String keyName;
    private final boolean locked;

    // Constructor para inicializar el nombre de la tecla y su estado de bloqueo.
    public LockKeyS2C(String keyName, boolean locked) {
        this.keyName = keyName;
        this.locked = locked;
    }

    // Constructor para deserializar desde el buffer.
    public LockKeyS2C(FriendlyByteBuf buf) {
        this.keyName = buf.readUtf(); // Cambié a readUtf() para mayor claridad.
        this.locked = buf.readBoolean();
    }

    // Metodo para serializar el nombre de la tecla y el estado de bloqueo al buffer.
    public void toBytes(FriendlyByteBuf buf) {
        buf.writeUtf(this.keyName); // Cambié a writeUtf() para mayor claridad.
        buf.writeBoolean(this.locked);
    }

    // Metodo para manejar la recepción del paquete en el cliente.
    public boolean handle(Supplier<NetworkEvent.Context> contextSupplier) {
        contextSupplier.get().enqueueWork(() -> {
            LockedKeysIO.INSTANCE.setKeyLocked(this.keyName, this.locked);
        });
        return true; // Indica que el manejo fue exitoso.
    }
}
