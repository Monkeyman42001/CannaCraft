package net.monkeyman42001.cannacraft.client.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.monkeyman42001.cannacraft.CannaCraft;
import net.monkeyman42001.cannacraft.menu.ExtractorMenu;

public class ExtractorScreen extends AbstractContainerScreen<ExtractorMenu> {
	private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath("minecraft", "textures/gui/container/furnace.png");

	public ExtractorScreen(ExtractorMenu menu, Inventory inventory, Component title) {
		super(menu, inventory, title);
		this.imageWidth = 176;
		this.imageHeight = 166;
	}

	@Override
	public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
		renderBackground(guiGraphics, mouseX, mouseY, partialTick);
		super.render(guiGraphics, mouseX, mouseY, partialTick);
		renderTooltip(guiGraphics, mouseX, mouseY);
	}

	@Override
	protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
		RenderSystem.setShaderTexture(0, TEXTURE);
		int x = (width - imageWidth) / 2;
		int y = (height - imageHeight) / 2;
		guiGraphics.blit(TEXTURE, x, y, 0, 0, imageWidth, imageHeight);

		if (menu.isCooking()) {
			int lit = menu.getLitProgressScaled(13);
			drawFlame(guiGraphics, x + 56, y + 36, lit);
		}
		int progress = menu.getCookProgressScaled(24);
		drawArrow(guiGraphics, x + 79, y + 34, progress);
	}

	private void drawArrow(GuiGraphics guiGraphics, int x, int y, int progress) {
		if (progress <= 0) {
			return;
		}
		int body = Math.max(0, progress - 4);
		if (body > 0) {
			guiGraphics.fill(x, y + 7, x + body, y + 9, 0xFFB86B00);
		}
		int head = Math.min(4, progress);
		guiGraphics.fill(x + body, y + 6, x + body + head, y + 10, 0xFFB86B00);
		if (head > 2) {
			guiGraphics.fill(x + body + head - 1, y + 7, x + body + head, y + 9, 0xFFB86B00);
		}
	}

	private void drawFlame(GuiGraphics guiGraphics, int x, int y, int lit) {
		if (lit <= 0) {
			return;
		}
		int height = Math.min(13, lit);
		int baseStartY = y + 13 - height;

		// Base (full width)
		int baseHeight = Math.min(5, height);
		guiGraphics.fill(x, y + 13 - baseHeight, x + 14, y + 13, 0xFFB86B00);

		// Mid (narrower)
		int midHeight = Math.min(4, Math.max(0, height - baseHeight));
		if (midHeight > 0) {
			guiGraphics.fill(x + 2, y + 13 - baseHeight - midHeight, x + 12, y + 13 - baseHeight, 0xFFB86B00);
		}

		// Top (tip)
		int topHeight = Math.min(4, Math.max(0, height - baseHeight - midHeight));
		if (topHeight > 0) {
			guiGraphics.fill(x + 4, baseStartY, x + 10, baseStartY + topHeight, 0xFFB86B00);
		}
	}
}
