package com.awakenedredstone.cbserverconfig.ui;

import io.wispforest.owo.config.Option;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class ParserRegistry {

    private static final Map<Class<?>, Parser<?>> PARSERS = new HashMap<>();
    private static final Map<Option.Key, Parser<?>> OPTION_PARSERS = new HashMap<>();

    public static <T> void register(@NotNull Class<T> clazz, @NotNull ParserRegistry.Parser<T> callback) {
        if (PARSERS.putIfAbsent(clazz, callback) != null) {
            throw new IllegalStateException("Adding duplicate key '" + clazz.getName() + "' to registry");
        }
    }

    public static <T> void register(Option.Key key, Parser<T> callback) {
        OPTION_PARSERS.put(key, callback);
    }

    @NotNull
    @SuppressWarnings("unchecked")
    public static <T> ParserRegistry.Parser<T> getParser(Class<T> clazz, Option.Key key) {
        return (Parser<T>) OPTION_PARSERS.getOrDefault(key, PARSERS.getOrDefault(clazz, Object::toString));
    }

    @SuppressWarnings("unchecked")
    public static <T> String parse(@NotNull ParserRegistry.Parser<T> callback, Object value) {
        return callback.parse((T) value);
    }

    @FunctionalInterface
    public interface Parser<T> {

        String parse(T currentValue);
    }
}
