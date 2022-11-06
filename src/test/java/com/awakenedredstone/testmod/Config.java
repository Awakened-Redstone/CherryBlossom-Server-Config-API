package com.awakenedredstone.testmod;

import com.awakenedredstone.cbserverconfig.annotation.Description;
import com.awakenedredstone.cbserverconfig.annotation.Icon;
import com.awakenedredstone.cbserverconfig.annotation.Name;
import com.awakenedredstone.cbserverconfig.annotation.Processor;

@Name("Test <blue>config</blue>!")
@Icon(Icons.Potato.class)
@Description("<gray>An example config class")
public class Config extends com.awakenedredstone.cbserverconfig.api.config.Config {
    @Description("<gray>An example of a <red>primary</red> on the config")
    public int integer = 0;
    @Icon(Icons.String.class)
    public String string = "";
    @Name("<rainbow>Super cool colors</rainbow>")
    public String color = "WHITE";
    @Processor(PotionEntryProcessor.class)
    @Icon(PotionIconSupplier.class)
    public String potion = "none";
    public boolean test1 = true;
    public boolean test2 = true;
    public boolean test3 = true;
    public boolean test4 = true;
    public boolean test5 = true;
    public boolean test6 = true;
    public boolean test7 = true;
    public boolean test8 = true;
    public boolean test9 = true;
    public boolean test10 = true;
    public boolean test11 = true;
    public boolean test12 = true;
    public boolean test13 = true;
    public boolean test14 = true;
    public boolean test15 = true;
    public boolean test16 = true;
}
