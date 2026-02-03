/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package net.monkeyman42001.cannacraft.block;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;

import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.Block;
import net.minecraft.core.registries.BuiltInRegistries;

import net.monkeyman42001.cannacraft.block.entity.CannabisPlantBlockEntity;
import net.monkeyman42001.cannacraft.CannaCraft;

public class CannacraftModBlockEntities {
	public static final DeferredRegister<BlockEntityType<?>> REGISTRY = DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, CannaCraft.MOD_ID);
	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<CannabisPlantBlockEntity>> CANNABIS_PLANT =
			register("cannabis_plant", CannacraftBlocks.CANNABIS_PLANT, CannabisPlantBlockEntity::new);

	// Start of user code block custom block entities
	// End of user code block custom block entities
	private static <T extends BlockEntity> DeferredHolder<BlockEntityType<?>, BlockEntityType<T>> register(
			String registryName,
			DeferredHolder<Block, Block> block,
			BlockEntityType.BlockEntitySupplier<T> supplier
	) {
		return REGISTRY.register(registryName, () -> BlockEntityType.Builder.of(supplier, block.get()).build(null));
	}

	public static void register(IEventBus eventBus) {
		REGISTRY.register(eventBus);
	}
}
