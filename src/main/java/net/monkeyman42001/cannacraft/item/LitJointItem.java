package net.monkeyman42001.cannacraft.item;

import net.minecraft.world.InteractionResultHolder;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
//import net.minecraft.world.item.ItemUseAnimation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.TooltipFlag;

import net.monkeyman42001.cannacraft.procedures.LitJointSmokeEffectProcedure;
import net.monkeyman42001.cannacraft.component.CannacraftDataComponents;
import net.monkeyman42001.cannacraft.component.Strain;

import java.util.List;

public class LitJointItem extends Item {
	public LitJointItem(Item.Properties properties) {
		super(properties.durability(100));
	}

	@Override
	public UseAnim getUseAnimation(ItemStack itemstack) {
		return UseAnim.BOW;
	}

	@Override
	public int getUseDuration(ItemStack itemstack, LivingEntity livingEntity) {
		return 20;
	}

	@Override
	public boolean isFoil(ItemStack itemstack) {
		return true;
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level world, Player entity, InteractionHand hand) {
		InteractionResultHolder<ItemStack> ar = super.use(world, entity, hand);
		entity.startUsingItem(hand);
		return ar;
	}

	@Override
	public ItemStack finishUsingItem(ItemStack itemstack, Level world, LivingEntity entity) {
		ItemStack retval = super.finishUsingItem(itemstack, world, entity);
		LitJointSmokeEffectProcedure.execute(entity, itemstack);
		return retval;
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
