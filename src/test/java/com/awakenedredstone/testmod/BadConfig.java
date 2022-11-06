package com.awakenedredstone.testmod;

import com.awakenedredstone.cbserverconfig.annotation.Description;
import com.awakenedredstone.cbserverconfig.annotation.Icon;
import com.awakenedredstone.cbserverconfig.annotation.Name;
import com.awakenedredstone.cbserverconfig.annotation.Processor;

@Name("A <red>bad</red> test config!")
@Icon(Icons.GlowInkSac.class)
@Description("An example of a bad config class")
public class BadConfig implements com.awakenedredstone.cbserverconfig.api.config.Config {
    @Description("An example of a <red>primary</red> on the config")
    public int integer = 0;
    @Icon(Icons.String.class)
    public String string = "";
    public String color = "WHITE";
    @Processor(PotionEntryProcessor.class)
    @Icon(BadPotionIconSupplier.class)
    public String potion = "none";
}
