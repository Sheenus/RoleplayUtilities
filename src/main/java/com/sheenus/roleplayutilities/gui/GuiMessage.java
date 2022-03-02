package com.sheenus.roleplayutilities.gui;

import java.io.IOException;

import com.sheenus.roleplayutilities.util.UtilitiesUtils;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.TextFormatting;

public class GuiMessage extends GuiScreen {
	
	private final GuiScreen parentScreen;
	private GuiMessageList messageList;
	private final String title = I18n.format("reference.message.title");
	private String template = I18n.format("reference.message.template");
	
	protected GuiButton btnCopyTemplate;
	private boolean textCopied;
	
	public GuiMessage(GuiScreen screen) {
		this.parentScreen = screen;
	}
	
	
	@Override
    public void initGui() {
		this.textCopied = false;
		this.messageList = new GuiMessageList(this, this.mc);
		GuiButton closeButton = new GuiButton(0, this.width/2 - 100, (this.height - (this.height/10 + 10)),I18n.format("reference.close"));
        this.buttonList.add(closeButton);
        
        this.btnCopyTemplate = new GuiButton(1, this.width/2 - 100, Math.max(this.height/10, 30), template);
        btnCopyTemplate.displayString = UtilitiesUtils.fitStringToButton(this.mc, template, btnCopyTemplate);
        this.buttonList.add(btnCopyTemplate);
    }
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        this.messageList.drawScreen(mouseX, mouseY, partialTicks);
        this.drawCenteredString(this.fontRenderer, this.title, this.width/2, Math.max(this.height/20, 15), 16777215);
        super.drawScreen(mouseX, mouseY, partialTicks);
        
        if (btnCopyTemplate.isMouseOver() && textCopied == true) {
        	this.drawHoveringText(TextFormatting.YELLOW + I18n.format("reference.submenu.copybutton.toast"), mouseX, mouseY);
        } else if (btnCopyTemplate.isMouseOver()) {
        	this.drawHoveringText(I18n.format("reference.submenu.copybutton.tooltip"), mouseX, mouseY);
        }
    }
	
	@Override
    protected void actionPerformed(GuiButton button) {
        if (button.id == 0) {
        	this.mc.displayGuiScreen(this.parentScreen);
        }
        if (button.id == 1) {
        	GuiScreen.setClipboardString(this.template);
        	textCopied = true;
        }
    }
	
	@Override
	public void handleMouseInput() throws IOException {
        super.handleMouseInput();
        this.messageList.handleMouseInput();
    }
	
	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		this.messageList.mouseClicked(mouseX, mouseY, mouseButton);
		super.mouseClicked(mouseX, mouseY, mouseButton);
	}
	
	@Override
	protected void mouseReleased(int mouseX, int mouseY, int state) {
		this.messageList.mouseReleased(mouseX, mouseY, state);
		super.mouseReleased(mouseX, mouseY, state);
	}
}
