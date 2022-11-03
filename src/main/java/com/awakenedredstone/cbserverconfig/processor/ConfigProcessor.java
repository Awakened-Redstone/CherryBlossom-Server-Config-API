package com.awakenedredstone.cbserverconfig.processor;

import com.awakenedredstone.cbserverconfig.CBServerConfig;
import com.awakenedredstone.cbserverconfig.annotation.Description;
import com.awakenedredstone.cbserverconfig.annotation.Icon;
import com.awakenedredstone.cbserverconfig.annotation.Name;
import com.awakenedredstone.cbserverconfig.api.IconSupplier;
import com.awakenedredstone.cbserverconfig.api.config.ConfigManager;
import com.awakenedredstone.cbserverconfig.exception.IllegalFieldException;
import com.awakenedredstone.cbserverconfig.polymer.CBGuiElement;
import com.awakenedredstone.cbserverconfig.polymer.CBGuiElementBuilder;
import com.awakenedredstone.cbserverconfig.util.Texts;
import eu.pb4.sgui.api.gui.SimpleGui;
import eu.pb4.sgui.api.gui.SimpleGuiBuilder;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ConfigProcessor {

    public static void test() {
        ConfigManager.getConfigs().forEach((identifier, configManager) -> {
            try {
                for (Field field : configManager.targetClass.getDeclaredFields()) {
                    if (Modifier.isStatic(field.getModifiers())) {
                        throw new IllegalFieldException(String.format("%s is not allowed to have static fields!", field.getDeclaringClass().getName()));
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
                    element = CBGuiElementBuilder.from(iconAnnotation.value().getConstructor().newInstance().getIcon(null));
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

                for (CBGuiElement element2 : elements) {
                    guiBuilder.addSlot(element);
                }

                SimpleGui gui2 = guiBuilder.build(gui.getPlayer());

                gui2.open();
            });

            elements.add(element.build());
        });

        return elements;
    }

    public static <T> List<CBGuiElement> buildFields(T config) {
        List<CBGuiElement> elements = new ArrayList<>();

        try {
            for (Field field : config.getClass().getDeclaredFields()) {
                if (Modifier.isStatic(field.getModifiers())) {
                    throw new IllegalFieldException(String.format("%s is not allowed to have static fields!", field.getDeclaringClass().getName()));
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

                        Object cast = field.getType().cast(value);
                        IconSupplier<?> supplier = iconAnnotation.value().getConstructor().newInstance();

                        Method method = iconAnnotation.value().getMethod("getIcon", field.getType());
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

                element.setCallback((index, type, action, gui) -> {
                    gui.getPlayer().sendMessage(Text.literal("Boop!"));
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
