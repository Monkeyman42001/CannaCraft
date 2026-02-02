package net.monkeyman42001.cannacraft.villager;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
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

	public static VillagerTrades.ItemListing sourDieselTrade() {
        return (trader, random) -> {
            ItemCost payment = new ItemCost(Items.EMERALD, 1);

            ItemStack result = new ItemStack(CannacraftItems.CANNABIS_SEED.get(), 1);
            CannabisSeedItem.setStrain(result, new Strain("Sour Diesel", 0.0f, 0.0f));

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
            CannabisSeedItem.setStrain(result, new Strain("Blue Dream", 0.0f, 0.0f));

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
            CannabisSeedItem.setStrain(result, new Strain("OG Kush", 0.0f, 0.0f));

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
        Int2ObjectMap<List<VillagerTrades.ItemListing>> trades = event.getTrades();

        trades.get(1).add((entity, randomSource) -> new MerchantOffer(
                new ItemCost(Items.EMERALD, 2),
                new ItemStack(CannacraftItems.JOINT.get(), 1), 6, 3, 0.05f));

        trades.get(1).add(sourDieselTrade());

        trades.get(1).add(blueDreamTrade());

        trades.get(1).add(ogKushTrade());
    }
}
