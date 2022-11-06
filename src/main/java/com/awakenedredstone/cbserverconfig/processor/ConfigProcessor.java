package com.awakenedredstone.cbserverconfig.processor;

import com.awakenedredstone.cbserverconfig.CBServerConfig;
import com.awakenedredstone.cbserverconfig.annotation.Description;
import com.awakenedredstone.cbserverconfig.annotation.Icon;
import com.awakenedredstone.cbserverconfig.annotation.Name;
import com.awakenedredstone.cbserverconfig.api.IconSupplier;
import com.awakenedredstone.cbserverconfig.api.config.Config;
import com.awakenedredstone.cbserverconfig.api.config.ConfigEntryProcessor;
import com.awakenedredstone.cbserverconfig.api.config.ConfigManager;
import com.awakenedredstone.cbserverconfig.exception.IllegalFieldException;
import com.awakenedredstone.cbserverconfig.polymer.CBGuiElement;
import com.awakenedredstone.cbserverconfig.polymer.CBGuiElementBuilder;
import com.awakenedredstone.cbserverconfig.util.Texts;
import com.awakenedredstone.cbserverconfig.util.Utils;
import eu.pb4.sgui.api.elements.GuiElementInterface;
import eu.pb4.sgui.api.gui.SimpleGui;
import eu.pb4.sgui.api.gui.SimpleGuiBuilder;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.crash.CrashException;
import net.minecraft.util.crash.CrashReport;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

public class ConfigProcessor {

    public static void test() {
        ConfigManager.getConfigs().forEach((identifier, configManager) -> {
            try {
                for (Field field : configManager.targetClass.getDeclaredFields()) {
                    if (Modifier.isStatic(field.getModifiers())) {
                        IllegalFieldException exception = new IllegalFieldException(String.format("%s is not allowed to have static fields!", field.getDeclaringClass().getName()));
                        CBServerConfig.LOGGER.error("Illegal field!", exception);
                        continue;
                    }

                    CBServerConfig.LOGGER.info("Field: {}, Value: {}, Default value: {}", field.getName(), field.get(configManager.getConfig()),
                            field.get(configManager.targetClass.getConstructor().newInstance()));
                }
            } catch (IllegalArgumentException | IllegalAccessException | NoSuchMethodException | InstantiationException | InvocationTargetException e) {
                throw new UnsupportedOperationException(e);
            }
        });
    }

    public static List<CBGuiElement> buildFileItems() {
        List<CBGuiElement> elements = new ArrayList<>();
        ConfigManager.getConfigs().forEach((identifier, configManager) -> {
            Class<?> targetClass = configManager.targetClass;
            Icon iconAnnotation = targetClass.getAnnotation(Icon.class);
            Name nameAnnotation = targetClass.getAnnotation(Name.class);
            Description descriptionAnnotation = targetClass.getAnnotation(Description.class);
            String name = nameAnnotation == null ? targetClass.getCanonicalName() : nameAnnotation.value();
            CBGuiElementBuilder element;
            if (iconAnnotation != null) {
                try {
                    element = CBGuiElementBuilder.from(iconAnnotation.value().getConstructor().newInstance().generateIcon(null));
                } catch (NullPointerException | NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
                    throw new UnsupportedOperationException(e);
                }
            } else element = new CBGuiElementBuilder();

            element.setName(Texts.of(name));

            if (descriptionAnnotation != null) {
                element.addLoreLine(Texts.of(descriptionAnnotation.value()));
                element.addLoreLine(Text.empty());
            }
            element.addLoreLine(Text.literal(identifier.toString()).formatted(Formatting.DARK_GRAY));

            element.setCallback((index, type, action, gui) -> {
                List<CBGuiElement> elements2 = ConfigProcessor.buildFields(configManager.getConfig());

                SimpleGuiBuilder guiBuilder = new SimpleGuiBuilder(ScreenHandlerType.GENERIC_9X6, false);
                guiBuilder.setTitle(Texts.of(name));

                CBGuiElementBuilder returnItem = new CBGuiElementBuilder(Items.BARRIER);
                returnItem.setName(Texts.of("<red>Go back</red>"));
                returnItem.setCallback((index1, type1, action1, gui1) -> {
                    gui.close();
                    gui.open();
                });

                guiBuilder.setSlot(53, returnItem.build());

                for (CBGuiElement element2 : elements2) {
                    guiBuilder.addSlot(element2);
                }

                SimpleGui gui2 = guiBuilder.build(gui.getPlayer());

                gui2.open();
            });

            elements.add(element.build());
        });

        return elements;
    }

    public static <T extends Config> List<CBGuiElement> buildFields(T config) {
        List<CBGuiElement> elements = new ArrayList<>();

        try {
            for (Field field : config.getClass().getDeclaredFields()) {
                if (Modifier.isStatic(field.getModifiers())) {
                    IllegalFieldException exception = new IllegalFieldException(String.format("%s is not allowed to have static fields!", field.getDeclaringClass().getName()));
                    CrashReport crash = CrashReport.create(exception, "Illegal static field");
                    throw new CrashException(crash);
                }

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
                            CrashReport crash = CrashReport.create(new IllegalArgumentException("An icon supplier must have a constructor without parameters!", e), "Illegal icon supplier");
                            throw new CrashException(crash);
                        }
                        IconSupplier<?> supplier = iconAnnotation.value().getConstructor().newInstance();

                        element = CBGuiElementBuilder.from(supplier.getIcon(field.get(config)));
                    } catch (NullPointerException | NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
                        throw new UnsupportedOperationException(e);
                    }
                } else element = new CBGuiElementBuilder();

                element.setName(Texts.of(name));

                if (descriptionAnnotation != null) {
                    element.addLoreLine(Texts.of(descriptionAnnotation.value()));
                    element.addLoreLine(Text.empty());
                }

                element.addLoreLine(Text.literal(String.format("Field: %s", field.getName())));
                element.addLoreLine(Text.literal(String.format("Value: %s", field.get(config))));
                element.addLoreLine(Text.literal(String.format("Default value: %s", field.get(config.getClass().getConstructor().newInstance()))));

                ConfigEntryProcessor<?> processor = Utils.getProcessor(field, config);

                if (processor == null) {
                    element.addLoreLine(Text.empty());
                    element.addLoreLine(Texts.of("<red>Missing valid config processor!</red>"));
                }

                element.setCallback((index, type, action, gui) -> {
                    if (processor == null) {
                        gui.getPlayer().sendMessage(Text.literal("No valid processor found for this option!").formatted(Formatting.RED));
                        return;
                    }

                    processor.openConfig(gui);
                });

                elements.add(element.build());
            }
        } catch (IllegalArgumentException | IllegalAccessException | NoSuchMethodException | InstantiationException | InvocationTargetException e) {
            throw new UnsupportedOperationException(e);
        }

        return elements;
    }

    private static <T> T test(Class<T> clazz, Object value) {
        return clazz.cast(value);
    }
}
