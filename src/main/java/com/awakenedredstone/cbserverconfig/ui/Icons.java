package com.awakenedredstone.cbserverconfig.ui;

import com.awakenedredstone.cbserverconfig.polymer.CBGuiElement;
import com.awakenedredstone.cbserverconfig.polymer.CBGuiElementBuilder;
import net.minecraft.item.Items;
import net.minecraft.text.Text;

public class Icons {

    public static final CBGuiElement FILLER = new CBGuiElementBuilder(Items.BLACK_STAINED_GLASS_PANE).setName(Text.empty()).build();

    public static final CBGuiElement TOGGLE = new CBGuiElementBuilder(Items.YELLOW_STAINED_GLASS_PANE).setName(Text.literal("Toggle")).build();

    public static final CBGuiElement INCREASE = new CBGuiElementBuilder(Items.LIME_STAINED_GLASS_PANE).setName(Text.literal("Increase")).build();

    public static final CBGuiElement DECREASE = new CBGuiElementBuilder(Items.RED_STAINED_GLASS_PANE).setName(Text.literal("Decrease")).build();

    public static final CBGuiElement CHECK_GREEN = new CBGuiElementBuilder(Items.GOLD_NUGGET).build();

    public static final CBGuiElement ERROR = new CBGuiElementBuilder(Items.BARRIER).build();

    public static final CBGuiElement UNKNOWN = new CBGuiElementBuilder(Items.GOLD_NUGGET).build();

    public static final CBGuiElement COLOR = new CBGuiElementBuilder(Items.LEATHER_CHESTPLATE).build();

    public static final CBGuiElement EDIT = new CBGuiElementBuilder(Items.WRITABLE_BOOK).build();

    public static final CBGuiElement ARROW_UP = new CBGuiElementBuilder(Items.LIME_STAINED_GLASS_PANE).build();

    public static final CBGuiElement ARROW_DOWN = new CBGuiElementBuilder(Items.RED_STAINED_GLASS_PANE).build();
}
