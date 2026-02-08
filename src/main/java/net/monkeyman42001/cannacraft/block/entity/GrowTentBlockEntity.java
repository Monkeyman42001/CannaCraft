package net.monkeyman42001.cannacraft.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.NonNullList;

import net.monkeyman42001.cannacraft.block.CannacraftModBlockEntities;
import net.monkeyman42001.cannacraft.menu.GrowTentMenu;

public class GrowTentBlockEntity extends BlockEntity implements Container, MenuProvider {
	private static final int SLOT_SEED_LEFT = 0;
	private static final int SLOT_SEED_RIGHT = 1;
	private static final int SLOT_OUTPUT = 2;
	private static final int SLOT_COUNT = 3;
	private final NonNullList<ItemStack> items = NonNullList.withSize(SLOT_COUNT, ItemStack.EMPTY);
	private boolean updatingOutput;

	public GrowTentBlockEntity(BlockPos pos, BlockState state) {
		super(CannacraftModBlockEntities.GROW_TENT.get(), pos, state);
	}

	@Override
	protected void saveAdditional(CompoundTag tag, HolderLookup.Provider provider) {
		super.saveAdditional(tag, provider);
		ContainerHelper.saveAllItems(tag, items, provider);
	}

	@Override
	protected void loadAdditional(CompoundTag tag, HolderLookup.Provider provider) {
		super.loadAdditional(tag, provider);
		ContainerHelper.loadAllItems(tag, items, provider);
	}

	@Override
	public Component getDisplayName() {
		return Component.translatable("block.cannacraft.grow_tent");
	}

	@Override
	public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
		return new GrowTentMenu(id, inventory, this);
	}

	@Override
	public int getContainerSize() {
		return items.size();
	}

	@Override
	public boolean isEmpty() {
		for (ItemStack stack : items) {
			if (!stack.isEmpty()) {
				return false;
			}
		}
		return true;
	}

	@Override
	public ItemStack getItem(int slot) {
		return items.get(slot);
	}

	@Override
	public ItemStack removeItem(int slot, int amount) {
		ItemStack stack = ContainerHelper.removeItem(items, slot, amount);
		if (!stack.isEmpty()) {
			setChanged();
			if (slot == SLOT_SEED_LEFT || slot == SLOT_SEED_RIGHT) {
				updateOutput();
			}
		}
		return stack;
	}

	@Override
	public ItemStack removeItemNoUpdate(int slot) {
		return ContainerHelper.takeItem(items, slot);
	}

	@Override
	public void setItem(int slot, ItemStack stack) {
		items.set(slot, stack);
		if (stack.getCount() > getMaxStackSize()) {
			stack.setCount(getMaxStackSize());
		}
		setChanged();
		if (!updatingOutput && (slot == SLOT_SEED_LEFT || slot == SLOT_SEED_RIGHT)) {
			updateOutput();
		}
	}

	@Override
	public boolean stillValid(Player player) {
		return level != null && level.getBlockEntity(worldPosition) == this
				&& player.distanceToSqr((double) worldPosition.getX() + 0.5D, (double) worldPosition.getY() + 0.5D, (double) worldPosition.getZ() + 0.5D) <= 64.0D;
	}

	@Override
	public void clearContent() {
		items.clear();
	}

	public void updateOutput() {
		if (level == null || level.isClientSide()) {
			return;
		}
		updatingOutput = true;
		try {
			ItemStack output = createBreedResult();
			items.set(SLOT_OUTPUT, output);
			setChanged();
		} finally {
			updatingOutput = false;
		}
	}

	private ItemStack createBreedResult() {
		ItemStack left = items.get(SLOT_SEED_LEFT);
		ItemStack right = items.get(SLOT_SEED_RIGHT);
		if (left.isEmpty() || right.isEmpty()) {
			return ItemStack.EMPTY;
		}
		if (left.getItem() != net.monkeyman42001.cannacraft.item.CannacraftItems.CANNABIS_SEED.get()
				|| right.getItem() != net.monkeyman42001.cannacraft.item.CannacraftItems.CANNABIS_SEED.get()) {
			return ItemStack.EMPTY;
		}
		var leftStrain = net.monkeyman42001.cannacraft.item.CannabisSeedItem.getStrain(left);
		var rightStrain = net.monkeyman42001.cannacraft.item.CannabisSeedItem.getStrain(right);
		if (leftStrain == null || rightStrain == null) {
			return ItemStack.EMPTY;
		}
		if (leftStrain.name().equalsIgnoreCase(rightStrain.name())) {
			return ItemStack.EMPTY;
		}

		var resultStrain = net.monkeyman42001.cannacraft.component.Strain.getBreedingResult(leftStrain, rightStrain);
		if (resultStrain == null) {
			return ItemStack.EMPTY;
		}

		var leftLineage = net.monkeyman42001.cannacraft.item.CannabisSeedItem.getLineage(left);
		var rightLineage = net.monkeyman42001.cannacraft.item.CannabisSeedItem.getLineage(right);
		net.monkeyman42001.cannacraft.component.LineageNode leftNode = leftLineage == null
				? new net.monkeyman42001.cannacraft.component.LineageNode(leftStrain.name(), java.util.List.of())
				: leftLineage;
		net.monkeyman42001.cannacraft.component.LineageNode rightNode = rightLineage == null
				? new net.monkeyman42001.cannacraft.component.LineageNode(rightStrain.name(), java.util.List.of())
				: rightLineage;
		ItemStack result = new ItemStack(net.monkeyman42001.cannacraft.item.CannacraftItems.CANNABIS_SEED.get());
		net.monkeyman42001.cannacraft.item.CannabisSeedItem.setStrain(result, resultStrain);
		net.monkeyman42001.cannacraft.item.CannabisSeedItem.setLineage(result, buildLineageTree(resultStrain.name(), leftNode, rightNode));
		return result;
	}

	private net.monkeyman42001.cannacraft.component.LineageNode buildLineageTree(
			String name,
			net.monkeyman42001.cannacraft.component.LineageNode leftNode,
			net.monkeyman42001.cannacraft.component.LineageNode rightNode
	) {
		return new net.monkeyman42001.cannacraft.component.LineageNode(name, java.util.List.of(leftNode, rightNode));
	}
}
