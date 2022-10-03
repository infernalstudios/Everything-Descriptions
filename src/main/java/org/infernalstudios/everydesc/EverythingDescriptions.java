package org.infernalstudios.everydesc;

import net.minecraft.client.Minecraft;
import net.minecraft.client.StringSplitter;
import net.minecraft.client.gui.screens.inventory.BookViewScreen;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.EnchantedBookItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.registries.ForgeRegistries;

import javax.swing.text.JTextComponent;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

@Mod("everydesc")
@Mod.EventBusSubscriber
@OnlyIn(Dist.CLIENT)
public class EverythingDescriptions {

    private static final int LINES_PER_PAGE = 14;
    public static final String MOD_ID = "everydesc";
    private static final String MOD_ID_DOT = MOD_ID + ".";

    public EverythingDescriptions() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public static void onKeyRegister(RegisterKeyMappingsEvent event) {
        event.register(KeyMappings.DESCRIPTION_KEY);
    }

    @SubscribeEvent
    public static void onKeyInput(InputEvent.Key event) {
        if (KeyMappings.DESCRIPTION_KEY.consumeClick()) {
            Player player = Minecraft.getInstance().player;
            Level world = player.getLevel();

            ItemStack usedStack = player.getMainHandItem();
            ItemStack usedStackOff = player.getOffhandItem();
            if (!(world instanceof ClientLevel)) {
                event.setCanceled(true);
                return;
            }

            Minecraft clientInstance = Minecraft.getInstance();

            ItemStack dummyStack = new ItemStack(Items.ENCHANTED_BOOK);
            // even though title and author will never be seen,
            // both must be present or the BookScreen#WrittenBookContents
            // won't consider the nbt valid
            dummyStack.addTagElement("title", StringTag.valueOf(usedStack.getDisplayName().getString()));
            dummyStack.addTagElement("author", StringTag.valueOf("Kuraion"));

            ListTag pages = new ListTag();

            String idString = ForgeRegistries.ITEMS.getKey(usedStack.getItem()).toString();
            String idStringOff = ForgeRegistries.ITEMS.getKey(usedStackOff.getItem()).toString();

            boolean translated = addTranslatedPages(pages, MOD_ID_DOT + idString);
            if (!translated) translated = addTranslatedPages(pages, MOD_ID_DOT + idStringOff);
            if (!translated) {
                event.setCanceled(true);
                return;
            }

            dummyStack.addTagElement("pages", pages);
            final BookViewScreen screen = new BookViewScreen(new BookViewScreen.WrittenBookAccess(dummyStack));
            clientInstance.setScreen(screen);

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
