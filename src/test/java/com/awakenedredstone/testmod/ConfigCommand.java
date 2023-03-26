package com.awakenedredstone.testmod;

import com.awakenedredstone.cbserverconfig.ui.ConfigScreen;
import com.awakenedredstone.cbserverconfig.CBServerConfig;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

import static net.minecraft.server.command.CommandManager.literal;

public class ConfigCommand {

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(literal("cherryblossom").requires(source -> source.hasPermissionLevel(2))
                .then(literal("config")
                        .executes(context -> execute(context.getSource()))
        ));
    }

    public static int execute(ServerCommandSource source) {
        try {
            if (!source.isExecutedByPlayer()) {
                source.sendError(Text.literal("Only players can execute this command!"));
                return 0;
            }
            new ConfigScreen(source.getPlayer(), TestMod.CONFIG, null, null);
        } catch (Exception e) {
            CBServerConfig.LOGGER.error("", e);
            throw e;
        }
        return 1;
    }
}