package com.awakenedredstone.cbserverconfig.ui;

import com.awakenedredstone.cbserverconfig.polymer.CBGuiElement;
import com.awakenedredstone.cbserverconfig.polymer.CBGuiElementBuilder;
import com.awakenedredstone.cbserverconfig.util.MapBuilder;
import com.awakenedredstone.cbserverconfig.util.Texts;
import com.awakenedredstone.cbserverconfig.util.Utils;
import eu.pb4.sgui.api.ClickType;
import eu.pb4.sgui.api.gui.GuiInterface;
import eu.pb4.sgui.api.gui.SimpleGui;
import io.wispforest.owo.config.ConfigWrapper;
import io.wispforest.owo.config.Option;
import io.wispforest.owo.config.annotation.ExcludeFromScreen;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.math.MathHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class ConfigScreen extends SimpleGui {
    private final CBGuiElement EMPTY = CBGuiElementBuilder.from(ItemStack.EMPTY).build();
    private final CBGuiElement FILLER = new CBGuiElementBuilder(Items.BLACK_STAINED_GLASS_PANE).setName(Text.empty()).build();
    private final CBGuiElement NEXT_PAGE = new CBGuiElementBuilder(Items.LIME_STAINED_GLASS_PANE).setName(Texts.of("Next page")).setCallback((index, type, action, gui) -> offsetPage(1)).build();
    private final CBGuiElement PREV_PAGE = new CBGuiElementBuilder(Items.RED_STAINED_GLASS_PANE).setName(Texts.of("Previous page")).setCallback((index, type, action, gui) -> offsetPage(-1)).build();

    protected final GuiInterface parent;
    protected final ConfigWrapper<?> config;
    @SuppressWarnings("rawtypes")
    private final Map<Option, Object> values = new LinkedHashMap<>();
    @SuppressWarnings("rawtypes")
    private final Predicate<Option> filterPredicate;

    private int page = 0;
    private final int maxPage;

    @SuppressWarnings("rawtypes")
    public ConfigScreen(ServerPlayerEntity player, @NotNull ConfigWrapper<?> config, @Nullable GuiInterface parent, @Nullable Predicate<Option> filterPredicate) {
        super(ScreenHandlerType.GENERIC_9X4, player, false);
        this.parent = parent;
        this.config = config;
        this.filterPredicate = filterPredicate;
        LinkedHashMap<? extends Option<?>, ?> collect = config.allOptions().values().stream()
                .filter(this::isVisible)
                .collect(Collectors.toMap(option -> option, Option::value, (o, o2) -> {
                    throw new IllegalArgumentException();
                }, LinkedHashMap::new));
        values.putAll(collect);
        maxPage = (int) Math.floor(values.size() / 7f);
        build();
        open();
    }

    private void build() {
        this.setTitle(Texts.of("text.config." + this.config.name() + ".title"));
        if (page < maxPage) this.setSlot(this.getSize() - 8, NEXT_PAGE);
        if (page > 0) this.setSlot(this.getSize() - 9, PREV_PAGE);

        CBGuiElementBuilder search = new CBGuiElementBuilder(Items.SPYGLASS)
                .setName(Texts.of("text.owo.config.search"))
                .addLoreLine(Text.empty())
                .addLoreLine(Texts.of("text.cbsc.config.search.tooltip"))
                .addLoreLine(Texts.of("<red>Not yet implemented"))
                .setCallback((index, type, action, gui) -> {
                    if (type != ClickType.MOUSE_LEFT) return;
                    this.player.playSound(SoundEvents.ITEM_SPYGLASS_USE, SoundCategory.MASTER, 1f, 1);

                    this.player.sendMessage(Texts.of("<red>Not yet implemented"));
                });
        this.setSlot(this.getSize() - 5, search);

        CBGuiElementBuilder sections = new CBGuiElementBuilder(Items.ENDER_PEARL)
                .setName(Texts.of("text.owo.config.sections"))
                .addLoreLine(Text.empty())
                .addLoreLine(Texts.of("text.cbsc.config.sections.tooltip"))
                .addLoreLine(Texts.of("<red>Not yet implemented"))
                .setCallback((index, type, action, gui) -> {
                    if (type != ClickType.MOUSE_LEFT) return;
                    this.player.playSound(SoundEvents.ENTITY_FOX_TELEPORT, SoundCategory.MASTER, 0.5f, 1);

                    this.player.sendMessage(Texts.of("<red>Not yet implemented"));
                });
        this.setSlot(this.getSize() - 4, sections);

        CBGuiElementBuilder reload = new CBGuiElementBuilder(Items.WRITABLE_BOOK)
                .setName(Texts.of("text.owo.config.button.reload"))
                .addLoreLine(Text.empty())
                .addLoreLine(Texts.of("text.cbsc.config.button.reload.tooltip"))
                .setCallback((index, type, action, gui) -> {
                    if (type != ClickType.MOUSE_LEFT_SHIFT) return;
                    this.player.playSound(SoundEvents.ITEM_BOOK_PAGE_TURN, SoundCategory.MASTER, 1f, 1);
                    this.config.load();

                    values.clear();
                    LinkedHashMap<? extends Option<?>, ?> collect = config.allOptions().values().stream()
                            .filter(option -> !option.backingField().hasAnnotation(ExcludeFromScreen.class))
                            .collect(Collectors.toMap(option -> option, Option::value, (o, o2) -> {
                                throw new IllegalArgumentException();
                            }, LinkedHashMap::new));
                    values.putAll(collect);

                    update();
                });
        this.setSlot(this.getSize() - 3, reload);

        CBGuiElementBuilder discardChanges = new CBGuiElementBuilder(Items.BARRIER)
                .setName(Texts.of("text.cbsc.config.discard"))
                .addLoreLine(Text.empty())
                .addLoreLine(Texts.of("text.cbsc.config.discard.tooltip"))
                .setCallback((index, type, action, gui) -> {
                    if (type != ClickType.MOUSE_RIGHT_SHIFT) return;
                    this.player.playSound(SoundEvents.UI_BUTTON_CLICK.value(), SoundCategory.MASTER, 0.3f, 1);
                    if (parent != null) {
                        parent.open();
                    }
                    this.close();
                });
        this.setSlot(this.getSize() - 2, discardChanges);

        CBGuiElementBuilder saveChanges = new CBGuiElementBuilder(Items.EMERALD)
                .setName(Texts.of("text.cbsc.config.save"))
                .addLoreLine(Text.empty())
                .addLoreLine(Texts.of("text.cbsc.config.save.tooltip"))
                .setCallback((index, type, action, gui) -> {
                    if (type != ClickType.MOUSE_LEFT) return;
                    this.player.playSound(SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.MASTER, 0.3f, 1);
                    //noinspection unchecked
                    values.forEach(Option::set);
                    config.save();
                    if (parent != null) {
                        parent.open();
                    }
                    this.close();
                });
        this.setSlot(this.getSize() - 1, saveChanges);

        update();
    }

    @Override
    public boolean open() {
        update();
        return super.open();
    }

    private void update() {
        this.setSlot(this.getSize() - 8, page < maxPage ? NEXT_PAGE : EMPTY);
        this.setSlot(this.getSize() - 9, page > 0 ? PREV_PAGE : EMPTY);

        var a = new Object() {
            int slot = 10;
        };

        values.entrySet().stream()
                .filter(entry -> this.filterPredicate == null || filterPredicate.test(entry.getKey()))
                .skip(page * 7L)
                .limit(7)
                .forEachOrdered(optionEntry -> setConfigItem(a.slot++, optionEntry));

        if (a.slot < 18) {
            for (int i = a.slot; i < 18; i++) {
                this.setSlot(i, EMPTY);
            }
        }
        Utils.quickFillGui(this, FILLER);
    }

    private void quickUpdate(@Nullable Option<?> forceOption) {
        var a = new Object() {
            int slot = 10;
        };
        values.entrySet().stream()
                .filter(entry -> this.filterPredicate == null || filterPredicate.test(entry.getKey()))
                .skip(page * 7L)
                .limit(7)
                .forEachOrdered(optionEntry -> {
                    Option<?> option = optionEntry.getKey();
                    if (option.value() == values.get(option) && !option.equals(forceOption)) {
                        a.slot++;
                        return;
                    }
                    setConfigItem(a.slot++, optionEntry);
                });
    }

    @SuppressWarnings("rawtypes")
    private void setConfigItem(int slot, @NotNull Map.Entry<Option, ?> entry) {
        Option<?> option = entry.getKey();
        var value = entry.getValue();
        @Nullable EditorRegistry.EditCallback<?> editor = EditorRegistry.getEditor(option.clazz(), option.key());
        @NotNull ParserRegistry.Parser<?> parser = ParserRegistry.getParser(option.clazz(), option.key());
        CBGuiElementBuilder builder = new CBGuiElementBuilder();
        builder.setItem(Items.PAPER);
        builder.setName(Texts.of(option.translationKey()));
        builder.addLoreLine(Texts.of("text.cbsc.config.value", new MapBuilder.StringMap().putAny("%value%", getString(parser, value)).build()));
        builder.addLoreLine(Texts.of("text.cbsc.config.current_value", new MapBuilder.StringMap().putAny("%value%", getString(parser, option.value())).build()));
        builder.addLoreLine(Texts.of("text.cbsc.config.default_value", new MapBuilder.StringMap().putAny("%value%", getString(parser, option.defaultValue())).build()));
        builder.addLoreLine(Text.empty());
        builder.addLoreLine(Texts.of("text.cbsc.tip.edit"));
        builder.addLoreLine(Texts.of("text.cbsc.tip.reset"));
        builder.addLoreLine(Texts.of("text.cbsc.tip.default"));
        if (option.syncMode() == Option.SyncMode.OVERRIDE_CLIENT) {
            builder.addLoreLine(Text.empty());
            builder.addLoreLine(Texts.of("text.cbsc.config.client_override_unsupported"));
        } else {
            if (editor == null) {
                builder.addLoreLine(Text.empty());
                builder.addLoreLine(Texts.of("text.cbsc.config.missing_editor", new MapBuilder.StringMap().putAny("%class%", option.clazz().getSimpleName()).build()));
            }
            builder.setCallback((index, type, action) -> {
                switch (type) {
                    case MOUSE_LEFT -> {
                        if (editor == null) return;
                        this.getPlayer().playSound(SoundEvents.UI_BUTTON_CLICK.value(), SoundCategory.MASTER, 0.3f, 1);
                        EditorRegistry.trigger(editor, value, type, action, this).whenComplete((o, throwable) -> values.put(option, o));
                        quickUpdate(option);
                    }
                    case MOUSE_RIGHT -> {
                        if (values.get(option) == option.value()) break;
                        this.player.playSound(SoundEvents.ENTITY_BLAZE_SHOOT, SoundCategory.MASTER, 0.3f, 1);
                        values.put(option, option.value());
                        quickUpdate(option);
                    }
                    case DROP -> {
                        if (values.get(option) == option.defaultValue()) break;
                        this.player.playSound(SoundEvents.BLOCK_GLASS_BREAK, SoundCategory.MASTER, 0.3f, 1);
                        values.put(option, option.defaultValue());
                        quickUpdate(option);
                    }
                    default -> {
                        return;
                    }
                }
                quickUpdate(null);
            });
        }
        this.setSlot(slot, builder);
    }

    public void offsetPage(int offset) {
        this.player.playSound(SoundEvents.UI_BUTTON_CLICK.value(), SoundCategory.MASTER, 0.3f, 1);
        this.page = (int) MathHelper.clamp(this.page + offset, 0, Math.floor(config.allOptions().values().size() / 7f));
        update();
    }

    private boolean isVisible(@NotNull Option<?> option) {
        if (option.backingField().hasAnnotation(ExcludeFromScreen.class)) return false;
        var parentKey = option.key().parent();
        return parentKey.isRoot() || !Objects.requireNonNull(this.config.fieldForKey(parentKey)).isAnnotationPresent(ExcludeFromScreen.class);
    }

    public String getString(ParserRegistry.Parser<?> parser, Object value) {
        return ParserRegistry.parse(parser, value);
    }
}
