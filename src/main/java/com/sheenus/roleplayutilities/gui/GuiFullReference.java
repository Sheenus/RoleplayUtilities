package com.sheenus.roleplayutilities.gui;

import java.io.IOException;

import com.sheenus.roleplayutilities.RoleplayUtilities;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;

public class GuiFullReference extends GuiScreen {

	protected String title = I18n.format("reference.home.title");
	private GuiFullReferenceList referenceList;
	
	GuiFullReference() { }
	
	@Override
    public void initGui() {
		this.referenceList = new GuiFullReferenceList(this, this.mc);
		
        this.buttonList.add(new GuiButton(0, this.width/2 - 100, (this.height - (this.height/12 + 10)), /*this.width/2, 20,*/ I18n.format("reference.close")));
    }
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
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
	
	@Override
	public void handleMouseInput() throws IOException {
        this.referenceList.handleMouseInput();
        super.handleMouseInput();
    }
	
	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		if (mouseButton != 0 || !this.referenceList.mouseClicked(mouseX, mouseY, mouseButton)) { super.mouseClicked(mouseX, mouseY, mouseButton); }
	}
	
	@Override
	protected void mouseReleased(int mouseX, int mouseY, int state) {
		if (state != 0 || !this.referenceList.mouseReleased(mouseX, mouseY, state)) { super.mouseReleased(mouseX, mouseY, state); }
	}
}
