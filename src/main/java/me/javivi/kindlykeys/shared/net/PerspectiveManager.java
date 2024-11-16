package me.javivi.kindlykeys.shared.net;

import me.javivi.kindlykeys.shared.net.ModPacketHandler;
import me.javivi.kindlykeys.shared.net.s2c.ChangePerspectiveS2C;
import me.javivi.kindlykeys.shared.objects.PerspectiveType;
import net.minecraft.server.level.ServerPlayer;

public class PerspectiveManager {
    public static void changePerspective(ServerPlayer player, PerspectiveType perspectiveType) {
        ChangePerspectiveS2C packet = new ChangePerspectiveS2C(perspectiveType);
        ModPacketHandler.sendToPlayer(player, packet);
    }
}