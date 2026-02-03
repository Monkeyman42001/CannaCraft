package net.monkeyman42001.cannacraft.procedures;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.monkeyman42001.cannacraft.component.CannacraftDataComponents;
import net.monkeyman42001.cannacraft.component.Strain;

public class LitJointSmokeEffectProcedure {
	public static void execute(Entity entity, ItemStack itemstack) {
		if (entity == null || itemstack == null)
			return;
		if (entity instanceof LivingEntity living && !living.level().isClientSide()) {
			Strain strain = itemstack.get(CannacraftDataComponents.STRAIN.get());
			String name = strain != null ? strain.name() : "";

			MobEffectInstance effect;
			switch (name) {
				case "Sour Diesel" -> effect = new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 600, 1);
				case "Blue Dream" -> effect = new MobEffectInstance(MobEffects.JUMP, 600, 1);
				case "OG Kush" -> effect = new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 600, 0);
				case "Girl Scout Cookies" -> effect = new MobEffectInstance(MobEffects.REGENERATION, 400, 0);
				case "Gelato" -> effect = new MobEffectInstance(MobEffects.ABSORPTION, 600, 1);
				case "Gorilla Glue" -> effect = new MobEffectInstance(MobEffects.DAMAGE_BOOST, 600, 0);
				case "Granddaddy Purple" -> effect = new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 400, 0);
				default -> effect = new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 400, 0);
			}

			living.addEffect(effect);
		}
	}
}
