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

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@EventBusSubscriber(modid = CannaCraft.MOD_ID, bus = EventBusSubscriber.Bus.GAME)
public class CannacraftCraftingEvents {
	@SubscribeEvent
	public static void onItemCrafted(PlayerEvent.ItemCraftedEvent event) {
		ItemStack result = event.getCrafting();
		if (result.getItem() != CannacraftItems.JOINT.get()) {
			return;
		}

		Container inventory = event.getInventory();
		Map<String, Strain> uniqueStrains = new LinkedHashMap<>();
		for (int i = 0; i < inventory.getContainerSize(); i++) {
			ItemStack stack = inventory.getItem(i);
			if (stack.getItem() == CannacraftItems.NUG.get()) {
				Strain strain = stack.get(CannacraftDataComponents.STRAIN.get());
				if (strain != null && !strain.name().isBlank()) {
					uniqueStrains.putIfAbsent(strain.name(), strain);
				}
			}
		}

		if (!uniqueStrains.isEmpty()) {
			List<Strain> hodgepodge = new ArrayList<>(uniqueStrains.values());
			result.set(CannacraftDataComponents.HODGEPODGE.get(), hodgepodge);
			result.set(CannacraftDataComponents.STRAIN.get(), hodgepodge.get(0));
		}
	}
}
