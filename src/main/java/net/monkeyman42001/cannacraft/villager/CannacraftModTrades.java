package net.monkeyman42001.cannacraft.villager;

import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.ItemCost;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.village.VillagerTradesEvent;

import net.monkeyman42001.cannacraft.CannaCraft;
import net.monkeyman42001.cannacraft.item.CannacraftItems;
import net.monkeyman42001.cannacraft.villager.CannacraftModVillagers;
import net.monkeyman42001.cannacraft.item.CannabisSeedItem;

import java.util.List;
import java.util.Map;

public class CannacraftModTrades {

	public static VillagerTrades.ItemListing sourDieselTrade() {
        return (trader, random) -> {
            ItemCost payment = new ItemCost(Items.EMERALD, 1);

            ItemStack result = new ItemStack(CannacraftItems.CANNABIS_SEED.get(), 1);
            CannabisSeedItem.setStrain(result, "Sour Diesel");

            return new MerchantOffer(
                    payment,
                    result,
                    16,     // max uses
                    2,      // villager XP
                    0.05F   // price multiplier
            );
        };
    }

    public static VillagerTrades.ItemListing blueDreamTrade() {
        return (trader, random) -> {
            ItemCost payment = new ItemCost(Items.WHEAT, 1);

            ItemStack result = new ItemStack(CannacraftItems.CANNABIS_SEED.get(), 1);
            CannabisSeedItem.setStrain(result, "Blue Dream");

            return new MerchantOffer(
                    payment,
                    result,
                    16,
                    2,
                    0.05F
            );
        };
    }

    public static VillagerTrades.ItemListing ogKushTrade() {
        return (trader, random) -> {
            ItemCost payment = new ItemCost(Items.DIAMOND, 1);

            ItemStack result = new ItemStack(CannacraftItems.CANNABIS_SEED.get(), 1);
            CannabisSeedItem.setStrain(result, "OG Kush");

            return new MerchantOffer(
                    payment,
                    result,
                    12,
                    5,
                    0.10F
            );
        };
    }
	@SubscribeEvent
    public static void addTrades(VillagerTradesEvent event) {
        if (event.getType().equals(CannacraftModVillagers.DEALER.getKey())) {
            return;
        }

        Map<Integer, List<VillagerTrades.ItemListing>> trades = event.getTrades();

        trades.get(1).add(sourDieselTrade());
        
        trades.get(1).add(blueDreamTrade());

        trades.get(1).add(ogKushTrade());
    }
}