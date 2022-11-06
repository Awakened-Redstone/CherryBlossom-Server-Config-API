package com.awakenedredstone.cbserverconfig.internal;

import com.awakenedredstone.cbserverconfig.api.config.ConfigEntryProcessor;
import eu.pb4.sgui.api.gui.SlotGuiInterface;

public class BooleanEntryProcessor extends ConfigEntryProcessor<Boolean> {

    public BooleanEntryProcessor(Boolean value, Boolean defaultValue, String entryName) {
        super(value, defaultValue, entryName);
    }

    @Override
    public void openConfig(SlotGuiInterface gui) {
        markDirty();
        this.value = !value;
    }
}
