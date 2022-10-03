package org.infernalstudios.everydesc;

import net.minecraft.client.Minecraft;
import net.minecraft.client.StringSplitter;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import org.infernalstudios.everydesc.gui.DescriptionsViewScreen;
import org.infernalstudios.everydesc.util.KeyMappings;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import static org.infernalstudios.everydesc.EverythingDescriptions.LINES_PER_PAGE;

@Mod.EventBusSubscriber(modid = EverythingDescriptions.MOD_ID, value = Dist.CLIENT)
public class DescriptionInteraction {
    @SubscribeEvent
    public static void onKeyInput(InputEvent.Key event) {
        if (KeyMappings.DESCRIPTION_KEY.consumeClick()) {
            Player player = Minecraft.getInstance().player;

            ItemStack dummyStack = new ItemStack(Items.ENCHANTED_BOOK);
            dummyStack.addTagElement("title", StringTag.valueOf("Guidebook"));
            dummyStack.addTagElement("author", StringTag.valueOf("Infernal Studios"));

            ListTag pages = new ListTag();

            String idString = ForgeRegistries.ITEMS.getKey(player.getMainHandItem().getItem()).toString();
            String idStringOff = ForgeRegistries.ITEMS.getKey(player.getOffhandItem().getItem()).toString();

            boolean translated = addTranslatedPages(pages, EverythingDescriptions.MOD_ID + "." + idString);
            if (!translated) translated = addTranslatedPages(pages, EverythingDescriptions.MOD_ID + "." + idStringOff);
            if (!translated) {
                event.setCanceled(true);
                return;
            }

            dummyStack.addTagElement("pages", pages);
            final DescriptionsViewScreen screen = new DescriptionsViewScreen(new DescriptionsViewScreen.WrittenBookAccess(dummyStack));
            Minecraft.getInstance().setScreen(screen);

            event.setCanceled(true);
        }
    }

    private static boolean addTranslatedPages(ListTag nbtList, String loreKey) {
        if (I18n.exists(loreKey)) {
            String translatedLore = Component.translatable(loreKey).getString();
            if (!translatedLore.isEmpty()) {
                nbtList.addAll(SplitToPageTags(translatedLore));
                return true;
            }
        }
        return false;
    }

    private static Collection<StringTag> SplitToPageTags(String string) {
        List<String> lines = new LinkedList<>();
        StringSplitter textHandler = Minecraft.getInstance().font.getSplitter();
        textHandler.splitLines(string, 114, Style.EMPTY, true, (style, ix, jx) -> {
            String substring = string.substring(ix, jx);
            lines.add(substring);
        });

        List<StringTag> pageTags = new LinkedList<>();

        int linesLeft = LINES_PER_PAGE;
        StringBuilder curString = new StringBuilder();
        while (!lines.isEmpty()) {
            curString.append(lines.remove(0));
            linesLeft--;
            if (linesLeft <= 0) {
                linesLeft = LINES_PER_PAGE;
                pageTags.add(StringTag.valueOf(curString.toString()));
                curString = new StringBuilder();
            }
        }

        if (curString.length() > 0)
            pageTags.add(StringTag.valueOf(curString.toString()));

        return pageTags;
    }
}
