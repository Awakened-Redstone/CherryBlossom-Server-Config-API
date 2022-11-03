package com.awakenedredstone.testmod;

import com.awakenedredstone.cbserverconfig.annotation.Description;
import com.awakenedredstone.cbserverconfig.annotation.Icon;
import com.awakenedredstone.cbserverconfig.annotation.Name;
import com.awakenedredstone.cbserverconfig.annotation.Processor;

@Name("Test <blue>config</blue>!")
@Icon(Icons.Potato.class)
@Description("An example config class")
public class Config implements com.awakenedredstone.cbserverconfig.api.config.Config {
    @Description("An example of a <red>primary</red> on the config")
    public int integer = 0;
    @Icon(Icons.String.class)
    public String string = "";
    public String color = "WHITE";
    @Processor(PotionProcessor.class)
    @Icon(PotionProcessor.class)
    public String potion = "none";
}
