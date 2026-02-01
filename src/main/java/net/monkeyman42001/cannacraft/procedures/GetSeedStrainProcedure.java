package net.monkeyman42001.cannacraft.procedures;

import net.minecraft.world.entity.Entity;

public class GetSeedStrainProcedure {
	public static String execute(Entity entity) {
		if (entity == null)
			return "";
		return entity.getPersistentData().getString("strain");
	}
}