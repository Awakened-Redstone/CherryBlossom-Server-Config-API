package com.awakenedredstone.cbserverconfig.api;

import com.awakenedredstone.cbserverconfig.polymer.CBGuiElement;

@FunctionalInterface
public interface IconSupplier<T> {
    CBGuiElement generateIcon(T value);

    default CBGuiElement getIcon(Object value) {
        return generateIcon((T) value);
    }
}
