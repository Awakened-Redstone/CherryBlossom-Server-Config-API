package com.awakenedredstone.cbserverconfig.command;

import com.awakenedredstone.cbserverconfig.CBServerConfig;
import com.awakenedredstone.cbserverconfig.polymer.CBGuiElement;
import com.awakenedredstone.cbserverconfig.processor.ConfigProcessor;
import com.mojang.brigadier.CommandDispatcher;
import eu.pb4.sgui.api.gui.SimpleGui;
import eu.pb4.sgui.api.gui.SimpleGuiBuilder;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

import java.util.List;

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
            List<CBGuiElement> elements = ConfigProcessor.buildFileItems();

            SimpleGuiBuilder guiBuilder = new SimpleGuiBuilder(ScreenHandlerType.GENERIC_9X6, false);
            guiBuilder.setTitle(Text.literal("Configurations"));

            for (CBGuiElement element : elements) {
                guiBuilder.addSlot(element);
            }

            SimpleGui gui = guiBuilder.build(source.getPlayer());

            gui.open();
        } catch (Exception e) {
            CBServerConfig.LOGGER.error("", e);
            throw e;
        }
        return 1;
    }
}