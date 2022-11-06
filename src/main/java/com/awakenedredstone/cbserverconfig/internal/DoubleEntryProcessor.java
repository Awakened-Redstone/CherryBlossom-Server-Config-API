package com.awakenedredstone.cbserverconfig.internal;

import com.awakenedredstone.cbserverconfig.api.config.ConfigEntryProcessor;
import com.awakenedredstone.cbserverconfig.internal.gui.DoubleInputGui;
import eu.pb4.sgui.api.gui.SlotGuiInterface;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;

import java.util.Optional;

public class DoubleEntryProcessor extends ConfigEntryProcessor<Double> {

    public DoubleEntryProcessor(Double value, Double defaultValue, String entryName) {
        super(value, defaultValue, entryName);
    }

    @Override
    public void openConfig(SlotGuiInterface gui) {
        new DoubleInputGui(gui.getPlayer(), value, value -> Optional.empty(), value -> {
            markDirty();
            gui.getPlayer().playSound(SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.MASTER, 0.3f, 1);
            this.value = value;
            gui.close();
            gui.open();
        });
    }
}
