package com.awakenedredstone.cbserverconfig.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Constants {
    public static final Gson GSON = new GsonBuilder().disableHtmlEscaping().setPrettyPrinting().create();
}
