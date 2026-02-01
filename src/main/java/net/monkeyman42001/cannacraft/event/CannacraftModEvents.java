package net.monkeyman42001.cannacraft.event;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import java.util.List;

import net.minecraft.world.entity.npc.VillagerTrades;
import net.monkeyman42001.cannacraft.CannaCraft;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.village.VillagerTradesEvent;

import net.monkeyman42001.cannacraft.villager.CannacraftModTrades;
import net.monkeyman42001.cannacraft.CannaCraft;
import net.monkeyman42001.cannacraft.villager.CannacraftModVillagers;


@EventBusSubscriber(modid = CannaCraft.MOD_ID, bus = EventBusSubscriber.Bus.GAME, value = Dist.CLIENT)
public class CannacraftModEvents {

	 @SubscribeEvent
    public static void addCustomTrades(VillagerTradesEvent event) {

        if(event.getType().equals(CannacraftModVillagers.DEALER)) {
            Int2ObjectMap<List<VillagerTrades.ItemListing>> trades = event.getTrades();

            trades.get(1).add(CannacraftModTrades.sourDieselTrade());

            trades.get(1).add(CannacraftModTrades.blueDreamTrade());

            trades.get(1).add(CannacraftModTrades.ogKushTrade());
        }
    }
    
}