package com.awakenedredstone.cbserverconfig;

import com.awakenedredstone.cbserverconfig.api.config.ConfigManager;
import com.awakenedredstone.cbserverconfig.command.ConfigCommand;
import com.awakenedredstone.cbserverconfig.processor.ConfigProcessor;
import com.mojang.brigadier.CommandDispatcher;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CBServerConfig implements ModInitializer {
	public static final String MOD_ID = "cherryblossom-server-config";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		ServerLifecycleEvents.SERVER_STARTING.register(server -> {
			ConfigManager.getConfigs().forEach((id, manager) -> manager.loadOrCreateConfig());
			ConfigProcessor.test();
		});
		ServerLifecycleEvents.START_DATA_PACK_RELOAD.register((server, resourceManager)  -> ConfigManager.getConfigs().forEach((id, manager) -> {
			boolean success = manager.loadOrCreateConfig();
			if (success) server.getCommandSource().sendFeedback(Text.literal("Loaded config " + manager.getFilePath()), true);
			else server.getCommandSource().sendFeedback(Text.literal("Failed to load config " + manager.getFilePath()).formatted(Formatting.RED), true);
		}));
		CommandRegistrationCallback.EVENT.register(this::initCommands);
	}

	private void initCommands(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess registryAccess, CommandManager.RegistrationEnvironment environment) {
		ConfigCommand.register(dispatcher);
	}
}
