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
	public static final DeferredItem<Item> GROUND_CANNABIS;
	public static final DeferredItem<Item> EXTRACT;
	public static final DeferredItem<Item> JOINT;
	public static final DeferredItem<Item> LIGHTER;
	public static final DeferredItem<Item> MATCHBOX;
	public static final DeferredItem<Item> GRINDER;
	public static final DeferredItem<Item> BOWL;
	static {
		CANNABIS_SEED = ITEMS.register("cannabis_seed", () -> new CannabisSeedItem(new Item.Properties()));
		NUG = ITEMS.register("nug", () -> new NugItem(new Item.Properties()));
		GROUND_CANNABIS = ITEMS.register("ground_cannabis", () -> new GroundCannabisItem(new Item.Properties()));
		EXTRACT = ITEMS.register("extract", () -> new ExtractItem(new Item.Properties().stacksTo(16)));
		JOINT = ITEMS.register("joint", () -> new JointItem(new Item.Properties()));
		LIGHTER = ITEMS.register("lighter", () -> new LighterItem(new Item.Properties().durability(100)));
		MATCHBOX = ITEMS.register("matchbox", () -> new LighterItem(new Item.Properties().durability(10)));
		GRINDER = ITEMS.register("grinder", () -> new Item(new Item.Properties().durability(128)));
		BOWL = ITEMS.register("bowl", () -> new BowlItem(new Item.Properties().stacksTo(1)));
	}

	// Start of user code block custom items
	// End of user code block custom items
	public static void register(IEventBus eventBus) {
		ITEMS.register(eventBus);
	}
}
