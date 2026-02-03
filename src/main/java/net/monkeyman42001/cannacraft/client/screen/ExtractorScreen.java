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
	protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
		RenderSystem.setShaderTexture(0, TEXTURE);
		int x = (width - imageWidth) / 2;
		int y = (height - imageHeight) / 2;
		guiGraphics.blit(TEXTURE, x, y, 0, 0, imageWidth, imageHeight);

		int progress = menu.getCookProgressScaled(24);
		guiGraphics.blit(TEXTURE, x + 79, y + 34, 176, 14, progress + 1, 16);
	}
}
