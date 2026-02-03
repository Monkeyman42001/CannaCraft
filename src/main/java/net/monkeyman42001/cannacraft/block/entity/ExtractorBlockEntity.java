package net.monkeyman42001.cannacraft.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Container;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.ContainerHelper;
import net.minecraft.core.NonNullList;
import net.monkeyman42001.cannacraft.block.CannacraftModBlockEntities;
import net.monkeyman42001.cannacraft.component.CannacraftDataComponents;
import net.monkeyman42001.cannacraft.component.Strain;
import net.monkeyman42001.cannacraft.item.CannacraftItems;
import net.monkeyman42001.cannacraft.menu.ExtractorMenu;

public class ExtractorBlockEntity extends BlockEntity implements Container, MenuProvider {
	public static final int SLOT_INPUT = 0;
	public static final int SLOT_BOTTLE = 1;
	public static final int SLOT_OUTPUT = 2;
	private static final int COOK_TIME_TOTAL = 200;
	private static final int LIT_TIME_TOTAL = 200;

	private final NonNullList<ItemStack> items = NonNullList.withSize(3, ItemStack.EMPTY);
	private int cookTime;
	private int litTime;
	private int litDuration;

	private final ContainerData dataAccess = new ContainerData() {
		@Override
		public int get(int index) {
			return switch (index) {
				case 0 -> litTime;
				case 1 -> litDuration;
				case 2 -> cookTime;
				case 3 -> COOK_TIME_TOTAL;
				default -> 0;
			};
		}

		@Override
		public void set(int index, int value) {
			switch (index) {
				case 0 -> litTime = value;
				case 1 -> litDuration = value;
				case 2 -> cookTime = value;
				default -> {
				}
			}
		}

		@Override
		public int getCount() {
			return 4;
		}
	};

	public ExtractorBlockEntity(BlockPos pos, BlockState state) {
		super(CannacraftModBlockEntities.EXTRACTOR.get(), pos, state);
	}

	public void serverTick() {
		ItemStack input = items.get(SLOT_INPUT);
		ItemStack bottle = items.get(SLOT_BOTTLE);
		ItemStack output = items.get(SLOT_OUTPUT);

		boolean canProcess = !input.isEmpty()
				&& input.getItem() == CannacraftItems.NUG.get()
				&& bottle.is(Items.GLASS_BOTTLE)
				&& canOutputAccept(output, input);

		if (!canProcess) {
			if (cookTime != 0) {
				cookTime = 0;
			}
			litTime = 0;
			litDuration = 0;
			setChanged();
			return;
		}

		litTime = LIT_TIME_TOTAL;
		litDuration = LIT_TIME_TOTAL;
		cookTime++;
		if (cookTime >= COOK_TIME_TOTAL) {
			cookTime = 0;
			craftItem();
		}
		setChanged();
	}

	private boolean canOutputAccept(ItemStack output, ItemStack input) {
		if (output.isEmpty()) {
			return true;
		}
		if (output.getItem() != CannacraftItems.EXTRACT.get() || output.getCount() >= output.getMaxStackSize()) {
			return false;
		}
		Strain inputStrain = input.get(CannacraftDataComponents.STRAIN.get());
		Strain outputStrain = output.get(CannacraftDataComponents.STRAIN.get());
		return inputStrain == null ? outputStrain == null : inputStrain.equals(outputStrain);
	}

	private void craftItem() {
		ItemStack input = items.get(SLOT_INPUT);
		ItemStack bottle = items.get(SLOT_BOTTLE);
		ItemStack output = items.get(SLOT_OUTPUT);
		if (input.isEmpty() || !bottle.is(Items.GLASS_BOTTLE) || !canOutputAccept(output, input)) {
			return;
		}

		ItemStack result = new ItemStack(CannacraftItems.EXTRACT.get());
		Strain strain = input.get(CannacraftDataComponents.STRAIN.get());
		if (strain != null) {
			result.set(CannacraftDataComponents.STRAIN.get(), strain);
		}

		input.shrink(1);
		bottle.shrink(1);

		if (output.isEmpty()) {
			items.set(SLOT_OUTPUT, result);
		} else {
			output.grow(1);
		}
	}

	@Override
	protected void saveAdditional(CompoundTag tag, HolderLookup.Provider provider) {
		super.saveAdditional(tag, provider);
		ContainerHelper.saveAllItems(tag, items, provider);
		tag.putInt("CookTime", cookTime);
		tag.putInt("LitTime", litTime);
		tag.putInt("LitDuration", litDuration);
	}

	@Override
	protected void loadAdditional(CompoundTag tag, HolderLookup.Provider provider) {
		super.loadAdditional(tag, provider);
		ContainerHelper.loadAllItems(tag, items, provider);
		cookTime = tag.getInt("CookTime");
		litTime = tag.getInt("LitTime");
		litDuration = tag.getInt("LitDuration");
	}

	@Override
	public Component getDisplayName() {
		return Component.literal("Extractor");
	}

	@Override
	public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
		return new ExtractorMenu(id, inventory, this, dataAccess);
	}

	public ContainerData getDataAccess() {
		return dataAccess;
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
}
