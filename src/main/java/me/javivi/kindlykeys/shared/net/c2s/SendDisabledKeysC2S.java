package me.javivi.kindlykeys.shared.net.c2s;

import com.google.common.collect.Lists;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.Collection;
import java.util.Iterator;
import java.util.UUID;
import java.util.function.Supplier;

public class SendDisabledKeysC2S {
    private final Collection<String> disabledKeys;
    private final UUID requesterUUID;

    // Constructor para inicializar la colección de teclas deshabilitadas y el UUID del solicitante.
    public SendDisabledKeysC2S(Collection<String> disabledKeys, UUID requesterUUID) {
        this.disabledKeys = disabledKeys;
        this.requesterUUID = requesterUUID;
    }

    // Constructor para deserializar desde el buffer.
    public SendDisabledKeysC2S(FriendlyByteBuf buf) {
        this.disabledKeys = buf.readCollection(Lists::newArrayListWithCapacity, FriendlyByteBuf::readUtf);
        this.requesterUUID = buf.readUUID();
    }

    // Método para serializar la colección de teclas deshabilitadas y el UUID del solicitante al buffer.
    public void toBytes(FriendlyByteBuf buf) {
        buf.writeCollection(this.disabledKeys, FriendlyByteBuf::writeUtf);
        buf.writeUUID(this.requesterUUID);
    }

    // Método para manejar la recepción del paquete en el servidor.
    public boolean handle(Supplier<NetworkEvent.Context> contextSupplier) {
        contextSupplier.get().enqueueWork(() -> {
            ServerPlayer player = contextSupplier.get().getSender();
            if (player != null) {
                MinecraftServer server = player.getServer();
                ServerPlayer requester = server.getPlayerList().getPlayer(requesterUUID);
                if (requester != null) {
                    StringBuilder disabledKeysMessage = new StringBuilder();
                    disabledKeysMessage.append(String.format("%s has the following keys disabled: ", requester.getDisplayName().getString()));
                    for (String key : this.disabledKeys) {
                        disabledKeysMessage.append(key).append(", ");
                    }

                    // Eliminar la última coma y espacio
                    if (disabledKeysMessage.length() > 2) {
                        disabledKeysMessage.setLength(disabledKeysMessage.length() - 2);
                    }

                    requester.sendSystemMessage(Component.literal(disabledKeysMessage.toString()), false);
                }
            }
        });
        return true; // Indica que el manejo fue exitoso.
    }
}

