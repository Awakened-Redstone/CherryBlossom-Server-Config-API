package com.awakenedredstone.testmod;

import com.awakenedredstone.cbserverconfig.api.config.ConfigEntryProcessor;
import com.awakenedredstone.cbserverconfig.internal.gui.StringInputGui;
import com.awakenedredstone.cbserverconfig.polymer.CBGuiElementBuilder;
import com.awakenedredstone.cbserverconfig.util.Texts;
import eu.pb4.sgui.api.gui.SimpleGui;
import eu.pb4.sgui.api.gui.SimpleGuiBuilder;
import eu.pb4.sgui.api.gui.SlotGuiInterface;
import net.minecraft.item.Items;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.Optional;

public class PotionEntryProcessor extends ConfigEntryProcessor<String> {

	public PotionEntryProcessor(String value, String defaultValue, String entryName) {
		super(value, defaultValue, entryName);
	}

	@Override
	public void openConfig(SlotGuiInterface gui) {
		new PotionInputGui(gui.getPlayer(), value, value -> Optional.empty(), value -> {
			markDirty();
			gui.getPlayer().playSound(SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.MASTER, 0.3f, 1);
			this.value = value;
			gui.close();
			gui.open();
		});
	}
}