package com.awakenedredstone.cbserverconfig.api.config;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnmodifiableView;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ConfigEntryProcessorManager {
    private static final Map<Class<?>, Class<? extends ConfigEntryProcessor<?>>> configProcessors = new HashMap<>();

    @Contract(pure = true)
    @NotNull @UnmodifiableView
    public static Map<Class<?>, Class<? extends ConfigEntryProcessor<?>>> getConfigProcessors() {
        return Collections.unmodifiableMap(configProcessors);
    }

    public static void register(Class<?> targetClass, Class<? extends ConfigEntryProcessor<?>> processor) {
        if (configProcessors.containsKey(targetClass)) throw new UnsupportedOperationException(String.format("It is not allowed to register more than one config processor for a class! Class: %s, Blocked processor: %s", targetClass.getName(), processor.getName()));
        configProcessors.put(targetClass, processor);
    }
}
