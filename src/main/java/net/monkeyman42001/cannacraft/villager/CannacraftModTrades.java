package net.monkeyman42001.cannacraft.villager;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.server.network.Filterable;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.WrittenBookContent;
import net.minecraft.world.item.trading.ItemCost;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.village.VillagerTradesEvent;

import net.monkeyman42001.cannacraft.item.CannacraftItems;
import net.monkeyman42001.cannacraft.item.CannabisSeedItem;
import net.monkeyman42001.cannacraft.component.Strain;

import java.util.List;

public class CannacraftModTrades {
    private static final String DEALER_BOOK_TITLE = "Dealer Catalog";
    private static final String DEALER_BOOK_AUTHOR = "Dealer";

    private static VillagerTrades.ItemListing buildStrainTrade(Strain strain) {
        Strain.TradeInfo trade = strain.trade();
        if (trade == null || !trade.isValid()) {
            return null;
        }
        ItemCost cost = new ItemCost(
                BuiltInRegistries.ITEM.get(trade.costItemId()),
                trade.costCount()
        );
        if (cost.item() == Items.AIR) {
            return null;
        }
        return (trader, random) -> {
            ItemStack result = new ItemStack(CannacraftItems.CANNABIS_SEED.get(), 1);
            CannabisSeedItem.setStrain(result, strain);

            return new MerchantOffer(
                    cost,
                    result,
                    trade.maxUses(),
                    trade.villagerXp(),
                    trade.priceMultiplier()
            );
        };
    }
	@SubscribeEvent
    public static void addTrades(VillagerTradesEvent event) {
        Int2ObjectMap<List<VillagerTrades.ItemListing>> trades = event.getTrades();
        List<VillagerTrades.ItemListing> levelOne = trades.get(1);
        levelOne.clear();
        levelOne.add((trader, random) -> {
            ItemStack book = new ItemStack(Items.WRITTEN_BOOK, 1);
            java.util.List<Filterable<Component>> pages = java.util.List.of(Filterable.<Component>passThrough(Component.literal(
                    "Dealer Level 1 Strains:\n"
                            + "- Afghan Kush\n"
                            + "- Durban Poison\n"
                            + "- Haze\n"
                            + "- Skunk #1\n"
                            + "- Chemdawg\n"
                            + "- Blueberry"
            )));
            var content = new WrittenBookContent(
                    Filterable.passThrough(DEALER_BOOK_TITLE),
                    DEALER_BOOK_AUTHOR,
                    0,
                    pages,
                    true
            );
            book.set(DataComponents.WRITTEN_BOOK_CONTENT, content);

            return new MerchantOffer(
                    new ItemCost(Items.WHEAT, 1),
                    book,
                    16,
                    1,
                    0.05F
            );
        });

        Strain[] strains = Strain.STARTER_STRAINS;
        for (int i = 0; i < strains.length; i++) {
            Strain strain = strains[i];
            Strain.TradeInfo trade = strain.trade();
            VillagerTrades.ItemListing listing = buildStrainTrade(strain);
            if (trade != null && listing != null) {
                if (i == 0) {
                    levelOne.add(listing);
                } else {
                    trades.get(2).add(listing);
                }
            }
        }
    }
}
