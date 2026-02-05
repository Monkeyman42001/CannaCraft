package net.monkeyman42001.cannacraft.procedures;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.monkeyman42001.cannacraft.component.CannacraftDataComponents;
import net.monkeyman42001.cannacraft.component.Strain;

import java.util.ArrayList;
import java.util.List;

public class LitJointSmokeEffectProcedure {
	public static void execute(Entity entity, ItemStack itemstack) {
		if (entity == null || itemstack == null)
			return;
		if (entity instanceof LivingEntity living && !living.level().isClientSide()) {
			List<Strain> strains = itemstack.get(CannacraftDataComponents.HODGEPODGE.get());
			if (strains == null || strains.isEmpty()) {
				Strain strain = itemstack.get(CannacraftDataComponents.STRAIN.get());
				if (strain == null) {
					living.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 400, 0));
					return;
				}
				strains = List.of(strain);
			}

			List<MobEffectInstance> allEffects = new ArrayList<>();
			for (Strain strain : strains) {
				allEffects.addAll(strain.createEffectInstances());
			}

			if (allEffects.isEmpty()) {
				living.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 400, 0));
				return;
			}
			for (var effect : allEffects) {
				living.addEffect(effect);
			}
		}
	}
}
