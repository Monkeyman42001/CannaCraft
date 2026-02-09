package net.monkeyman42001.cannacraft.item;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraft.world.item.ItemStack;
import net.monkeyman42001.cannacraft.procedures.LitJointSmokeEffectProcedure;

public class JointItem extends Smokable {
	private static final boolean ENABLE_INVENTORY_BURN = true;
	private static final int BURN_TICKS_PER_DAMAGE = 2;
	private static final int BURN_DAMAGE = 1;
	private static final int USE_DAMAGE = 120;

	public JointItem(Item.Properties properties) {
		super(properties.stacksTo(1).durability(360));
	}

	@Override
	public UseAnim getUseAnimation(ItemStack itemstack) {
		return isLit(itemstack) ? UseAnim.BOW : UseAnim.NONE;
	}

	@Override
	public int getUseDuration(ItemStack itemstack, LivingEntity livingEntity) {
		return isLit(itemstack) ? 20 : 0;
	}

	@Override
	public boolean isFoil(ItemStack itemstack) {
		return isLit(itemstack);
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level world, Player entity, InteractionHand hand) {
		ItemStack stack = entity.getItemInHand(hand);
		if (entity.isShiftKeyDown() && isLit(stack)) {
			if (!world.isClientSide) {
				setLit(stack, false);
				ItemStack otherHand = entity.getItemInHand(hand == InteractionHand.MAIN_HAND ? InteractionHand.OFF_HAND : InteractionHand.MAIN_HAND);
				if (otherHand.getItem() == this && isLit(otherHand)) {
					setLit(otherHand, false);
				}
			}
			return InteractionResultHolder.sidedSuccess(stack, world.isClientSide);
		}
		if (!isLit(stack)) {
			return InteractionResultHolder.pass(stack);
		}
		InteractionResultHolder<ItemStack> ar = super.use(world, entity, hand);
		entity.startUsingItem(hand);
		return ar;
	}

	@Override
	public ItemStack finishUsingItem(ItemStack itemstack, Level world, LivingEntity entity) {
		ItemStack retval = super.finishUsingItem(itemstack, world, entity);
		if (isLit(itemstack)) {
			LitJointSmokeEffectProcedure.execute(entity, itemstack);
			if (!world.isClientSide) {
				int nextDamage = itemstack.getDamageValue() + USE_DAMAGE;
				if (nextDamage >= itemstack.getMaxDamage()) {
					itemstack.shrink(1);
				} else {
					itemstack.setDamageValue(nextDamage);
				}
			}
		}
		return retval;
	}

	@Override
	public void inventoryTick(ItemStack itemstack, Level world, Entity entity, int slot, boolean selected) {
		super.inventoryTick(itemstack, world, entity, slot, selected);
		if (ENABLE_INVENTORY_BURN && !world.isClientSide && entity instanceof LivingEntity livingEntity && isLit(itemstack)) {
			if (livingEntity.tickCount % BURN_TICKS_PER_DAMAGE == 0) {
				int nextDamage = itemstack.getDamageValue() + BURN_DAMAGE;
				if (nextDamage >= itemstack.getMaxDamage()) {
					itemstack.shrink(1);
				} else {
					itemstack.setDamageValue(nextDamage);
				}
			}
		}
	}

	@Override
	public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
		return slotChanged || oldStack.getItem() != newStack.getItem();
	}

}
