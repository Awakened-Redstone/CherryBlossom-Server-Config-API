package com.awakenedredstone.cbserverconfig.internal;

import com.awakenedredstone.cbserverconfig.api.config.ConfigEntryProcessor;
import eu.pb4.sgui.api.gui.SlotGuiInterface;

public class StringEntryProcessor extends ConfigEntryProcessor<String> {

    public StringEntryProcessor(String value, String defaultValue, String entryName) {
        super(value, defaultValue, entryName);
    }

    @Override
    public void openConfig(SlotGuiInterface gui) {

    }
}
