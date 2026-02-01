package net.monkeyman42001.cannacraft.init;

import net.monkeyman42001.cannacraft.block.CannacraftBlocks;
import net.monkeyman42001.cannacraft.item.CannacraftItems;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;

import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.core.registries.Registries;

import net.monkeyman42001.cannacraft.CannaCraft;

@EventBusSubscriber(modid=CannaCraft.MOD_ID, bus = EventBusSubscriber.Bus.GAME)
public class CannacraftModTabs {
	public static final DeferredRegister<CreativeModeTab> REGISTRY = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, CannaCraft.MOD_ID);

	@SubscribeEvent
	public static void buildTabContentsVanilla(BuildCreativeModeTabContentsEvent tabData) {
		if (tabData.getTabKey() == CreativeModeTabs.NATURAL_BLOCKS) {
			tabData.accept(CannacraftItems.CANNABIS_SEED.get());
		} else if (tabData.getTabKey() == CreativeModeTabs.FOOD_AND_DRINKS) {
			tabData.accept(CannacraftItems.NUG.get());
			tabData.accept(CannacraftItems.JOINT.get());
		} else if (tabData.getTabKey() == CreativeModeTabs.TOOLS_AND_UTILITIES) {
			tabData.accept(CannacraftItems.LIGHTER.get());
		} else if (tabData.getTabKey() == CreativeModeTabs.BUILDING_BLOCKS) {
			tabData.accept(CannacraftBlocks.GROW_TENT.get().asItem());
		}
	}
}