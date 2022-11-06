package com.awakenedredstone.cbserverconfig.internal;

import com.awakenedredstone.cbserverconfig.api.config.ConfigEntryProcessor;
import eu.pb4.sgui.api.gui.SlotGuiInterface;

public class IntegerEntryProcessor extends ConfigEntryProcessor<Integer> {

    public IntegerEntryProcessor(Integer value, Integer defaultValue, String entryName) {
        super(value, defaultValue, entryName);
    }

    @Override
    public void openConfig(SlotGuiInterface gui) {

    }
}
