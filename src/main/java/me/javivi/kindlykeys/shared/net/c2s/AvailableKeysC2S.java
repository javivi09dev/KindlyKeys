package me.javivi.kindlykeys.shared.net.c2s;

import com.google.common.collect.Lists;
import java.util.Collection;
import java.util.function.Supplier;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import com.google.common.collect.Lists;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.Collection;
import java.util.function.Supplier;

public class AvailableKeysC2S {
    // Se recomienda que sea 'private' y solo accesible mediante un metodo.
    private static Collection<String> availableKeys = Lists.newArrayList();
    private final Collection<String> keys;

    // Constructor para inicializar la colección de teclas disponibles.
    public AvailableKeysC2S(Collection<String> keys) {
        this.keys = keys;
    }

    // Constructor para deserializar desde el buffer.
    public AvailableKeysC2S(FriendlyByteBuf buf) {
        this.keys = buf.readCollection(Lists::newArrayListWithCapacity, FriendlyByteBuf::readUtf);
    }

    // Metodo para serializar la colección de teclas al buffer.
    public void toBytes(FriendlyByteBuf buf) {
        buf.writeCollection(this.keys, FriendlyByteBuf::writeUtf);
    }

    // Metodo para manejar la recepción del paquete en el servidor.
    public boolean handle(Supplier<NetworkEvent.Context> contextSupplier) {
        contextSupplier.get().enqueueWork(() -> {
            availableKeys = this.keys; // Actualiza la colección de teclas disponibles.
        });
        return true; // Indica que el manejo fue exitoso.
    }

    // Metodo estático para obtener las teclas disponibles.
    public static Collection<String> getAvailableKeys() {
        return availableKeys;
    }
}