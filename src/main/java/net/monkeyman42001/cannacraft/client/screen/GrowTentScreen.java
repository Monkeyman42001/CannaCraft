package net.monkeyman42001.cannacraft.client.screen;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.EditBox;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

import net.monkeyman42001.cannacraft.CannaCraft;
import net.monkeyman42001.cannacraft.menu.GrowTentMenu;

public class GrowTentScreen extends AbstractContainerScreen<GrowTentMenu> {
	private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(CannaCraft.MOD_ID, "textures/screens/growtent_gui.png");
	private EditBox nameBox;

	public GrowTentScreen(GrowTentMenu menu, Inventory inventory, Component title) {
		super(menu, inventory, title);
		this.imageWidth = 176;
		this.imageHeight = 166;
	}

	@Override
	protected void init() {
		super.init();
		int x = leftPos + 8;
		int y = topPos + 70;
		nameBox = new EditBox(font, x, y, 160, 12, Component.literal("Name"));
		nameBox.setMaxLength(64);
		nameBox.setBordered(true);
		nameBox.setVisible(true);
		addRenderableWidget(nameBox);
		setInitialFocus(nameBox);
	}

	@Override
	public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
		renderBackground(guiGraphics, mouseX, mouseY, partialTick);
		super.render(guiGraphics, mouseX, mouseY, partialTick);
		if (nameBox != null) {
			nameBox.render(guiGraphics, mouseX, mouseY, partialTick);
		}
		renderTooltip(guiGraphics, mouseX, mouseY);
	}

	@Override
	protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
		RenderSystem.setShaderTexture(0, TEXTURE);
		guiGraphics.blit(TEXTURE, leftPos, topPos, 0, 0, imageWidth, imageHeight, imageWidth, imageHeight);
	}

	@Override
	protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
	}

	@Override
	public boolean keyPressed(int key, int scanCode, int modifiers) {
		if (nameBox != null && nameBox.keyPressed(key, scanCode, modifiers)) {
			return true;
		}
		return super.keyPressed(key, scanCode, modifiers);
	}

	@Override
	public boolean charTyped(char codePoint, int modifiers) {
		if (nameBox != null && nameBox.charTyped(codePoint, modifiers)) {
			return true;
		}
		return super.charTyped(codePoint, modifiers);
	}

	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int button) {
		if (nameBox != null && nameBox.mouseClicked(mouseX, mouseY, button)) {
			return true;
		}
		return super.mouseClicked(mouseX, mouseY, button);
	}
}
