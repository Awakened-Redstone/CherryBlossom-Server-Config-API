package com.awakenedredstone.cbserverconfig.editor;

import com.awakenedredstone.cbserverconfig.ui.EditorRegistry;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public class DefaultEditors {

    public static final EditorRegistry.EditCallback<String> STRING = (currentValue, clickType, slotAction, gui) -> {
        CompletableFuture<String> future = new CompletableFuture<>();
        new StringInputGui(gui.getPlayer(), currentValue, value -> Optional.empty(), value -> {
            gui.getPlayer().playSound(SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.MASTER, 0.3f, 1);
            future.complete(value);
            gui.close();
            gui.open();
        });
        return future;
    };

    public static final EditorRegistry.EditCallback<Boolean> BOOLEAN = (currentValue, clickType, slotAction, gui) -> {
        return CompletableFuture.completedFuture(!currentValue);
    };

    public static final EditorRegistry.EditCallback<Double> DOUBLE = (currentValue, clickType, slotAction, gui) -> {
        CompletableFuture<Double> future = new CompletableFuture<>();
        new DoubleInputGui(gui.getPlayer(), currentValue, value -> Optional.empty(), value -> {
            gui.getPlayer().playSound(SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.MASTER, 0.3f, 1);
            future.complete(value);
            gui.close();
            gui.open();
        });
        return future;
    };

    public static final EditorRegistry.EditCallback<Float> FLOAT = (currentValue, clickType, slotAction, gui) -> {
        CompletableFuture<Float> future = new CompletableFuture<>();
        new FloatInputGui(gui.getPlayer(), currentValue, value -> Optional.empty(), value -> {
            gui.getPlayer().playSound(SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.MASTER, 0.3f, 1);
            future.complete(value);
            gui.close();
            gui.open();
        });
        return future;
    };

    public static final EditorRegistry.EditCallback<Long> LONG = (currentValue, clickType, slotAction, gui) -> {
        CompletableFuture<Long> future = new CompletableFuture<>();
        new LongInputGui(gui.getPlayer(), currentValue, value -> Optional.empty(), value -> {
            gui.getPlayer().playSound(SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.MASTER, 0.3f, 1);
            future.complete(value);
            gui.close();
            gui.open();
        });
        return future;
    };

    public static final EditorRegistry.EditCallback<Integer> INTEGER = (currentValue, clickType, slotAction, gui) -> {
        CompletableFuture<Integer> future = new CompletableFuture<>();
        new IntegerInputGui(gui.getPlayer(), currentValue, value -> Optional.empty(), value -> {
            gui.getPlayer().playSound(SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.MASTER, 0.3f, 1);
            future.complete(value);
            gui.close();
            gui.open();
        });
        return future;
    };

    public static final EditorRegistry.EditCallback<Short> SHORT = (currentValue, clickType, slotAction, gui) -> {
        CompletableFuture<Short> future = new CompletableFuture<>();
        new ShortInputGui(gui.getPlayer(), currentValue, value -> Optional.empty(), value -> {
            gui.getPlayer().playSound(SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.MASTER, 0.3f, 1);
            future.complete(value);
            gui.close();
            gui.open();
        });
        return future;
    };

    public static final EditorRegistry.EditCallback<Byte> BYTE = (currentValue, clickType, slotAction, gui) -> {
        CompletableFuture<Byte> future = new CompletableFuture<>();
        new ByteInputGui(gui.getPlayer(), currentValue, value -> Optional.empty(), value -> {
            gui.getPlayer().playSound(SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.MASTER, 0.3f, 1);
            future.complete(value);
            gui.close();
            gui.open();
        });
        return future;
    };
}
