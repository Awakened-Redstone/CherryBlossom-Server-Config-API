package com.awakenedredstone.testmod;

import blue.endless.jankson.JsonArray;
import blue.endless.jankson.JsonPrimitive;
import com.awakenedredstone.testmod.TestConfig;
import com.mojang.brigadier.CommandDispatcher;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

public class TestMod implements ModInitializer {
    public static final String MOD_ID = "testmod";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    public static final TestConfig CONFIG = TestConfig.createAndLoad(builder -> {
        builder.registerSerializer(UUID.class, (uuid, marshaller) -> {
            JsonArray array = new JsonArray();
            array.add(new JsonPrimitive(uuid.getMostSignificantBits()));
            array.add(new JsonPrimitive(uuid.getLeastSignificantBits()));
            return array;
        });
        builder.registerDeserializer(JsonArray.class, UUID.class, (json, m) -> new UUID(json.getLong(0, 0), json.getLong(1, 0)));
    });

    @Override
    public void onInitialize() {
        CommandRegistrationCallback.EVENT.register(this::initCommands);
    }

    private void initCommands(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess registryAccess, CommandManager.RegistrationEnvironment environment) {
        ConfigCommand.register(dispatcher);
    }
}
