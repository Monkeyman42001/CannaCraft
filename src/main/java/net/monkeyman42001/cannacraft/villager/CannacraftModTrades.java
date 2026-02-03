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

    private record StrainTrade(
            String name,
            float thc,
            float terpene,
            ItemCost cost,
            int maxUses,
            int villagerXp,
            float priceMultiplier,
            int level
    ) {}

    private static final StrainTrade[] STRAIN_TRADES = new StrainTrade[] {
            new StrainTrade("Sour Diesel", 19.0f, 1.6f, new ItemCost(Items.EMERALD, 1), 16, 2, 0.05F, 1),
            new StrainTrade("Blue Dream", 19.2f, 1.9f, new ItemCost(Items.WHEAT, 1), 16, 2, 0.05F, 1),
            new StrainTrade("OG Kush", 19.1f, 1.35f, new ItemCost(Items.DIAMOND, 1), 12, 5, 0.10F, 1),
            new StrainTrade("Girl Scout Cookies", 19.1f, 1.4f, new ItemCost(Items.EMERALD, 2), 12, 5, 0.08F, 2),
            new StrainTrade("Gelato", 18.7f, 1.0f, new ItemCost(Items.EMERALD, 3), 12, 5, 0.08F, 2),
            new StrainTrade("Gorilla Glue", 21.3f, 1.53f, new ItemCost(Items.EMERALD, 4), 10, 8, 0.10F, 3),
            new StrainTrade("Granddaddy Purple", 20.0f, 1.07f, new ItemCost(Items.EMERALD, 3), 10, 8, 0.10F, 3)
    };

    private static VillagerTrades.ItemListing buildStrainTrade(StrainTrade trade) {
        return (trader, random) -> {
            ItemStack result = new ItemStack(CannacraftItems.CANNABIS_SEED.get(), 1);
            CannabisSeedItem.setStrain(result, new Strain(trade.name, trade.thc, trade.terpene));

            return new MerchantOffer(
                    trade.cost,
                    result,
                    trade.maxUses,
                    trade.villagerXp,
                    trade.priceMultiplier
            );
        };
    }
	@SubscribeEvent
    public static void addTrades(VillagerTradesEvent event) {
        Int2ObjectMap<List<VillagerTrades.ItemListing>> trades = event.getTrades();

        trades.get(1).add((entity, randomSource) -> new MerchantOffer(
                new ItemCost(Items.EMERALD, 2),
                new ItemStack(CannacraftItems.JOINT.get(), 1), 6, 3, 0.05f));

        for (StrainTrade trade : STRAIN_TRADES) {
            trades.get(trade.level).add(buildStrainTrade(trade));
        }
    }
}
