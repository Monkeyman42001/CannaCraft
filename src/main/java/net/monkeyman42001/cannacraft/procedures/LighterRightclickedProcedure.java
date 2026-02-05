package net.monkeyman42001.cannacraft.procedures;

import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.InteractionHand;

import net.monkeyman42001.cannacraft.item.CannacraftItems;
import net.monkeyman42001.cannacraft.item.Smokable;

public class LighterRightclickedProcedure {
	public static void execute(LevelAccessor world, Entity entity, ItemStack itemstack) {
		if (entity == null)
			return;
		if (!(world instanceof Level level) || level.isClientSide()) {
			return;
		}

		if (!(entity instanceof LivingEntity livingEntity)) {
			return;
		}

		ItemStack mainHand = livingEntity.getMainHandItem();
		ItemStack offHand = livingEntity.getOffhandItem();

		boolean lighterInMain = itemstack == mainHand;
		boolean lighterInOff = itemstack == offHand;
		if (!lighterInMain && !lighterInOff) {
			return;
		}

		boolean jointInMain = mainHand.getItem() == CannacraftItems.JOINT.get();
		boolean jointInOff = offHand.getItem() == CannacraftItems.JOINT.get();
		if (!jointInMain && !jointInOff) {
			return;
		}

		ItemStack jointStack = jointInMain ? mainHand : offHand;
		if (Smokable.isLit(jointStack)) {
			return;
		}
		Smokable.setLit(jointStack, true);
		if (livingEntity instanceof Player player) {
			player.getInventory().setChanged();
		}

		if (!(livingEntity instanceof Player player) || !player.getAbilities().instabuild) {
			itemstack.hurtAndBreak(1, livingEntity, LivingEntity.getSlotForHand(lighterInMain ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND));
		}
	}
}
