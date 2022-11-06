package com.awakenedredstone.testmod;

import com.awakenedredstone.cbserverconfig.api.IconSupplier;
import com.awakenedredstone.cbserverconfig.polymer.CBGuiElement;
import com.awakenedredstone.cbserverconfig.polymer.CBGuiElementBuilder;
import net.minecraft.item.Items;

public class Icons {
    public static class String implements IconSupplier<Object> {
        public CBGuiElement generateIcon(Object value) {
            return new CBGuiElementBuilder(Items.STRING).build();
        }
    }

    public static class Potato implements IconSupplier<Object> {
        public CBGuiElement generateIcon(Object value) {
            return new CBGuiElementBuilder(Items.POTATO).build();
        }
    }

    public static class GlowInkSac implements IconSupplier<Object> {
        public CBGuiElement generateIcon(Object value) {
            return new CBGuiElementBuilder(Items.GLOW_INK_SAC).build();
        }
    }
}
