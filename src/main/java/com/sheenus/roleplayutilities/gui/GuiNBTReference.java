package com.sheenus.roleplayutilities.gui;

import org.lwjgl.opengl.GL11;

import com.sheenus.roleplayutilities.util.Reference;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ResourceLocation;

public class GuiNBTReference extends GuiScreen {
	
	private GuiButton closeButton;
    private int backgroundWidth = 256;
    private int backgroundHeight = 256;
    
    @Override
    public void initGui() {
        this.buttonList.add(this.closeButton = new GuiButton(0, (this.width - this.backgroundWidth + 100) / 2, (this.height + this.backgroundHeight - 60) / 2, this.backgroundWidth - 100, 20, "Close"));
    }
	
    @Override
    public void drawScreen(int mouseX, int mouseY, float par3) {
        int x = (this.width - this.backgroundWidth) / 2;
        int y = (this.height - this.backgroundHeight) / 2 - this.closeButton.height;

        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.renderEngine.bindTexture(new ResourceLocation(Reference.MOD_ID, "textures/gui/reference.png"));
        this.drawTexturedModalRect(x, y, 0, 0, this.backgroundWidth, this.backgroundHeight);
        super.drawScreen(mouseX, mouseY, par3);
    }
    
    @Override
    protected void actionPerformed(GuiButton button) {
        if (button.id == 0) {
            this.mc.player.closeScreen();
        }
    }
    
    @Override
    public boolean doesGuiPauseGame() {
        return true;
    }
}
