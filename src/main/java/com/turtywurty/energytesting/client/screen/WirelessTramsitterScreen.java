package com.turtywurty.energytesting.client.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.turtywurty.energytesting.EnergyTesting;
import com.turtywurty.energytesting.common.container.WirelessTransmitterContainer;

import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class WirelessTramsitterScreen extends ContainerScreen<WirelessTransmitterContainer> {

	private static final ResourceLocation TEXTURE = new ResourceLocation(EnergyTesting.MOD_ID,
			"textures/gui/wireless_transmitter.png");

	public WirelessTramsitterScreen(WirelessTransmitterContainer screenContainer, PlayerInventory inv,
			ITextComponent titleIn) {
		super(screenContainer, inv, titleIn);
		this.guiLeft = 0;
		this.guiTop = 0;
		this.xSize = 176;
		this.ySize = 166;
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		this.renderBackground();
		RenderSystem.color4f(1.0f, 1.0f, 1.0f, 1.0f);
		getMinecraft().getTextureManager().bindTexture(TEXTURE);
		this.blit(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		super.drawGuiContainerForegroundLayer(mouseX, mouseY);
		this.font.drawString(this.title.getFormattedText(), 8.0F, 6.0F, 0x404040);
		this.font.drawString(this.playerInventory.getDisplayName().getFormattedText(), 8.0F,
				(float) (this.ySize - 96 + 2), 0x404040);
	}
}
