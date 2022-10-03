package org.infernalstudios.everydesc;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.infernalstudios.everydesc.util.KeyMappings;

@Mod("everydesc")
@Mod.EventBusSubscriber
@OnlyIn(Dist.CLIENT)
public class EverythingDescriptions {

    public static final int LINES_PER_PAGE = 14;
    public static final String MOD_ID = "everydesc";

    public EverythingDescriptions() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ClientModBusEvents {
        @SubscribeEvent
        public static void onKeyRegister(RegisterKeyMappingsEvent event) {
            event.register(KeyMappings.DESCRIPTION_KEY);
        }
    }
}
