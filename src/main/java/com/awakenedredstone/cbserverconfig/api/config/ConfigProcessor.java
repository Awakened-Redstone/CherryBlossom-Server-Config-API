package com.awakenedredstone.cbserverconfig.api.config;

public abstract class ConfigProcessor<T> {
    protected T value;

    public ConfigProcessor(T value) {
        this.value = value;
    }

    public abstract void openConfig();

    public T getValue() {
        return value;
    }
}
