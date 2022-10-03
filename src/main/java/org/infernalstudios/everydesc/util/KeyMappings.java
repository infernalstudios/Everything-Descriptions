package org.infernalstudios.everydesc.util;

import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.ClientRegistry;
import org.lwjgl.glfw.GLFW;

public class KeyMappings {

    public static KeyMapping DESCRIPTION_KEY;

    public static void registerKeys() {
        DESCRIPTION_KEY = registerKeybinding(
                new KeyMapping("key.everydesc.description", GLFW.GLFW_KEY_I, KeyMapping.CATEGORY_GAMEPLAY));
    }

    private static KeyMapping registerKeybinding(KeyMapping key) {
        ClientRegistry.registerKeyBinding(key);
        return key;
    }
}


