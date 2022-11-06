package com.awakenedredstone.cbserverconfig.annotation;

import com.awakenedredstone.cbserverconfig.api.config.ConfigEntryProcessor;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface Processor {
    Class<? extends ConfigEntryProcessor<?>> value();
}
