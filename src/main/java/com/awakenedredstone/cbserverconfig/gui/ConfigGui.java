package com.awakenedredstone.cbserverconfig.gui;

import com.awakenedredstone.cbserverconfig.CBServerConfig;
import com.awakenedredstone.cbserverconfig.api.config.ConfigData;
import com.awakenedredstone.cbserverconfig.api.config.ConfigEntry;
import com.awakenedredstone.cbserverconfig.api.config.ConfigEntryProcessor;
import com.awakenedredstone.cbserverconfig.api.config.ConfigManager;
import com.awakenedredstone.cbserverconfig.polymer.CBGuiElement;
import com.awakenedredstone.cbserverconfig.polymer.CBGuiElementBuilder;
import com.awakenedredstone.cbserverconfig.polymer.CBSimpleGuiBuilder;
import com.awakenedredstone.cbserverconfig.util.Texts;
import com.awakenedredstone.cbserverconfig.util.Utils;
import eu.pb4.sgui.api.gui.GuiInterface;
import eu.pb4.sgui.api.gui.SimpleGui;
import eu.pb4.sgui.api.gui.SlotGuiInterface;
import net.minecraft.item.Items;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

public class ConfigGui {
    private final List<ConfigEntry> entries = new ArrayList<>();
    private final ConfigData configData;
    private final GuiInterface parent;
    private int page = 0;
    private int maxPage = 0;
    private final Consumer<SlotGuiInterface> updateGui;
    private final Consumer<SlotGuiInterface> simpleUpdateGui;
    private final CBGuiElement filler = new CBGuiElementBuilder(Items.BLACK_STAINED_GLASS_PANE).setName(Text.empty()).build();
    private final CBGuiElement nextPage = CBGuiElementBuilder.from(Icons.INCREASE).setName(Texts.of("Next page")).setCallback((index, type, action, gui) -> offsetPage(1, gui)).build();
    private final CBGuiElement prevPage = CBGuiElementBuilder.from(Icons.DECREASE).setName(Texts.of("Previous page")).setCallback((index, type, action, gui) -> offsetPage(-1, gui)).build();

    public ConfigGui(ConfigData configData, @Nullable GuiInterface parent) {
        this.configData = configData;
        this.parent = parent;
        updateGui = gui -> {
            Utils.fillGui(gui, filler);

            String title = maxPage > 0 ? configData.name() + "<reset> (" + page + ")" : configData.name();
            gui.setTitle(Texts.of(title));

            int slot = 10;
            int offset = page * 7;
            for (int i = offset; i < Math.min(offset + 7, entries.size()); i++) {
                ConfigEntry pageEntry = entries.get(i);
                gui.setSlot(slot++, pageEntry.getIcon());
            }

            if (page < maxPage) gui.setSlot(gui.getSize() - 8, nextPage);
            if (page > 0) gui.setSlot(gui.getSize() - 9, prevPage);

            CBGuiElementBuilder discardChanges = new CBGuiElementBuilder(Items.BARRIER).setName(Texts.of("<red>Discard changes")).setCallback((index, type, action, gui1) -> {
                gui.getPlayer().playSound(SoundEvents.UI_BUTTON_CLICK, SoundCategory.MASTER, 0.3f, 1);
                if (parent != null) {
                    parent.close();
                    parent.open();
                } else gui.close();
            });
            gui.setSlot(gui.getSize() - 2, discardChanges);

            CBGuiElementBuilder saveChanges = new CBGuiElementBuilder(Items.EMERALD).setName(Texts.of("<green>Save changes")).setCallback((index, type, action, gui1) -> {
                gui.getPlayer().playSound(SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.MASTER, 0.3f, 1);
                for (ConfigEntry entry : entries) {
                    try {
                        if (entry.processor() == null) {
                            gui.getPlayer().sendMessage(Texts.of("<red>Skipping \"" + entry.identifier() + "\" due to missing processor!</red>"));
                            continue;
                        }
                        entry.field().set(entry.manager().getConfig(), entry.processor().getValue());
                        ConfigManager.getManager(configData.identifier()).save();
                    } catch (IllegalAccessException e) {
                        gui.getPlayer().sendMessage(Texts.of("<red>Failed to save data! Check the logs for more info.</red>"));
                        CBServerConfig.LOGGER.error("Failed to save data!", e);
                        return;
                    }
                }
                if (parent != null) {
                    parent.close();
                    parent.open();
                } else gui.close();
            });
            gui.setSlot(gui.getSize() - 1, saveChanges);
        };

        simpleUpdateGui = gui -> {
            int slot = 10;
            int offset = page * 7;
            for (int i = offset; i < Math.min(offset + 7, entries.size()); i++) {
                ConfigEntry pageEntry = entries.get(i);
                if (pageEntry.processor().isDirty()) gui.setSlot(slot, pageEntry.getIcon());
                slot++;
            }
        };
    }

    public SimpleGui buildGui(ServerPlayerEntity player) {
        fillEntryList();

        CBSimpleGuiBuilder builder = new CBSimpleGuiBuilder(ScreenHandlerType.GENERIC_9X4, false);
        Utils.fillGui(builder, filler);

        builder.setOnOpen(updateGui::accept);
        builder.setOnClick(simpleUpdateGui::accept);

        return builder.build(player);
    }

    public void openGui(ServerPlayerEntity player) {
        buildGui(player).open();
    }

    private void fillEntryList() {
        entries.clear();
        for (Field field : configData.manager().getConfig().getClass().getDeclaredFields()) {
            Identifier identifier = Utils.getFieldIdentifier(field, configData.identifier());
            String name = Utils.getFieldName(field);
            String description = Utils.getFieldDescription(field);
            ConfigEntryProcessor<?> processor = Utils.getProcessor(field, configData.manager().getConfig());
            entries.add(new ConfigEntry(identifier, name, description, processor, field, configData.manager()));
        }

        maxPage = (int) Math.floor(entries.size() / 7f);
    }

    public void offsetPage(int offset, SlotGuiInterface gui) {
        gui.getPlayer().playSound(SoundEvents.UI_BUTTON_CLICK, SoundCategory.MASTER, 0.3f, 1);
        this.page = (int) MathHelper.clamp(this.page + offset, 0, Math.floor(entries.size() / 7f));
        updateGui.accept(gui);
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPage() {
        return page;
    }

    public List<ConfigEntry> getEntries() {
        return Collections.unmodifiableList(entries);
    }

    @Nullable
    public ConfigEntry getEntry(Identifier identifier) {
        return entries.stream().filter(entry -> entry.identifier().equals(identifier)).findFirst().orElse(null);
    }

    public ConfigData getConfigData() {
        return configData;
    }
}
