package net.monkeyman42001.cannacraft.menu;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

import net.monkeyman42001.cannacraft.item.CannacraftItems;
import net.monkeyman42001.cannacraft.registry.CannacraftMenus;

public class GrowTentMenu extends AbstractContainerMenu {
	private static final int SLOT_SEED_LEFT = 0;
	private static final int SLOT_SEED_RIGHT = 1;
	private static final int SLOT_OUTPUT = 2;
	private static final int SLOT_COUNT = 3;
	private static final int INV_START = 3;
	private static final int INV_END = 30;
	private static final int HOTBAR_START = 30;
	private static final int HOTBAR_END = 39;

	private final Container container;

	public GrowTentMenu(int id, Inventory playerInventory, Container container) {
		super(CannacraftMenus.GROW_TENT.get(), id);
		this.container = container;

		addGrowTentSlots();
		addPlayerSlots(playerInventory);
	}

	public GrowTentMenu(int id, Inventory playerInventory) {
		this(id, playerInventory, new net.minecraft.world.SimpleContainer(SLOT_COUNT));
	}

	public GrowTentMenu(int id, Inventory playerInventory, RegistryFriendlyByteBuf buf) {
		this(id, playerInventory, new net.minecraft.world.SimpleContainer(SLOT_COUNT));
		buf.readBlockPos();
	}

	private void addGrowTentSlots() {
		addSlot(new Slot(container, SLOT_SEED_LEFT, 43, 14) {
			@Override
			public boolean mayPlace(ItemStack stack) {
				return stack.getItem() == CannacraftItems.CANNABIS_SEED.get();
			}
		});
		addSlot(new Slot(container, SLOT_SEED_RIGHT, 116, 14) {
			@Override
			public boolean mayPlace(ItemStack stack) {
				return stack.getItem() == CannacraftItems.CANNABIS_SEED.get();
			}
		});
		addSlot(new Slot(container, SLOT_OUTPUT, 80, 48) {
			@Override
			public boolean mayPlace(ItemStack stack) {
				return false;
			}
		});
	}

	private void addPlayerSlots(Inventory playerInventory) {
		for (int row = 0; row < 3; row++) {
			for (int col = 0; col < 9; col++) {
				addSlot(new Slot(playerInventory, col + row * 9 + 9, 8 + col * 18, 84 + row * 18));
			}
		}
		for (int col = 0; col < 9; col++) {
			addSlot(new Slot(playerInventory, col, 8 + col * 18, 142));
		}
	}

	@Override
	public boolean stillValid(Player player) {
		return container.stillValid(player);
	}

	@Override
	public ItemStack quickMoveStack(Player player, int index) {
		ItemStack itemstack = ItemStack.EMPTY;
		Slot slot = slots.get(index);
		if (slot != null && slot.hasItem()) {
			ItemStack stack = slot.getItem();
			itemstack = stack.copy();

			if (index == SLOT_OUTPUT) {
				if (!moveItemStackTo(stack, INV_START, HOTBAR_END, false)) {
					return ItemStack.EMPTY;
				}
				slot.onQuickCraft(stack, itemstack);
			} else if (index >= INV_START) {
				if (stack.getItem() == CannacraftItems.CANNABIS_SEED.get()) {
					if (!moveItemStackTo(stack, SLOT_SEED_LEFT, SLOT_SEED_RIGHT + 1, false)) {
						return ItemStack.EMPTY;
					}
				} else if (index < HOTBAR_START) {
					if (!moveItemStackTo(stack, HOTBAR_START, HOTBAR_END, false)) {
						return ItemStack.EMPTY;
					}
				} else if (!moveItemStackTo(stack, INV_START, HOTBAR_START, false)) {
					return ItemStack.EMPTY;
				}
			} else if (!moveItemStackTo(stack, INV_START, HOTBAR_END, false)) {
				return ItemStack.EMPTY;
			}

			if (stack.isEmpty()) {
				slot.set(ItemStack.EMPTY);
			} else {
				slot.setChanged();
			}

			if (stack.getCount() == itemstack.getCount()) {
				return ItemStack.EMPTY;
			}

			slot.onTake(player, stack);
		}
		return itemstack;
	}
}
