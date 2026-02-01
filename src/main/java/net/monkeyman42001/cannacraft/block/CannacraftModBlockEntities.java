/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package net.monkeyman42001.cannacraft.block;

import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.items.wrapper.SidedInvWrapper;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;

import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.Block;
import net.minecraft.core.registries.BuiltInRegistries;

//import net.monkeyman42001.cannacraft.block.entity.CannabisPlant0BlockEntity;
import net.monkeyman42001.cannacraft.CannaCraft;

//@EventBusSubscriber(modid=CannaCraft.MOD_ID, bus = EventBusSubscriber.Bus.GAME)
public class CannacraftModBlockEntities {
	public static final DeferredRegister<BlockEntityType<?>> REGISTRY = DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, CannaCraft.MOD_ID);
	//public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<CannabisPlant0BlockEntity>> CANNABIS_PLANT_0 = register("cannabis_plant_0", CannacraftModBlocks.CANNABIS_PLANT_0, CannabisPlant0BlockEntity::new);
	//public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<CannabisPlant1BlockEntity>> CANNABIS_PLANT_1 = register("cannabis_plant_1", CannacraftModBlocks.CANNABIS_PLANT_1, CannabisPlant1BlockEntity::new);
	//public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<CannabisPlant2BlockEntity>> CANNABIS_PLANT_2 = register("cannabis_plant_2", CannacraftModBlocks.CANNABIS_PLANT_2, CannabisPlant2BlockEntity::new);
	//public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<CannabisPlant3BlockEntity>> CANNABIS_PLANT_3 = register("cannabis_plant_3", CannacraftModBlocks.CANNABIS_PLANT_3, CannabisPlant3BlockEntity::new);
	//public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<CannabisPlant4BlockEntity>> CANNABIS_PLANT_4 = register("cannabis_plant_4", CannacraftModBlocks.CANNABIS_PLANT_4, CannabisPlant4BlockEntity::new);

	// Start of user code block custom block entities
	// End of user code block custom block entities
	//private static <T extends BlockEntity> DeferredHolder<BlockEntityType<?>, BlockEntityType<T>> register(String registryname, DeferredHolder<Block, Block> block, BlockEntityType.BlockEntitySupplier<T> supplier) {
	//	return REGISTRY.register(registryname, () -> new BlockEntityType(supplier, block.get()));
	//}

	//@SubscribeEvent
	public static void registerCapabilities(RegisterCapabilitiesEvent event) {
		//event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, CANNABIS_PLANT_0.get(), SidedInvWrapper::new);
		//event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, CANNABIS_PLANT_1.get(), SidedInvWrapper::new);
		//event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, CANNABIS_PLANT_2.get(), SidedInvWrapper::new);
		//event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, CANNABIS_PLANT_3.get(), SidedInvWrapper::new);
		//event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, CANNABIS_PLANT_4.get(), SidedInvWrapper::new);
	}
}