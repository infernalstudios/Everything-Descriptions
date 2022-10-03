package org.infernalstudios.everydesc.gui;

import net.minecraft.ChatFormatting;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import org.infernalstudios.everydesc.EverythingDescriptions;
import org.infernalstudios.everydesc.util.KeyMappings;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class TooltipEvent {
    @SubscribeEvent
    public static void onItemTooltip(ItemTooltipEvent event) {
        String langKey = EverythingDescriptions.MOD_ID + "." + ForgeRegistries.ITEMS.getKey(event.getItemStack().getItem());
        if (I18n.exists(langKey)) {
            event.getToolTip().add(new TranslatableComponent(EverythingDescriptions.MOD_ID + ".tooltip", KeyMappings.DESCRIPTION_KEY.getTranslatedKeyMessage()).withStyle(ChatFormatting.GRAY));
        }
    }
}