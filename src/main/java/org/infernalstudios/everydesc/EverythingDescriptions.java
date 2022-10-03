package org.infernalstudios.everydesc;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
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

    @Mod.EventBusSubscriber(modid = MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ClientProxy {
        @SubscribeEvent
        public static void clientSetup(FMLClientSetupEvent event) {
            KeyMappings.registerKeys();
        }
    }
}