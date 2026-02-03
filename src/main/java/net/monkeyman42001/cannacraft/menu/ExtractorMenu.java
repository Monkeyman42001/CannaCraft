package net.monkeyman42001.cannacraft.menu;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.monkeyman42001.cannacraft.item.CannacraftItems;
import net.monkeyman42001.cannacraft.registry.CannacraftMenus;
import net.minecraft.network.RegistryFriendlyByteBuf;

public class ExtractorMenu extends AbstractContainerMenu {
	private static final int SLOT_INPUT = 0;
	private static final int SLOT_BOTTLE = 1;
	private static final int SLOT_OUTPUT = 2;
	private static final int SLOT_COUNT = 3;
	private static final int INV_START = 3;
	private static final int INV_END = 30;
	private static final int HOTBAR_START = 30;
	private static final int HOTBAR_END = 39;

	private final Container container;
	private final ContainerData data;

	public ExtractorMenu(int id, Inventory playerInventory, Container container, ContainerData data) {
		super(CannacraftMenus.EXTRACTOR.get(), id);
		this.container = container;
		this.data = data;

		addExtractorSlots();
		addPlayerSlots(playerInventory);
		addDataSlots(data);
	}

	public ExtractorMenu(int id, Inventory playerInventory) {
		this(id, playerInventory, new net.minecraft.world.SimpleContainer(SLOT_COUNT), new net.minecraft.world.inventory.SimpleContainerData(4));
	}

	public ExtractorMenu(int id, Inventory playerInventory, RegistryFriendlyByteBuf buf) {
		this(id, playerInventory, new net.minecraft.world.SimpleContainer(SLOT_COUNT), new net.minecraft.world.inventory.SimpleContainerData(4));
		buf.readBlockPos();
	}

	private void addExtractorSlots() {
		addSlot(new Slot(container, SLOT_INPUT, 56, 17) {
			@Override
			public boolean mayPlace(ItemStack stack) {
				return stack.getItem() == CannacraftItems.NUG.get();
			}
		});
		addSlot(new Slot(container, SLOT_BOTTLE, 56, 53) {
			@Override
			public boolean mayPlace(ItemStack stack) {
				return stack.is(Items.GLASS_BOTTLE);
			}
		});
		addSlot(new net.monkeyman42001.cannacraft.menu.slot.ExtractorResultSlot(container, SLOT_OUTPUT, 116, 35));
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

	public int getCookProgressScaled(int pixels) {
		int cookTime = data.get(2);
		int cookTotal = data.get(3);
		if (cookTotal == 0 || cookTime == 0) {
			return 0;
		}
		return cookTime * pixels / cookTotal;
	}

	public boolean isCooking() {
		return data.get(2) > 0;
	}

	public int getLitProgressScaled(int pixels) {
		int lit = data.get(0);
		int litTotal = data.get(1);
		if (litTotal == 0 || lit == 0) {
			return 0;
		}
		return lit * pixels / litTotal;
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
				if (stack.getItem() == CannacraftItems.NUG.get()) {
					if (!moveItemStackTo(stack, SLOT_INPUT, SLOT_INPUT + 1, false)) {
						return ItemStack.EMPTY;
					}
				} else if (stack.is(Items.GLASS_BOTTLE)) {
					if (!moveItemStackTo(stack, SLOT_BOTTLE, SLOT_BOTTLE + 1, false)) {
						return ItemStack.EMPTY;
					}
				} else if (stack.getItem() == CannacraftItems.EXTRACT.get()) {
					if (!moveItemStackTo(stack, INV_START, HOTBAR_END, false)) {
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
