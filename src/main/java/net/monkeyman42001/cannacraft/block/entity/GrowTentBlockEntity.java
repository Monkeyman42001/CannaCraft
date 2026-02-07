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
	private static final String TAG_BREED_NAME = "BreedName";
	private final NonNullList<ItemStack> items = NonNullList.withSize(SLOT_COUNT, ItemStack.EMPTY);
	private String breedName = "";
	private boolean updatingOutput;

	public GrowTentBlockEntity(BlockPos pos, BlockState state) {
		super(CannacraftModBlockEntities.GROW_TENT.get(), pos, state);
	}

	@Override
	protected void saveAdditional(CompoundTag tag, HolderLookup.Provider provider) {
		super.saveAdditional(tag, provider);
		ContainerHelper.saveAllItems(tag, items, provider);
		tag.putString(TAG_BREED_NAME, breedName);
	}

	@Override
	protected void loadAdditional(CompoundTag tag, HolderLookup.Provider provider) {
		super.loadAdditional(tag, provider);
		ContainerHelper.loadAllItems(tag, items, provider);
		breedName = tag.getString(TAG_BREED_NAME);
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

	public void setBreedName(String name) {
		String trimmed = name == null ? "" : name.trim();
		if (!trimmed.equals(breedName)) {
			breedName = trimmed;
			updateOutput();
		}
	}

	public String getBreedName() {
		return breedName;
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
		if (breedName.isBlank()) {
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

		var leftLineage = net.monkeyman42001.cannacraft.item.CannabisSeedItem.getLineage(left);
		var rightLineage = net.monkeyman42001.cannacraft.item.CannabisSeedItem.getLineage(right);
		net.monkeyman42001.cannacraft.component.LineageNode leftNode = leftLineage == null
				? new net.monkeyman42001.cannacraft.component.LineageNode(leftStrain.name(), java.util.List.of())
				: leftLineage;
		net.monkeyman42001.cannacraft.component.LineageNode rightNode = rightLineage == null
				? new net.monkeyman42001.cannacraft.component.LineageNode(rightStrain.name(), java.util.List.of())
				: rightLineage;
		int generation = Math.max(leftNode.depth(), rightNode.depth()) + 1;

		float baseThc = lerp(leftStrain.thcPercentage(), rightStrain.thcPercentage(), 0.4f);
		float baseTerpene = lerp(leftStrain.terpenePercentage(), rightStrain.terpenePercentage(), 0.4f);
		float gainFactor = Math.min(0.05f + (generation * 0.005f), 0.20f);

		float newThc = roundToTenth(applyGain(baseThc, 30.0f, gainFactor));
		float newTerpene = roundToHundredth(applyGain(baseTerpene, 3.0f, gainFactor));

		var newStrain = new net.monkeyman42001.cannacraft.component.Strain(
				breedName,
				newThc,
				newTerpene,
				leftStrain.colorRgb(),
				leftStrain.trade(),
				leftStrain.effects()
		);
		ItemStack result = new ItemStack(net.monkeyman42001.cannacraft.item.CannacraftItems.CANNABIS_SEED.get());
		net.monkeyman42001.cannacraft.item.CannabisSeedItem.setStrain(result, newStrain);
		net.monkeyman42001.cannacraft.item.CannabisSeedItem.setLineage(result, buildLineageTree(leftNode, rightNode));
		return result;
	}

	private static float lerp(float start, float end, float factor) {
		return start + (end - start) * factor;
	}

	private static float applyGain(float baseValue, float capValue, float gainFactor) {
		return baseValue + (capValue - baseValue) * gainFactor;
	}

	private static float roundToTenth(float value) {
		return Math.round(value * 10.0f) / 10.0f;
	}

	private static float roundToHundredth(float value) {
		return Math.round(value * 100.0f) / 100.0f;
	}

	private net.monkeyman42001.cannacraft.component.LineageNode buildLineageTree(
			net.monkeyman42001.cannacraft.component.LineageNode leftNode,
			net.monkeyman42001.cannacraft.component.LineageNode rightNode
	) {
		return new net.monkeyman42001.cannacraft.component.LineageNode(breedName, java.util.List.of(leftNode, rightNode));
	}
}
