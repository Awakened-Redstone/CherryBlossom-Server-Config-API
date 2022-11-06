package com.awakenedredstone.cbserverconfig.api.config;

import com.awakenedredstone.cbserverconfig.polymer.CBGuiElement;
import com.awakenedredstone.cbserverconfig.util.Utils;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;

public record ConfigEntry(Identifier identifier, String name, @Nullable String description, ConfigEntryProcessor<?> processor, Field field, ConfigManager<?> manager) {

    public CBGuiElement getIcon() {
        return Utils.getFieldIcon(field, manager.getConfig(), processor, manager.identifier);
    }
}
