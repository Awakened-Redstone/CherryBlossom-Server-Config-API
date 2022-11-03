package com.awakenedredstone.cbserverconfig.annotation;

import com.awakenedredstone.cbserverconfig.api.IconSupplier;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.TYPE})
public @interface Icon {
    Class<? extends IconSupplier<?>> value();
}
