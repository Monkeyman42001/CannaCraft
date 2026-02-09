package net.monkeyman42001.cannacraft.item;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.monkeyman42001.cannacraft.component.CannacraftDataComponents;
import net.minecraft.world.InteractionResult;
import net.minecraft.network.chat.Component;
import net.monkeyman42001.cannacraft.component.Strain;

import java.util.List;

public class BowlItem extends Item {
	public BowlItem(Properties properties) {
		super(properties);
	}

	public static boolean isPacked(ItemStack stack) {
		return stack.get(CannacraftDataComponents.BOWL_STRAIN.get()) != null;
	}

	public static boolean tryPack(Level level, Player player, InteractionHand bowlHand, InteractionHand cannabisHand) {
		ItemStack bowlStack = player.getItemInHand(bowlHand);
		if (!(bowlStack.getItem() instanceof BowlItem)) {
			return false;
		}
		if (isPacked(bowlStack)) {
			return false;
		}
		ItemStack cannabisStack = player.getItemInHand(cannabisHand);
		if (!cannabisStack.is(CannacraftItems.GROUND_CANNABIS.get())) {
			return false;
		}
		Strain strain = cannabisStack.get(CannacraftDataComponents.STRAIN.get());
		if (strain == null || strain.name().isBlank()) {
			return false;
		}
		if (!level.isClientSide) {
			bowlStack.set(CannacraftDataComponents.BOWL_STRAIN.get(), strain);
			if (!player.getAbilities().instabuild) {
				cannabisStack.shrink(1);
			}
		}
		return true;
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
		InteractionHand otherHand = hand == InteractionHand.MAIN_HAND ? InteractionHand.OFF_HAND : InteractionHand.MAIN_HAND;
		if (tryPack(level, player, hand, otherHand)) {
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
		if (tryPack(context.getLevel(), player, hand, otherHand)) {
			return InteractionResult.sidedSuccess(context.getLevel().isClientSide);
		}
		return InteractionResult.PASS;
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
		Strain strain = stack.get(CannacraftDataComponents.BOWL_STRAIN.get());
		if (strain == null || strain.name().isBlank()) {
			tooltipComponents.add(Component.literal("Bowl: Empty"));
			return;
		}
		tooltipComponents.add(Component.literal("Bowl: ").append(strain.coloredName()));
	}
}
