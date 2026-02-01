package net.monkeyman42001.cannacraft.item;

import net.minecraft.world.level.Level;
import net.minecraft.world.item.ToolMaterial;
import net.minecraft.world.item.Item;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.tags.TagKey;
import net.minecraft.tags.BlockTags;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.registries.Registries;

import net.mcreator.cannacraft.procedures.LighterRightclickedProcedure;

public class LighterItem extends Item {
	private static final ToolMaterial TOOL_MATERIAL = new ToolMaterial(BlockTags.INCORRECT_FOR_WOODEN_TOOL, 100, 1f, 0, 2, TagKey.create(Registries.ITEM, ResourceLocation.parse("cannacraft:lighter_repair_items")));

	public LighterItem(Item.Properties properties) {
		super(properties.sword(TOOL_MATERIAL, 0f, -3f));
	}

	@Override
	public InteractionResult use(Level world, Player entity, InteractionHand hand) {
		InteractionResult ar = super.use(world, entity, hand);
		LighterRightclickedProcedure.execute(world, entity, entity.getItemInHand(hand));
		return ar;
	}
}