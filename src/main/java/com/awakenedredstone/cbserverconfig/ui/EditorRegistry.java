package com.awakenedredstone.cbserverconfig.ui;

import eu.pb4.sgui.api.ClickType;
import eu.pb4.sgui.api.gui.SlotGuiInterface;
import io.wispforest.owo.config.Option;
import net.minecraft.screen.slot.SlotActionType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class EditorRegistry {

    private static final Map<Class<?>, EditCallback<?>> EDITORS = new HashMap<>();
    private static final Map<Option.Key, EditCallback<?>> OPTION_EDITORS = new HashMap<>();

    public static <T> void register(@NotNull Class<T> clazz, @NotNull EditCallback<T> callback) {
        if (EDITORS.putIfAbsent(clazz, callback) != null) {
            throw new IllegalStateException("Adding duplicate key '" + clazz.getName() + "' to registry");
        }
    }

    public static <T> void register(Option.Key key, EditCallback<T> callback) {
        OPTION_EDITORS.put(key, callback);
    }

    @Nullable
    @SuppressWarnings("unchecked")
    public static <T> EditCallback<T> getEditor(Class<T> clazz, Option.Key key) {
        return (EditCallback<T>) OPTION_EDITORS.getOrDefault(key, EDITORS.get(clazz));
    }

    @SuppressWarnings("unchecked")
    public static <T> CompletableFuture<T> trigger(@NotNull EditCallback<T> callback, Object value, ClickType clickType, SlotActionType slotAction, SlotGuiInterface gui) {
        return callback.edit((T) value, clickType, slotAction, gui);
    }

    @FunctionalInterface
    public interface EditCallback<T> {

        /**
         * Executed when a config is clicked.
         * @param currentValue the current option value
         * @param clickType the click type on the item
         * @param slotAction the slot action on the item
         * @param gui the gui
         */
        CompletableFuture<T> edit(T currentValue, ClickType clickType, SlotActionType slotAction, SlotGuiInterface gui);
    }
}
