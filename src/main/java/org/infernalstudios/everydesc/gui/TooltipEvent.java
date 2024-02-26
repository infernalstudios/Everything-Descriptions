package org.infernalstudios.everydesc.gui;

import net.minecraft.ChatFormatting;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import org.infernalstudios.everydesc.EverythingDescriptions;
import org.infernalstudios.everydesc.util.KeyMappings;

import java.util.List;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class TooltipEvent {
    @SubscribeEvent
    public static void onItemTooltip(ItemTooltipEvent event) {
        String langKey = EverythingDescriptions.MOD_ID + "." + ForgeRegistries.ITEMS.getKey(event.getItemStack().getItem());
        if (I18n.exists(langKey)) {
            event.getToolTip().add(Component.translatable(EverythingDescriptions.MOD_ID + ".tooltip", KeyMappings.DESCRIPTION_KEY.getTranslatedKeyMessage()).withStyle(ChatFormatting.GRAY));
        }
        else {
            List<TagKey<Item>> tags = event.getItemStack().getTags().toList();

            String[] dummyArr1;
            String[] dummyArr2;

            for (TagKey<Item> t : tags
            ) {
                dummyArr1 = t.toString().split(" / ");
                dummyArr2 = dummyArr1[1].split("]");
                if (I18n.exists(EverythingDescriptions.MOD_ID + "." + dummyArr2[0] + ".tag")) {
                    event.getToolTip().add(Component.translatable(EverythingDescriptions.MOD_ID + ".tooltip", KeyMappings.DESCRIPTION_KEY.getTranslatedKeyMessage()).withStyle(ChatFormatting.GRAY));
                    break;
                }
            }
        }
    }
}
