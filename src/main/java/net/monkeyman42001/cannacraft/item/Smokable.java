package net.monkeyman42001.cannacraft.item;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.monkeyman42001.cannacraft.component.CannacraftDataComponents;
import net.monkeyman42001.cannacraft.component.Strain;

import java.util.List;

public class Smokable extends Item {
	public Smokable(Item.Properties properties) {
		super(properties);
	}

	public static void setHodgepodge(ItemStack stack, List<Strain> strains) {
		if (strains == null || strains.isEmpty()) {
			stack.set(CannacraftDataComponents.HODGEPODGE.get(), List.of());
			return;
		}
		stack.set(CannacraftDataComponents.HODGEPODGE.get(), List.copyOf(strains));
	}

	public static List<Strain> getHodgepodge(ItemStack stack) {
		List<Strain> strains = stack.get(CannacraftDataComponents.HODGEPODGE.get());
		return strains == null ? List.of() : strains;
	}

	public static void setLit(ItemStack stack, boolean lit) {
		stack.set(CannacraftDataComponents.LIT.get(), lit);
	}

	public static boolean isLit(ItemStack stack) {
		Boolean lit = stack.get(CannacraftDataComponents.LIT.get());
		return lit != null && lit;
	}
}
