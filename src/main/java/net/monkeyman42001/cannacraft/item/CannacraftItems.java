/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package net.monkeyman42001.cannacraft.item;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredHolder;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.BlockItem;

import net.monkeyman42001.cannacraft.CannaCraft;
import net.monkeyman42001.cannacraft.block.CannacraftBlocks;

import java.util.function.Function;

public class CannacraftItems {
	public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(CannaCraft.MOD_ID);
	public static final DeferredItem<Item> CANNABIS_SEED;
	public static final DeferredItem<Item> NUG;
	public static final DeferredItem<Item> EXTRACT;
	public static final DeferredItem<Item> JOINT;
	public static final DeferredItem<Item> LIGHTER;
	public static final DeferredItem<Item> LIT_JOINT;
	static {
		CANNABIS_SEED = ITEMS.register("cannabis_seed", () -> new CannabisSeedItem(new Item.Properties()));
		NUG = ITEMS.register("nug", () -> new NugItem(new Item.Properties()));
		EXTRACT = ITEMS.register("extract", () -> new ExtractItem(new Item.Properties().stacksTo(16)));
		JOINT = ITEMS.register("joint", () -> new JointItem(new Item.Properties()));
		LIGHTER = ITEMS.register("lighter", () -> new LighterItem(new Item.Properties().durability(100)));
		LIT_JOINT = ITEMS.register("lit_joint", () -> new LitJointItem(new Item.Properties()));
	}

	// Start of user code block custom items
	// End of user code block custom items
	public static void register(IEventBus eventBus) {
		ITEMS.register(eventBus);
	}
}
