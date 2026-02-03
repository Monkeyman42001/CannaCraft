package net.monkeyman42001.cannacraft.block;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.InteractionHand;
import net.monkeyman42001.cannacraft.block.entity.GrowTentBlockEntity;

public class GrowTentBlock extends Block implements EntityBlock {
	public GrowTentBlock(BlockBehaviour.Properties properties) {
		super(properties);
	}

	//@Override
	public int getLightBlock(BlockState state) {
		return 15;
	}

	@Override
	protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hit) {
		if (level.isClientSide()) {
			return InteractionResult.SUCCESS;
		}
		openMenu(level, pos, player);
		return InteractionResult.CONSUME;
	}

	@Override
	protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
		if (level.isClientSide()) {
			return ItemInteractionResult.sidedSuccess(true);
		}
		openMenu(level, pos, player);
		return ItemInteractionResult.CONSUME;
	}

	private void openMenu(Level level, BlockPos pos, Player player) {
		BlockEntity blockEntity = level.getBlockEntity(pos);
		if (blockEntity instanceof MenuProvider menuProvider && player instanceof ServerPlayer serverPlayer) {
			serverPlayer.openMenu(menuProvider, buf -> buf.writeBlockPos(pos));
		}
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new GrowTentBlockEntity(pos, state);
	}
}
