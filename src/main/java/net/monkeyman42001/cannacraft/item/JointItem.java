package net.monkeyman42001.cannacraft.item;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.monkeyman42001.cannacraft.component.CannacraftDataComponents;
import net.monkeyman42001.cannacraft.component.Strain;

import java.util.List;

public class JointItem extends Item {
	public JointItem(Item.Properties properties) {
		super(properties.stacksTo(1));
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
		Strain strain = stack.get(CannacraftDataComponents.STRAIN.get());
		if (strain != null && !strain.name().isBlank()) {
			tooltipComponents.add(Component.literal("Strain: " + strain.name()));
			tooltipComponents.add(Component.literal("THC %: " + strain.thcPercentage()));
			tooltipComponents.add(Component.literal("Terpene %: " + strain.terpenePercentage()));
		} else {
			tooltipComponents.add(Component.literal("Strain: Unknown"));
		}
	}
}
