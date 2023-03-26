package com.awakenedredstone.cbserverconfig.ui;

import io.wispforest.owo.ui.core.Color;

public class DefaultParsers {
    public static final ParserRegistry.Parser<Color> COLOR = (currentValue) -> currentValue.asHexString(false);
    public static final ParserRegistry.Parser<Color> COLOR_WITH_ALPHA = (currentValue) -> currentValue.asHexString(true);
}
