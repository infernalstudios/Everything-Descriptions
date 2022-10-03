package org.infernalstudios.everydesc;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.settings.KeyConflictContext;
import org.lwjgl.glfw.GLFW;

public final class KeyMappings {
    public static final String KEY_DESCRIPTION = "key." + EverythingDescriptions.MOD_ID + ".description";

    public static final KeyMapping DESCRIPTION_KEY = new KeyMapping(KEY_DESCRIPTION, KeyConflictContext.IN_GAME, InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_I, KeyMapping.CATEGORY_GAMEPLAY);
}
