package com.awakenedredstone.cbserverconfig.api;

import com.awakenedredstone.cbserverconfig.polymer.CBGuiElement;

@FunctionalInterface
public interface IconSupplier<T> {
    CBGuiElement getIcon(T value);
}
