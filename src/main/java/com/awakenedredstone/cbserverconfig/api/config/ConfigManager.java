package com.awakenedredstone.cbserverconfig.api.config;

import com.awakenedredstone.cbserverconfig.CBServerConfig;
import com.awakenedredstone.cbserverconfig.util.Constants;
import com.awakenedredstone.cbserverconfig.util.JsonHelper;
import com.google.gson.JsonObject;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileReader;
import java.nio.file.Path;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ConfigManager<T extends Config> {
    private static final Path CONFIG_DIR = FabricLoader.getInstance().getConfigDir();
    private static final Map<Identifier, ConfigManager<?>> configs = new HashMap<>();
    public final Path dir;
    public final Identifier identifier;
    public final File configFile;
    public final Class<T> targetClass;
    public final boolean useFolder;
    public final boolean uniqueIcons;

    private T config;

    private ConfigManager(Identifier identifier, Class<T> targetClass, boolean useFolder, boolean uniqueIcons) {
        this.dir = useFolder ? CONFIG_DIR.resolve(identifier.getNamespace()) : CONFIG_DIR;
        this.configFile = dir.resolve(identifier.getPath() + ".json").toFile();
        this.uniqueIcons = uniqueIcons;
        this.identifier = identifier;
        this.useFolder = useFolder;
        this.targetClass = targetClass;
    }

    public boolean loadOrCreateConfig() {
        try {
            if ((dir.toFile().exists() && dir.toFile().isDirectory()) || dir.toFile().mkdirs()) {
                if (configFile.exists() && configFile.isFile() && configFile.canRead()) {
                    config = Constants.GSON.fromJson(new FileReader(configFile), targetClass);
                } else if (!configFile.exists()) {
                    JsonObject defaultConfig = defaultConfig();
                    if (defaultConfig == null) {
                        CBServerConfig.LOGGER.error("Failed to get default state of \"{}\"! Target file: \"{}\"", identifier, getFilePath());
                        return false;
                    }
                    JsonHelper.writeJsonToFile(defaultConfig, getConfigFile());
                    config = targetClass.getConstructor().newInstance();
                }
                if (config != null) {
                    CBServerConfig.LOGGER.info("Loaded config {} [{}]", getFilePath(), identifier);
                    return true;
                } else {
                    CBServerConfig.LOGGER.error("Failed to load config {} [{}]", getFilePath(), identifier);
                    return false;
                }
            } else {
                CBServerConfig.LOGGER.error("Failed to access config file or directory! Target file: {} [{}]", getFilePath(), identifier);
                return false;
            }
        } catch (Exception exception) {
            CBServerConfig.LOGGER.error(String.format("An error occurred when trying to load config %s!, Target file: %s", identifier, getFilePath()), exception);
            return false;
        }
    }

    @Nullable
    public JsonObject defaultConfig() {
        try {
            return Constants.GSON.toJsonTree(targetClass.getConstructor().newInstance()).getAsJsonObject();
        } catch (NoSuchMethodException e) {
            CBServerConfig.LOGGER.error(String.format("Failed to get constructor of \"%s\" for file \"%s\"", targetClass.getName(), getFilePath()), e);
            return null;
        } catch (Exception e) {
            CBServerConfig.LOGGER.error(String.format("Failed to initiate \"%s\" for file \"%s\" due to \"%s\"", targetClass.getName(), getFilePath(), e.getClass().getName()), e);
            return null;
        }
    }

    public T getConfig() {
        return config;
    }

    public void save() {
        if (!dir.toFile().exists()) dir.toFile().mkdirs();
        JsonHelper.writeJsonToFile(Constants.GSON.toJsonTree(config).getAsJsonObject(), configFile);
    }

    public File getConfigFile() {
        return configFile;
    }

    public String getFilePath() {
        return configFile.getPath().replaceFirst(CONFIG_DIR.toFile().getPath(), "").substring(1);
    }

    public static <T extends Config> ConfigManager<T> register(Identifier identifier, Class<T> clazz, boolean useFolder) {
        return register(identifier, clazz, useFolder, false);
    }

    public static <T extends Config> ConfigManager<T> register(Identifier identifier, Class<T> clazz, boolean useFolder, boolean uniqueIcons) {
        if (configs.containsKey(identifier)) throw new UnsupportedOperationException(String.format("Config %s is already registered!", identifier));
        ConfigManager<T> manager = new ConfigManager<>(identifier, clazz, useFolder, uniqueIcons);
        configs.put(identifier, manager);
        return manager;
    }

    public static Map<Identifier, ConfigManager<?>> getConfigs() {
        return Collections.unmodifiableMap(configs);
    }
}
