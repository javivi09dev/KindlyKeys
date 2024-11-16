package me.javivi.kindlykeys.shared.net;

import me.javivi.kindlykeys.shared.net.c2s.AvailableKeysC2S;
import me.javivi.kindlykeys.shared.net.c2s.SendDisabledKeysC2S;
import me.javivi.kindlykeys.shared.net.s2c.AvailableKeyListRequestS2C;
import me.javivi.kindlykeys.shared.net.s2c.ChangePerspectiveS2C;
import me.javivi.kindlykeys.shared.net.s2c.DisabledKeyListRequestS2C;
import me.javivi.kindlykeys.shared.net.s2c.KeyDebugS2C;
import me.javivi.kindlykeys.shared.net.s2c.LockKeyS2C;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.NetworkRegistry.ChannelBuilder;
import net.minecraftforge.network.simple.SimpleChannel;

public class ModPacketHandler {
    private static SimpleChannel CHANNEL;
    private static int packetId = 0;

    public ModPacketHandler() {
    }

    private static int nextId() {
        return packetId++;
    }

    public static void register() {
        CHANNEL = ChannelBuilder.named(new ResourceLocation("keychanger", "packet_messages")).networkProtocolVersion(() -> {
            return "1.0-SNAPSHOT";
        }).clientAcceptedVersions((s) -> {
            return true;
        }).serverAcceptedVersions((s) -> {
            return true;
        }).simpleChannel();
        CHANNEL.messageBuilder(ChangePerspectiveS2C.class, nextId(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(ChangePerspectiveS2C::new)
                .encoder(ChangePerspectiveS2C::toBytes)
                .consumerNetworkThread(ChangePerspectiveS2C::handle)
                .add();

        CHANNEL.messageBuilder(LockKeyS2C.class, nextId(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(LockKeyS2C::new)
                .encoder(LockKeyS2C::toBytes)
                .consumerNetworkThread(LockKeyS2C::handle)
                .add();

        CHANNEL.messageBuilder(KeyDebugS2C.class, nextId(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(KeyDebugS2C::new)
                .encoder(KeyDebugS2C::toBytes)
                .consumerNetworkThread(KeyDebugS2C::handle)
                .add();

        CHANNEL.messageBuilder(DisabledKeyListRequestS2C.class, nextId(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(DisabledKeyListRequestS2C::new)
                .encoder(DisabledKeyListRequestS2C::toBytes)
                .consumerNetworkThread(DisabledKeyListRequestS2C::handle)
                .add();

        CHANNEL.messageBuilder(SendDisabledKeysC2S.class, nextId(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(SendDisabledKeysC2S::new)
                .encoder(SendDisabledKeysC2S::toBytes)
                .consumerNetworkThread(SendDisabledKeysC2S::handle)
                .add();

        CHANNEL.messageBuilder(AvailableKeyListRequestS2C.class, nextId(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(AvailableKeyListRequestS2C::new)
                .encoder(AvailableKeyListRequestS2C::toBytes)
                .consumerNetworkThread(AvailableKeyListRequestS2C::handle)
                .add();

        CHANNEL.messageBuilder(AvailableKeysC2S.class, nextId(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(AvailableKeysC2S::new)
                .encoder(AvailableKeysC2S::toBytes)
                .consumerNetworkThread(AvailableKeysC2S::handle)
                .add();
    }
    public static <Message> void sendToAllPlayers(Message message) {
        CHANNEL.send(PacketDistributor.ALL.noArg(), message);
    }

    public static <Message> void sendToPlayer(ServerPlayer player, Message message) {
        CHANNEL.send(PacketDistributor.PLAYER.with(() -> {
            return player;
        }), message);
    }

    public static <Message> void sendToServer(Message message) {
        CHANNEL.sendToServer(message);
    }
}
