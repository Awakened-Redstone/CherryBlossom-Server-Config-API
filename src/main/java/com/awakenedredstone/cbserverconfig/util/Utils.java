package com.awakenedredstone.cbserverconfig.util;

import com.awakenedredstone.cbserverconfig.CBServerConfig;
import com.awakenedredstone.cbserverconfig.annotation.Description;
import com.awakenedredstone.cbserverconfig.annotation.Icon;
import com.awakenedredstone.cbserverconfig.annotation.Name;
import com.awakenedredstone.cbserverconfig.annotation.Processor;
import com.awakenedredstone.cbserverconfig.api.IconSupplier;
import com.awakenedredstone.cbserverconfig.api.config.Config;
import com.awakenedredstone.cbserverconfig.api.config.ConfigEntryProcessor;
import com.awakenedredstone.cbserverconfig.api.config.ConfigEntryProcessorManager;
import com.awakenedredstone.cbserverconfig.api.config.ConfigManager;
import com.awakenedredstone.cbserverconfig.polymer.CBGuiElement;
import com.awakenedredstone.cbserverconfig.polymer.CBGuiElementBuilder;
import com.awakenedredstone.cbserverconfig.polymer.GuiModels;
import eu.pb4.sgui.api.ClickType;
import eu.pb4.sgui.api.SlotHolder;
import eu.pb4.sgui.api.elements.GuiElementInterface;
import net.minecraft.item.Items;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.crash.CrashException;
import net.minecraft.util.crash.CrashReport;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

