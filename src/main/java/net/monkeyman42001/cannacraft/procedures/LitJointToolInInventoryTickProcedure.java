package net.monkeyman42001.cannacraft.procedures;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.server.level.ServerLevel;

public class LitJointToolInInventoryTickProcedure {
	public static void execute(LevelAccessor world, ItemStack itemstack) {
		double burnCountdown = 0;
		if (burnCountdown == 0) {
			burnCountdown = 5;
			if (world instanceof ServerLevel _level) {
				itemstack.hurtAndBreak(1, _level, null, _stkprov -> {
				});
			}
		}
		burnCountdown = burnCountdown - 1;
	}
}