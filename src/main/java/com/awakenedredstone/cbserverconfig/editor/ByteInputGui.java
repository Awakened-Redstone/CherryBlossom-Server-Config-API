package com.awakenedredstone.cbserverconfig.editor;

import com.awakenedredstone.cbserverconfig.polymer.CBGuiElementBuilder;
import com.awakenedredstone.cbserverconfig.ui.Icons;
import eu.pb4.sgui.api.elements.GuiElementBuilder;
import eu.pb4.sgui.api.gui.AnvilInputGui;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

public class ByteInputGui extends AnvilInputGui {
    private final Consumer<Byte> callback;
    private final Function<Byte, Optional<Text>> errorFunction;
    private byte value;

    public ByteInputGui(ServerPlayerEntity player, byte defaultValue, Function<Byte, Optional<Text>> errorFunction, Consumer<Byte> callback) {
        super(player, false);
        this.errorFunction = errorFunction;
        this.value = defaultValue;
        this.callback = callback;
        setDefaultInputValue(String.valueOf(defaultValue));
        this.open();
    }

    @Override
    public void onInput(String input) {
        GuiElementBuilder item;
        try {
            value = Byte.parseByte(input);
            Optional<Text> error = errorFunction.apply(value);
            if (error.isPresent()) {
                if (error.get() instanceof MutableText mutableText) {
                    item = CBGuiElementBuilder.from(Icons.ERROR).setName(Text.literal("Error!").formatted(Formatting.RED, Formatting.UNDERLINE))
                            .addLoreLine(mutableText.formatted(Formatting.RED));
                } else {
                    item = CBGuiElementBuilder.from(Icons.ERROR).setName(Text.literal("Error!").formatted(Formatting.RED, Formatting.UNDERLINE))
                            .setLore(error.get().getWithStyle(Style.EMPTY.withColor(Formatting.RED)));
                }
            } else {
                item = CBGuiElementBuilder.from(Icons.CHECK_GREEN).setName(Text.literal(input))
                        .setCallback((index, type1, action, gui) -> callback.accept(value));
            }
        } catch (NumberFormatException ignored) {
            item = CBGuiElementBuilder.from(Icons.ERROR).setName(Text.literal("Error!").formatted(Formatting.RED, Formatting.UNDERLINE))
                    .addLoreLine(Text.literal("Invalid input!").formatted(Formatting.RED));
        }
        this.setSlot(2, item);
        super.onInput(input);
    }
}
