package com.awakenedredstone.cbserverconfig.util;

import eu.pb4.placeholders.api.TextParserUtils;
import net.minecraft.text.Text;
import net.minecraft.util.Language;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class Texts {
    public static Text of(String key, @NotNull Consumer<Map<String, String>> builder) {
        String text = Language.getInstance().get(key);
        Map<String, String> placeholders = new HashMap<>();
        builder.accept(placeholders);

        for (String k : placeholders.keySet()) {
            String v = placeholders.get(k);
            text = text.replaceAll(k, v);
        }
        return TextParserUtils.formatText(text);
    }

    public static Text of(String key) {
        return of(key, (m) -> {/**/});
    }
}
