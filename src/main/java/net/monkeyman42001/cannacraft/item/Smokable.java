package net.monkeyman42001.cannacraft.item;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
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

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
		List<Strain> strains = stack.get(CannacraftDataComponents.HODGEPODGE.get());
		if (strains == null || strains.isEmpty()) {
			Strain strain = stack.get(CannacraftDataComponents.STRAIN.get());
			if (strain != null && !strain.name().isBlank()) {
				tooltipComponents.add(Component.literal("Strain: ").append(strain.coloredName()));
				tooltipComponents.add(Component.literal("THC %: " + strain.thcPercentage()));
				tooltipComponents.add(Component.literal("Terpene %: " + strain.terpenePercentage()));
			} else {
				tooltipComponents.add(Component.literal("Strain: Unknown"));
			}
			return;
		}

		if (strains.size() == 1) {
			Strain strain = strains.get(0);
			if (strain != null && !strain.name().isBlank()) {
				tooltipComponents.add(Component.literal("Strain: ").append(strain.coloredName()));
				tooltipComponents.add(Component.literal("THC %: " + strain.thcPercentage()));
				tooltipComponents.add(Component.literal("Terpene %: " + strain.terpenePercentage()));
			} else {
				tooltipComponents.add(Component.literal("Strain: Unknown"));
			}
			return;
		}

		tooltipComponents.add(Component.literal("Strain: Hodgepodge"));
		if (Screen.hasShiftDown()) {
			for (Strain strain : strains) {
				if (strain == null || strain.name().isBlank()) {
					continue;
				}
				tooltipComponents.add(Component.literal("- ").append(strain.coloredName()));
			}
		} else {
			tooltipComponents.add(Component.literal("Hold Shift for details"));
		}
	}
}
