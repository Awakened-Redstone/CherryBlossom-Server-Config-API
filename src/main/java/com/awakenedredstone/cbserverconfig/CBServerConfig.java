package com.awakenedredstone.cbserverconfig;

import com.awakenedredstone.cbserverconfig.editor.DefaultEditors;
import com.awakenedredstone.cbserverconfig.ui.EditorRegistry;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CBServerConfig implements ModInitializer {
    public static final String MOD_ID = "cherryblossom-server-config";
    public static final Logger LOGGER = LoggerFactory.getLogger("CherryBlossom");

    @Override
    public void onInitialize() {
        EditorRegistry.register(String.class, DefaultEditors.STRING);

        EditorRegistry.register(boolean.class, DefaultEditors.BOOLEAN);
        EditorRegistry.register(Boolean.class, DefaultEditors.BOOLEAN);

        EditorRegistry.register(double.class, DefaultEditors.DOUBLE);
        EditorRegistry.register(Double.class, DefaultEditors.DOUBLE);

        EditorRegistry.register(float.class, DefaultEditors.FLOAT);
        EditorRegistry.register(Float.class, DefaultEditors.FLOAT);

        EditorRegistry.register(long.class, DefaultEditors.LONG);
        EditorRegistry.register(Long.class, DefaultEditors.LONG);

        EditorRegistry.register(int.class, DefaultEditors.INTEGER);
        EditorRegistry.register(Integer.class, DefaultEditors.INTEGER);

        EditorRegistry.register(short.class, DefaultEditors.SHORT);
        EditorRegistry.register(Short.class, DefaultEditors.SHORT);

        EditorRegistry.register(byte.class, DefaultEditors.BYTE);
        EditorRegistry.register(Byte.class, DefaultEditors.BYTE);
    }
}
