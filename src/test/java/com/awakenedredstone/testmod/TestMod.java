package com.awakenedredstone.testmod;

import com.awakenedredstone.cbserverconfig.api.config.ConfigManager;
import net.fabricmc.api.ModInitializer;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestMod implements ModInitializer {
    public static final String MOD_ID = "testmod";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        ConfigManager.register(new Identifier(MOD_ID, "test"), Config.class, false);
    }
}
