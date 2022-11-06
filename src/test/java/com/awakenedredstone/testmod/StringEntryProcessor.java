package com.awakenedredstone.testmod;

import com.awakenedredstone.cbserverconfig.api.config.ConfigEntryProcessor;
import com.awakenedredstone.cbserverconfig.polymer.CBGuiElementBuilder;
import com.awakenedredstone.cbserverconfig.util.Texts;
import eu.pb4.sgui.api.gui.SimpleGui;
import eu.pb4.sgui.api.gui.SimpleGuiBuilder;
import eu.pb4.sgui.api.gui.SlotGuiInterface;
import net.minecraft.item.Items;
import net.minecraft.screen.ScreenHandlerType;

public class StringEntryProcessor extends ConfigEntryProcessor<String> {

	public StringEntryProcessor(String value, String defaultValue, String entryName) {
		super(value, defaultValue, entryName);
	}

	@Override
	public void openConfig(SlotGuiInterface gui) {
		SimpleGuiBuilder guiBuilder = new SimpleGuiBuilder(ScreenHandlerType.GENERIC_9X6, false);
		guiBuilder.setTitle(Texts.of(entryName));

		CBGuiElementBuilder returnItem = new CBGuiElementBuilder(Items.BARRIER);
		returnItem.setName(Texts.of("<red>Go back</red>"));
		returnItem.setCallback((index1, type1, action1, gui1) -> {
			gui.close();
			gui.open();
		});

		guiBuilder.setSlot(53, returnItem.build());

		SimpleGui gui1 = guiBuilder.build(gui.getPlayer());
		gui1.open();
	}
}