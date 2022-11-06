package com.awakenedredstone.cbserverconfig.gui;

import com.awakenedredstone.cbserverconfig.CBServerConfig;
import com.awakenedredstone.cbserverconfig.polymer.CBGuiElement;
import com.awakenedredstone.cbserverconfig.polymer.CBGuiElementBuilder;
import com.awakenedredstone.cbserverconfig.polymer.GuiModels;
import eu.pb4.sgui.api.elements.GuiElementBuilder;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class Icons {

    public static final CBGuiElement FILLER = new CBGuiElementBuilder(Items.BLACK_STAINED_GLASS_PANE).setName(Text.empty())
            .setCustomModelData(GuiModels.getOrCreate(new Identifier(CBServerConfig.MOD_ID, "gui/filler"),
                    Items.BLACK_STAINED_GLASS_PANE, GuiModels.SlotType.FULL_SLOT).value()).build();

    public static final CBGuiElement TOGGLE = new CBGuiElementBuilder(Items.YELLOW_STAINED_GLASS_PANE).setName(Text.literal("Toggle"))
            .setCustomModelData(GuiModels.getOrCreate(new Identifier(CBServerConfig.MOD_ID, "gui/toggle"),
                    Items.YELLOW_STAINED_GLASS_PANE, GuiModels.SlotType.FULL_SLOT).value()).build();

    public static final CBGuiElement INCREASE = new CBGuiElementBuilder(Items.LIME_STAINED_GLASS_PANE).setName(Text.literal("Increase"))
            .setCustomModelData(GuiModels.getOrCreate(new Identifier(CBServerConfig.MOD_ID, "gui/add"),
                    Items.LIME_STAINED_GLASS_PANE, GuiModels.SlotType.FULL_SLOT).value()).build();

    public static final CBGuiElement DECREASE = new CBGuiElementBuilder(Items.RED_STAINED_GLASS_PANE).setName(Text.literal("Decrease"))
            .setCustomModelData(GuiModels.getOrCreate(new Identifier(CBServerConfig.MOD_ID, "gui/remove"),
                    Items.RED_STAINED_GLASS_PANE, GuiModels.SlotType.FULL_SLOT).value()).build();

    public static final CBGuiElement CHECK_GREEN = new CBGuiElementBuilder(Items.GOLD_NUGGET)
            .setCustomModelData(GuiModels.getOrCreate(new Identifier(CBServerConfig.MOD_ID, "gui/check_green"),
                    Items.GOLD_NUGGET, GuiModels.SlotType.DEFAULT).value()).build();

    public static final CBGuiElement ERROR = new CBGuiElementBuilder(Items.BARRIER)
            .setCustomModelData(GuiModels.getOrCreate(new Identifier(CBServerConfig.MOD_ID, "gui/error"),
                    Items.BARRIER, GuiModels.SlotType.DEFAULT).value()).build();

    public static final CBGuiElement UNKNOWN = new CBGuiElementBuilder(Items.GOLD_NUGGET)
            .setCustomModelData(GuiModels.getOrCreate(new Identifier(CBServerConfig.MOD_ID, "gui/unknown"),
                    Items.GOLD_NUGGET, GuiModels.SlotType.DEFAULT).value()).build();

    public static final CBGuiElement COLOR = new CBGuiElementBuilder(Items.LEATHER_CHESTPLATE)
            .setCustomModelData(GuiModels.getOrCreate(new Identifier(CBServerConfig.MOD_ID, "gui/color"),
                    Items.LEATHER_CHESTPLATE, GuiModels.SlotType.DEFAULT).value()).build();

    public static final CBGuiElement EDIT = new CBGuiElementBuilder(Items.WRITABLE_BOOK)
            .setCustomModelData(GuiModels.getOrCreate(new Identifier(CBServerConfig.MOD_ID, "gui/edit"),
                    Items.WRITABLE_BOOK, GuiModels.SlotType.FULL_SLOT).value()).build();

    public static final CBGuiElement ARROW_UP = new CBGuiElementBuilder(Items.LIME_STAINED_GLASS_PANE)
            .setCustomModelData(GuiModels.getOrCreate(new Identifier(CBServerConfig.MOD_ID, "gui/arrow_up"),
                    Items.LIME_STAINED_GLASS_PANE, GuiModels.SlotType.FULL_SLOT).value()).build();

    public static final CBGuiElement ARROW_DOWN = new CBGuiElementBuilder(Items.RED_STAINED_GLASS_PANE)
            .setCustomModelData(GuiModels.getOrCreate(new Identifier(CBServerConfig.MOD_ID, "gui/arrow_down"),
                    Items.RED_STAINED_GLASS_PANE, GuiModels.SlotType.FULL_SLOT).value()).build();
}
