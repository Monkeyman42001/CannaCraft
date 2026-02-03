/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package net.monkeyman42001.cannacraft.block;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.SoundType;
import net.monkeyman42001.cannacraft.item.CannacraftItems;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredBlock;

import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.BlockSetType;

import net.minecraft.world.level.block.Block;

import net.monkeyman42001.cannacraft.CannaCraft;

import java.util.function.Function;
import java.util.function.Supplier;


public class CannacraftBlocks {
	public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(CannaCraft.MOD_ID);
	public static final DeferredBlock<Block> CANNABIS_PLANT = registerBlockNoItem("cannabis_plant",
			() -> new CannabisPlantBlock(BlockBehaviour.Properties.of()
					.noCollission()
					.randomTicks()
					.instabreak()
					.sound(SoundType.CROP)));
	public static final DeferredBlock<Block> EXTRACTOR = registerBlock("extractor",
			() -> new ExtractorBlock(BlockBehaviour.Properties.of().sound(SoundType.METAL).strength(3f, 6f)));
	//public static final DeferredBlock<Block> CANNABIS_PLANT_0;
	//public static final DeferredBlock<Block> CANNABIS_PLANT_1;
	//public static final DeferredBlock<Block> CANNABIS_PLANT_2;
	//public static final DeferredBlock<Block> CANNABIS_PLANT_3;
	//public static final DeferredBlock<Block> CANNABIS_PLANT_4;
	public static final DeferredBlock<Block> GROW_TENT = registerBlock("grow_tent",
			() -> new Block(BlockBehaviour.Properties.of().sound(SoundType.GRAVEL).strength(1f, 10f)));

		//CANNABIS_PLANT_0 = register("cannabis_plant_0", CannabisPlant0Block::new);
		//CANNABIS_PLANT_1 = register("cannabis_plant_1", CannabisPlant1Block::new);
		//CANNABIS_PLANT_2 = register("cannabis_plant_2", CannabisPlant2Block::new);
		//CANNABIS_PLANT_3 = register("cannabis_plant_3", CannabisPlant3Block::new);
		//CANNABIS_PLANT_4 = register("cannabis_plant_4", CannabisPlant4Block::new);



	// Start of user code block custom blocks
	// End of user code block custom blocks
	private static <T extends Block> DeferredBlock<T> registerBlock(String name, Supplier<T> block) {
		DeferredBlock<T> toReturn = BLOCKS.register(name, block);
		registerBlockItem(name, toReturn);
		return toReturn;
	}

	private static <T extends Block> DeferredBlock<T> registerBlockNoItem(String name, Supplier<T> block) {
		return BLOCKS.register(name, block);
	}

	private static <T extends Block> void registerBlockItem(String name, DeferredBlock<T> block) {
		CannacraftItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
	}

	public static void register(IEventBus eventBus) {
		BLOCKS.register(eventBus);
	}
}
