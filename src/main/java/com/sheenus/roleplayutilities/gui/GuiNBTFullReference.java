package com.sheenus.roleplayutilities.gui;

import com.sheenus.roleplayutilities.RoleplayUtilities;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;

public class GuiNBTFullReference extends GuiScreen {

	protected String title = "Roleplay Utilities NBT tag reference";
	private GuiNBTReferenceList referenceList;
	
	@Override
    public void initGui() {
		this.referenceList = new GuiNBTReferenceList(this, this.mc);
		
        this.buttonList.add(new GuiButton(0, this.width/4, (this.height - Math.max(20, this.height/10)), this.width/2, 20, "Close"));
    }
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        this.drawDefaultBackground();
        this.referenceList.drawScreen(mouseX, mouseY, partialTicks);
        this.drawCenteredString(this.fontRenderer, this.title, this.width / 2, 15, 16777215);
        super.drawScreen(mouseX, mouseY, partialTicks);
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
