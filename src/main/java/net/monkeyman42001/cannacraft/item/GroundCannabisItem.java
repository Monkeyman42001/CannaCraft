package net.monkeyman42001.cannacraft.item;

import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.monkeyman42001.cannacraft.component.CannacraftDataComponents;
import net.monkeyman42001.cannacraft.component.Strain;
import net.minecraft.world.InteractionResult;

import java.util.List;

public class GroundCannabisItem extends Item {
	public GroundCannabisItem(Properties properties) {
		super(properties);
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
		Strain strain = stack.get(CannacraftDataComponents.STRAIN.get());
		if (strain != null && !strain.name().isBlank()) {
			tooltipComponents.add(Component.literal("Strain: ").append(strain.coloredName()));
			tooltipComponents.add(Component.literal("THC %: " + strain.thcPercentage()));
			tooltipComponents.add(Component.literal("Terpene %: " + strain.terpenePercentage()));
		} else {
			tooltipComponents.add(Component.literal("Strain: Unknown"));
		}
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
		InteractionHand otherHand = hand == InteractionHand.MAIN_HAND ? InteractionHand.OFF_HAND : InteractionHand.MAIN_HAND;
		if (BowlItem.tryPack(level, player, otherHand, hand)) {
			return InteractionResultHolder.sidedSuccess(player.getItemInHand(hand), level.isClientSide);
		}
		return InteractionResultHolder.pass(player.getItemInHand(hand));
	}

	@Override
	public InteractionResult useOn(UseOnContext context) {
		Player player = context.getPlayer();
		if (player == null) {
			return InteractionResult.PASS;
		}
		InteractionHand hand = context.getHand();
		InteractionHand otherHand = hand == InteractionHand.MAIN_HAND ? InteractionHand.OFF_HAND : InteractionHand.MAIN_HAND;
		if (BowlItem.tryPack(context.getLevel(), player, otherHand, hand)) {
			return InteractionResult.sidedSuccess(context.getLevel().isClientSide);
		}
		return InteractionResult.PASS;
	}
}
