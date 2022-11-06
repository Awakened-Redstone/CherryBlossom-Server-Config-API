package com.awakenedredstone.cbserverconfig.processor;

import com.awakenedredstone.cbserverconfig.api.config.ConfigManager;
import com.awakenedredstone.cbserverconfig.gui.ConfigGui;
import com.awakenedredstone.cbserverconfig.polymer.CBGuiElement;
import com.awakenedredstone.cbserverconfig.polymer.CBGuiElementBuilder;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ConfigProcessor {
    @NotNull
    public static List<CBGuiElement> buildFileItems() {
        List<CBGuiElement> elements = new ArrayList<>();
        ConfigManager.getConfigs().forEach((identifier, configManager) -> {
            CBGuiElement icon = ConfigManager.getConfigIcon(configManager.getConfig(), configManager.identifier);

            CBGuiElementBuilder builder = CBGuiElementBuilder.from(icon).setCallback((index, type, action, gui) -> {
                ConfigGui configGui = new ConfigGui(ConfigManager.getConfig(configManager.identifier), gui);
                configGui.buildGui(gui.getPlayer()).open();
            });

            elements.add(builder.build());
        });

        return elements;
    }
}
