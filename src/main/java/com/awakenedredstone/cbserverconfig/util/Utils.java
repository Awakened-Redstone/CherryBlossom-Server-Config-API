package com.awakenedredstone.cbserverconfig.util;

import com.awakenedredstone.cbserverconfig.annotation.Description;
import com.awakenedredstone.cbserverconfig.annotation.Name;
import com.awakenedredstone.cbserverconfig.annotation.Processor;
import com.awakenedredstone.cbserverconfig.api.config.Config;
import com.awakenedredstone.cbserverconfig.api.config.ConfigEntryProcessor;
import com.awakenedredstone.cbserverconfig.api.config.ConfigProcessorManager;
import net.minecraft.util.crash.CrashException;
import net.minecraft.util.crash.CrashReport;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

public class Utils {
    public static ConfigEntryProcessor<?> getProcessor(Field field, Config config) {
        Processor processorAnnotation = field.getAnnotation(Processor.class);
        Name nameAnnotation = field.getAnnotation(Name.class);
        String name = nameAnnotation == null ? field.getName() : nameAnnotation.value();

        if (processorAnnotation != null) {
            try {
                Constructor<?>[] constructors = processorAnnotation.value().getConstructors();
                if (constructors.length != 1) throw createCrashException(new IllegalArgumentException("A config processor must have only one constructor!"), "Illegal config processor");
                return (ConfigEntryProcessor<?>) constructors[0].newInstance(field.get(config), field.get(createDefaultConfig(config)), name);

            } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                String message = String.format("A config processor must have a valid constructor! Bad processor: %s", processorAnnotation.value().getName());
                throw createCrashException(new IllegalArgumentException(message, e), "Illegal config processor");
            }
        } else {
            Map<Class<?>, Class<? extends ConfigEntryProcessor<?>>> processors = ConfigProcessorManager.getConfigProcessors();
            if (processors.containsKey(field.getType())) {
                Class<? extends ConfigEntryProcessor<?>> processorClass = processors.get(field.getType());

                try {
                    Constructor<?>[] constructors = processorClass.getConstructors();
                    if (constructors.length != 1) throw createCrashException(new IllegalArgumentException("A config processor must have only one constructor!"), "Illegal config processor");
                    return (ConfigEntryProcessor<?>) constructors[0].newInstance(field.get(config), field.get(createDefaultConfig(config)), name);

                } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                    String message = String.format("A config processor must have a valid constructor! Bad processor: %s", processorClass.getName());
                    throw createCrashException(new IllegalArgumentException(message, e), "Illegal config processor");
                }
            }
        }


        return null;
    }

    public static CrashException createCrashException(Throwable throwable, String message) {
        return new CrashException(CrashReport.create(throwable, message));
    }

    public static Config createDefaultConfig(Config config) {
        try {
            return config.getClass().getConstructor().newInstance();
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException | NoSuchMethodException e) {
            throw createCrashException(e, "Configs must have an empty constructor!");
        }
    }
}
