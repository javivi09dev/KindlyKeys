// src/main/java/me/javivi/kindlykeys/PerspectiveCommand.java
package me.javivi.kindlykeys;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import me.javivi.kindlykeys.client.io.LockedKeysIO;
import me.javivi.kindlykeys.shared.net.PerspectiveManager;
import me.javivi.kindlykeys.shared.objects.PerspectiveType;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

import java.util.Collection;
import java.util.function.Supplier;

public class PerspectiveCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("changeperspective")
                .then(Commands.argument("targets", EntityArgument.players())
                        .then(Commands.argument("perspective", StringArgumentType.string())
                                .executes(context -> {
                                    Collection<ServerPlayer> players = EntityArgument.getPlayers(context, "targets");
                                    String perspective = StringArgumentType.getString(context, "perspective");
                                    PerspectiveType perspectiveType = PerspectiveType.valueOf(perspective.toUpperCase());
                                    for (ServerPlayer player : players) {
                                        PerspectiveManager.changePerspective(player, perspectiveType);
                                    }
                                    return 1;
                                }))));

        dispatcher.register(Commands.literal("unlockallkeys")
                .then(Commands.argument("targets", EntityArgument.players())
                        .executes(context -> {
                            Collection<ServerPlayer> players = EntityArgument.getPlayers(context, "targets");
                            for (ServerPlayer player : players) {
                                LockedKeysIO.INSTANCE.unlockAllKeys();
                                context.getSource().sendSuccess((Supplier<Component>) Component.literal("All keys have been unlocked for " + player.getName().getString()), true);
                            }
                            return 1;
                        })));

        dispatcher.register(Commands.literal("lockkey")
                .then(Commands.argument("targets", EntityArgument.players())
                        .then(Commands.argument("keyName", StringArgumentType.string())
                                .executes(context -> {
                                    Collection<ServerPlayer> players = EntityArgument.getPlayers(context, "targets");
                                    String keyName = StringArgumentType.getString(context, "keyName");
                                    for (ServerPlayer player : players) {
                                        LockedKeysIO.INSTANCE.setKeyLocked(keyName, true);
                                        context.getSource().sendSuccess((Supplier<Component>) Component.literal("Key " + keyName + " has been locked for " + player.getName().getString()), true);
                                    }
                                    return 1;
                                }))));
    }
}