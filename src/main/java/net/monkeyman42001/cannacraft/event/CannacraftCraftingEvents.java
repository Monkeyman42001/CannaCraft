package net.monkeyman42001.cannacraft.event;

import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.monkeyman42001.cannacraft.CannaCraft;
import net.monkeyman42001.cannacraft.component.CannacraftDataComponents;
import net.monkeyman42001.cannacraft.component.Strain;
import net.monkeyman42001.cannacraft.item.CannacraftItems;

@EventBusSubscriber(modid = CannaCraft.MOD_ID, bus = EventBusSubscriber.Bus.GAME)
public class CannacraftCraftingEvents {
	@SubscribeEvent
	public static void onItemCrafted(PlayerEvent.ItemCraftedEvent event) {
		ItemStack result = event.getCrafting();
		if (result.getItem() != CannacraftItems.JOINT.get()) {
			return;
		}

		Container inventory = event.getInventory();
		Strain strain = null;
		for (int i = 0; i < inventory.getContainerSize(); i++) {
			ItemStack stack = inventory.getItem(i);
			if (stack.getItem() == CannacraftItems.NUG.get()) {
				strain = stack.get(CannacraftDataComponents.STRAIN.get());
				if (strain != null) {
					break;
				}
			}
		}

		if (strain != null) {
			result.set(CannacraftDataComponents.STRAIN.get(), strain);
		}
	}
}
