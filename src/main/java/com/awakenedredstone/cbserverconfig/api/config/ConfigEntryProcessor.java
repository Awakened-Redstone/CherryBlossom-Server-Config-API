package com.awakenedredstone.cbserverconfig.api.config;

import eu.pb4.sgui.api.gui.SlotGuiInterface;

public abstract class ConfigEntryProcessor<T> {
    private boolean dirty;
    protected final String entryName;
    protected final T currentValue;
    protected final T defaultValue;
    protected T value;

    public ConfigEntryProcessor(T value, T defaultValue, String entryName) {
        this.defaultValue = defaultValue;
        this.entryName = entryName;
        this.currentValue = value;
        this.value = value;
    }

    public abstract void openConfig(SlotGuiInterface gui);

    public void resetValue() {
        markDirty();
        this.value = currentValue;
    }

    public void resetToDefault() {
        markDirty();
        this.value = defaultValue;
    }

    public T getValue() {
        return value;
    }

    public T getCurrentValue() {
        return currentValue;
    }

    public T getDefaultValue() {
        return defaultValue;
    }

    public String getEntryName() {
        return entryName;
    }

    protected void markDirty() {
        this.dirty = true;
    }

    public boolean isDirty() {
        if (value != currentValue) markDirty();
        return dirty;
    }
}
