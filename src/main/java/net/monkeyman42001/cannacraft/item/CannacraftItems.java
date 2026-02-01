/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package net.monkeyman42001.cannacraft.item;

import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredHolder;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.BlockItem;

import net.monkeyman42001.cannacraft.item.NugItem;
import net.monkeyman42001.cannacraft.item.LitJointItem;
import net.monkeyman42001.cannacraft.item.LighterItem;
import net.monkeyman42001.cannacraft.item.JointItem;
import net.monkeyman42001.cannacraft.item.CannabisSeedItem;
import net.monkeyman42001.cannacraft.CannacraftMod;

import java.util.function.Function;

public class CannacraftItems {
	public static final DeferredRegister.Items REGISTRY = DeferredRegister.createItems(CannacraftMod.MODID);
	public static final DeferredItem<Item> CANNABIS_SEED;
	public static final DeferredItem<Item> NUG;
	public static final DeferredItem<Item> JOINT;
	public static final DeferredItem<Item> LIGHTER;
	public static final DeferredItem<Item> GROW_TENT;
	public static final DeferredItem<Item> LIT_JOINT;
	static {
		CANNABIS_SEED = register("cannabis_seed", CannabisSeedItem::new);
		NUG = register("nug", NugItem::new);
		JOINT = register("joint", JointItem::new);
		LIGHTER = register("lighter", LighterItem::new);
		GROW_TENT = block(CannacraftModBlocks.GROW_TENT);
		LIT_JOINT = register("lit_joint", LitJointItem::new);
	}

	// Start of user code block custom items
	// End of user code block custom items
	private static <I extends Item> DeferredItem<I> register(String name, Function<Item.Properties, ? extends I> supplier) {
		return REGISTRY.registerItem(name, supplier, new Item.Properties());
	}

	private static DeferredItem<Item> block(DeferredHolder<Block, Block> block) {
		return block(block, new Item.Properties());
	}

	private static DeferredItem<Item> block(DeferredHolder<Block, Block> block, Item.Properties properties) {
		return REGISTRY.registerItem(block.getId().getPath(), prop -> new BlockItem(block.get(), prop), properties);
	}
}