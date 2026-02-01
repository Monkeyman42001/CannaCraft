package net.monkeyman42001.cannacraft.item;

import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
//import net.minecraft.world.item.ToolMaterial;
import net.minecraft.world.item.Item;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.InteractionHand;

import net.monkeyman42001.cannacraft.procedures.LighterRightclickedProcedure;
import org.jetbrains.annotations.NotNull;

public class LighterItem extends Item {
	//private static final ToolMaterial TOOL_MATERIAL = new ToolMaterial(BlockTags.INCORRECT_FOR_WOODEN_TOOL, 100, 1f, 0, 2, TagKey.create(Registries.ITEM, ResourceLocation.parse("cannacraft:lighter_repair_items")));

	public LighterItem(Item.Properties properties) {
		super(properties
				//.sword(TOOL_MATERIAL, 0f, -3f)
		);
	}

	@Override
	public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level world, @NotNull Player entity, @NotNull InteractionHand hand) {
		InteractionResultHolder<ItemStack> ar = super.use(world, entity, hand);
		LighterRightclickedProcedure.execute(world, entity, entity.getItemInHand(hand));
		return ar;
	}
}