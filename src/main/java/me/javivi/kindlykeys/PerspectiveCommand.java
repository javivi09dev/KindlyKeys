package me.javivi.kindlykeys;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import me.javivi.kindlykeys.shared.net.PerspectiveManager;
import me.javivi.kindlykeys.shared.objects.PerspectiveType;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.level.ServerPlayer;

public class PerspectiveCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("changeperspective")
                .then(Commands.argument("perspective", StringArgumentType.string())
                        .executes(context -> {
                            String perspective = StringArgumentType.getString(context, "perspective");
                            ServerPlayer player = context.getSource().getPlayerOrException();
                            PerspectiveType perspectiveType = PerspectiveType.valueOf(perspective.toUpperCase());
                            PerspectiveManager.changePerspective(player, perspectiveType);
                            return 1;
                        })));
    }
}
