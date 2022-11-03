package com.awakenedredstone.cbserverconfig.annotation;

import com.awakenedredstone.cbserverconfig.api.config.ConfigProcessor;

public @interface Processor {
    Class<? extends ConfigProcessor<?>> value();
}
