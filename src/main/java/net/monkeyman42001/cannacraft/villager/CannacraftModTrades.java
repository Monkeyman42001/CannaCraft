package net.monkeyman42001.cannacraft.villager;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.ItemCost;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.village.VillagerTradesEvent;

import net.monkeyman42001.cannacraft.item.CannacraftItems;
import net.monkeyman42001.cannacraft.item.CannabisSeedItem;
import net.monkeyman42001.cannacraft.component.Strain;

import java.util.List;

public class CannacraftModTrades {
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

        Strain[] strains = Strain.DEFAULT_STRAINS;
        for (Strain strain : strains) {
            Strain.TradeInfo trade = strain.trade();
            VillagerTrades.ItemListing listing = buildStrainTrade(strain);
            if (trade != null && listing != null) {
                trades.get(trade.level()).add(listing);
            }
        }
    }
}
