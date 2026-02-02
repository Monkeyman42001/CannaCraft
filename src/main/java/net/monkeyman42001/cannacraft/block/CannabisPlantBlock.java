package net.monkeyman42001.cannacraft.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.monkeyman42001.cannacraft.block.entity.CannabisPlantBlockEntity;
import net.monkeyman42001.cannacraft.component.CannacraftDataComponents;
import net.monkeyman42001.cannacraft.component.Strain;
import net.monkeyman42001.cannacraft.item.CannacraftItems;

import java.util.ArrayList;
import java.util.List;

public class CannabisPlantBlock extends CropBlock implements EntityBlock {
	public static final IntegerProperty AGE = IntegerProperty.create("age", 0, 4);
	private static final VoxelShape[] SHAPES = new VoxelShape[]{
			Block.box(0.0D, 0.0D, 0.0D, 16.0D, 2.0D, 16.0D),
			Block.box(0.0D, 0.0D, 0.0D, 16.0D, 4.0D, 16.0D),
			Block.box(0.0D, 0.0D, 0.0D, 16.0D, 6.0D, 16.0D),
			Block.box(0.0D, 0.0D, 0.0D, 16.0D, 8.0D, 16.0D),
			Block.box(0.0D, 0.0D, 0.0D, 16.0D, 10.0D, 16.0D)
	};

	public CannabisPlantBlock(Properties properties) {
		super(properties);
		registerDefaultState(this.stateDefinition.any().setValue(getAgeProperty(), 0));
	}

	@Override
	public IntegerProperty getAgeProperty() {
		return AGE;
	}

	@Override
	public int getMaxAge() {
		return 4;
	}

	@Override
	protected ItemLike getBaseSeedId() {
		return CannacraftItems.CANNABIS_SEED.get();
	}

	@Override
	public VoxelShape getShape(BlockState state, LevelReader level, BlockPos pos, CollisionContext context) {
		return SHAPES[state.getValue(getAgeProperty())];
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(AGE);
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new CannabisPlantBlockEntity(pos, state);
	}

	@Override
	public List<ItemStack> getDrops(BlockState state, LootParams.Builder params) {
		List<ItemStack> drops = new ArrayList<>();
		Strain strain = null;
		BlockEntity blockEntity = params.getOptionalParameter(LootContextParams.BLOCK_ENTITY);
		if (blockEntity instanceof CannabisPlantBlockEntity plantEntity) {
			strain = plantEntity.getStrain();
		}

		int age = getAge(state);
		if (age >= getMaxAge()) {
			int seedCount = 1 + params.getLevel().random.nextInt(3);
			int nugCount = 1 + params.getLevel().random.nextInt(2);
			for (int i = 0; i < seedCount; i++) {
				ItemStack seed = new ItemStack(CannacraftItems.CANNABIS_SEED.get());
				if (strain != null) {
					seed.set(CannacraftDataComponents.STRAIN.get(), strain);
				}
				drops.add(seed);
			}
			for (int i = 0; i < nugCount; i++) {
				ItemStack nug = new ItemStack(CannacraftItems.NUG.get());
				if (strain != null) {
					nug.set(CannacraftDataComponents.STRAIN.get(), strain);
				}
				drops.add(nug);
			}
		} else {
			ItemStack seed = new ItemStack(CannacraftItems.CANNABIS_SEED.get());
			if (strain != null) {
				seed.set(CannacraftDataComponents.STRAIN.get(), strain);
			}
			drops.add(seed);
		}

		return drops;
	}
}
