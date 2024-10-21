package me.javivi.kindlykeys.shared.net.s2c;

import java.util.function.Supplier;
import me.javivi.kindlykeys.shared.net.s2c.ChangePerspectiveS2C;
import me.javivi.kindlykeys.shared.objects.PerspectiveType;
import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

public class ChangePerspectiveS2C {
    private final PerspectiveType perspectiveType;

    // Constructor con el tipo de perspectiva
    public ChangePerspectiveS2C(PerspectiveType perspectiveType) {
        this.perspectiveType = perspectiveType;
    }

    // Constructor que recibe los datos desde un buffer (deserialización)
    public ChangePerspectiveS2C(FriendlyByteBuf buf) {
        // Cambié el método para que el código use correctamente la deserialización
        this.perspectiveType = buf.readEnum(PerspectiveType.class);
    }

    // Método para serializar los datos (enviar los bytes)
    public void toBytes(FriendlyByteBuf friendlyByteBuf) {
        // Uso de writeEnum en lugar de un método desconocido
        friendlyByteBuf.writeEnum(this.perspectiveType);
    }

    // Método para manejar el cambio de perspectiva
    public boolean handle(Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();

        context.enqueueWork(() -> {
            // Se obtiene la configuración de opciones de Minecraft
            Options options = Minecraft.getInstance().options;

            // Selección de tipo de cámara según el PerspectiveType recibido
            CameraType cameraType;
            switch (perspectiveType) {
                case FIRST_PERSON -> cameraType = CameraType.FIRST_PERSON;
                case THIRD_PERSON_BACK -> cameraType = CameraType.THIRD_PERSON_BACK;
                case THIRD_PERSON_FRONT -> cameraType = CameraType.THIRD_PERSON_FRONT;
                default -> throw new IncompatibleClassChangeError("Tipo de perspectiva desconocido.");
            }

            // Se cambia la perspectiva
            options.setCameraType(cameraType);
        });

        // Señalamos que el manejo fue exitoso
        return true;
    }
}