public class Utils {
    @Nullable
    public static ConfigEntryProcessor<?> getProcessor(@NotNull Field field, Config config) {
        Processor processorAnnotation = field.getAnnotation(Processor.class);
        Name nameAnnotation = field.getAnnotation(Name.class);
        String name = nameAnnotation == null ? field.getName() : nameAnnotation.value();

        if (processorAnnotation != null) {
            try {
                Constructor<?>[] constructors = processorAnnotation.value().getConstructors();
                if (constructors.length != 1)
                    throw createCrashException(new IllegalArgumentException("A config processor must have only one constructor!"), "Illegal config processor");
                return (ConfigEntryProcessor<?>) constructors[0].newInstance(field.get(config), field.get(createDefaultConfig(config)), name);

            } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                String message = String.format("A config processor must have a valid constructor! Bad processor: %s", processorAnnotation.value().getName());
                throw createCrashException(new IllegalArgumentException(message, e), "Illegal config processor");
            }
        } else {
            Map<Class<?>, Class<? extends ConfigEntryProcessor<?>>> processors = ConfigEntryProcessorManager.getConfigProcessors();
            if (processors.containsKey(field.getType())) {
                Class<? extends ConfigEntryProcessor<?>> processorClass = processors.get(field.getType());

                try {
                    Constructor<?>[] constructors = processorClass.getConstructors();
                    if (constructors.length != 1)
                        throw createCrashException(new IllegalArgumentException("A config processor must have only one constructor!"), "Illegal config processor");
                    return (ConfigEntryProcessor<?>) constructors[0].newInstance(field.get(config), field.get(createDefaultConfig(config)), name);

                } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                    String message = String.format("A config processor must have a valid constructor! Bad processor: %s", processorClass.getName());
                    throw createCrashException(new IllegalArgumentException(message, e), "Illegal config processor");
                }
            }
        }


        return null;
    }

    public static String getFieldName(@NotNull Field field) {
        var annotation = field.getAnnotation(Name.class);
        return annotation == null ? field.getName() : annotation.value();
    }

    @Nullable
    public static String getFieldDescription(@NotNull Field field) {
        var annotation = field.getAnnotation(Description.class);
        return annotation == null ? null : annotation.value();
    }

    @Nullable
    public static CBGuiElement getFieldIcon(@NotNull Field field, Config config, ConfigEntryProcessor<?> processor, Identifier configIdentifier) {
        try {
            Icon iconAnnotation = field.getAnnotation(Icon.class);
            Name nameAnnotation = field.getAnnotation(Name.class);
            Description descriptionAnnotation = field.getAnnotation(Description.class);
            String name = nameAnnotation == null ? field.getName() : nameAnnotation.value();

            CBGuiElementBuilder element;
            if (iconAnnotation != null) {
                try {
                    var value = field.get(config);
                    assert field.getType().isInstance(value);

                    try {
                        iconAnnotation.value().getConstructor();
                    } catch (NoSuchMethodException e) {
                        throw createCrashException(new IllegalArgumentException("An icon supplier must have a constructor without parameters!", e), "Illegal icon supplier");
                    }
                    IconSupplier<?> supplier = iconAnnotation.value().getConstructor().newInstance();

                    element = CBGuiElementBuilder.from(supplier.getIcon(processor == null ? field.get(config) : processor.getValue()));
                } catch (NullPointerException | NoSuchMethodException | InstantiationException |
                         IllegalAccessException | InvocationTargetException e) {
                    throw new UnsupportedOperationException(e);
                }
            } else if (ConfigManager.getManager(configIdentifier) != null && ConfigManager.getManager(configIdentifier).uniqueIcons) {
                element = new CBGuiElementBuilder().setCustomModelData(GuiModels.getOrCreate(getFieldIdentifier(field, configIdentifier), Items.PAPER, GuiModels.SlotType.DEFAULT).value());
            } else element = new CBGuiElementBuilder(Items.PAPER);

            element.setName(Texts.of(name));

            if (descriptionAnnotation != null) {
                element.addLoreLine(Texts.of(descriptionAnnotation.value()));
                element.addLoreLine(Text.empty());
            }

            /*element.addLoreLine(Text.literal(String.format("Field: %s", field.getName())));
            element.addLoreLine(Text.literal(String.format("Field type: %s", field.getType().getName())));
            element.addLoreLine(Text.literal(String.format("Value: %s", field.get(config))));
            element.addLoreLine(Text.literal(String.format("New value: %s", processor == null ? "Missing processor" : processor.getValue())));
            element.addLoreLine(Text.literal(String.format("Default value: %s", field.get(config.getClass().getConstructor().newInstance()))));*/

            element.addLoreLine(Text.literal(String.format("Value: %s", processor == null ? "Missing processor" : processor.getValue())).formatted(Formatting.DARK_GRAY));
            element.addLoreLine(Text.literal(String.format("Current value: %s", field.get(config))).formatted(Formatting.DARK_GRAY));
            element.addLoreLine(Text.literal(String.format("Default value: %s", field.get(config.getClass().getConstructor().newInstance()))).formatted(Formatting.DARK_GRAY));

            element.addLoreLine(Text.empty());

            element.addLoreLine(Text.literal("Left click to edit").formatted(Formatting.GRAY));
            element.addLoreLine(Text.literal("Right click to reset any changes").formatted(Formatting.GRAY));
            element.addLoreLine(Text.literal("Drop to reset to default").formatted(Formatting.GRAY));

            element.addLoreLine(Text.empty());
            element.addLoreLine(Text.literal(getFieldIdentifier(field, configIdentifier).toString()).formatted(Formatting.DARK_GRAY));

            if (processor == null) {
                element.addLoreLine(Text.empty());
                element.addLoreLine(Texts.of("<red>Missing valid config processor!</red>"));
            }

            element.setCallback((index, type, action, gui) -> {
                if (processor == null) {
                    gui.getPlayer().sendMessage(Text.literal("No valid processor found for this option!").formatted(Formatting.RED));
                    return;
                }

                switch (type) {
                    case MOUSE_LEFT -> {
                        gui.getPlayer().playSound(SoundEvents.UI_BUTTON_CLICK, SoundCategory.MASTER, 0.3f, 1);
                        processor.openConfig(gui);
                    }
                    case MOUSE_RIGHT -> {
                        gui.getPlayer().playSound(SoundEvents.ITEM_FIRECHARGE_USE, SoundCategory.MASTER, 0.3f, 1);
                        processor.resetValue();
                    }
                    case DROP -> {
                        gui.getPlayer().playSound(SoundEvents.BLOCK_GLASS_BREAK, SoundCategory.MASTER, 0.3f, 1.3f);
                        processor.resetToDefault();
                    }
                }
            });

            return element.build();
        } catch (IllegalArgumentException | IllegalAccessException | NoSuchMethodException | InstantiationException | InvocationTargetException e) {
            throw new UnsupportedOperationException(e);
        }
    }

    @Contract("_, _ -> new")
    public static @NotNull CrashException createCrashException(Throwable throwable, String message) {
        return new CrashException(CrashReport.create(throwable, message));
    }

    @NotNull
    public static Config createDefaultConfig(@NotNull Config config) {
        try {
            return config.getClass().getConstructor().newInstance();
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException | NoSuchMethodException e) {
            throw createCrashException(e, "Configs must have an empty constructor!");
        }
    }

    public static void fillGui(@NotNull SlotHolder gui, GuiElementInterface item) {
        for (int i = 0; i < gui.getSize(); i++) {
            gui.setSlot(i, item);
        }
    }

    @NotNull
    public static Identifier getFieldIdentifier(Field field, Identifier configIdentifier) {
        return addToPath(configIdentifier, field.getName());
    }

    @NotNull
    @Contract("_, _ -> new")
    public static Identifier addToPath(@NotNull Identifier identifier, String toAdd) {
        return new Identifier(identifier + "/" + toAdd);
    }
}
