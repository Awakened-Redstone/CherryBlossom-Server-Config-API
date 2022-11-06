package com.awakenedredstone.cbserverconfig.api.config;

import com.awakenedredstone.cbserverconfig.polymer.CBGuiElement;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

public record ConfigData(Identifier identifier, String name, @Nullable String description, @Nullable CBGuiElement icon, ConfigManager<?> manager) {
}
