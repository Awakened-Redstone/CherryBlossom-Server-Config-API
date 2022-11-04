package com.awakenedredstone.testmod;

import com.awakenedredstone.cbserverconfig.api.IconSupplier;
import com.awakenedredstone.cbserverconfig.api.config.ConfigProcessor;
import com.awakenedredstone.cbserverconfig.polymer.CBGuiElement;
import com.awakenedredstone.cbserverconfig.polymer.CBGuiElementBuilder;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionUtil;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class PotionProcessor extends ConfigProcessor<String> implements IconSupplier<String> {

	public PotionProcessor(String value) {
		super(value);
	}

	@Override
	public void openConfig() {}

	@Override
	public CBGuiElement generateIcon(String value) {
		Potion potion = Registry.POTION.get(new Identifier(value));
		return CBGuiElementBuilder.from(PotionUtil.setPotion(new ItemStack(Items.POTION), potion)).build();
	}
}