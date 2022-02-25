package com.sheenus.roleplayutilities.gui;

import org.apache.commons.lang3.ArrayUtils;

import com.sheenus.roleplayutilities.RoleplayUtilities;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiKeyBindingList;
import net.minecraft.client.gui.GuiListExtended;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class GuiNBTReferenceList extends GuiListExtended {	
	
	private int listWidth = this.width;
	private int listHeight = this.height;
	
	private final GuiNBTFullReference fullReferenceGUI;
	private final Minecraft mc;
	private final GuiListExtended.IGuiListEntry[] listEntries;
	private int maxListLabelWidth;
	
	private final MainRefElement noTooltipInfo = new MainRefElement("No Tooltip", "");
	private final MainRefElement messageInfo = new MainRefElement("Message", "");
	private final MainRefElement potionEffectsInfo = new MainRefElement("Potion Effects", "");
	private final MainRefElement whileUseEffectsInfo = new MainRefElement("While-in-use Effects", "");
	private final MainRefElement weaponEffectInfo = new MainRefElement("Weapon Effects", "");
	private final MainRefElement setRedstoneActivateInfo = new MainRefElement("set Redstone On-activate", "");

	public GuiNBTReferenceList(GuiNBTFullReference fullReference, Minecraft mcIn) {
		super(mcIn, fullReference.width, fullReference.height, 50, fullReference.height - 40, 50);
		this.fullReferenceGUI = fullReference;
		this.mc = mcIn;
		MainRefElement[] refElements = ArrayUtils.addAll(new MainRefElement[] {this.noTooltipInfo, this.messageInfo, this.potionEffectsInfo, this.whileUseEffectsInfo, this.weaponEffectInfo, this.setRedstoneActivateInfo});
		listEntries = new GuiListExtended.IGuiListEntry[refElements.length];
		int i = 0;
		for (MainRefElement refElement : refElements) {
			
			int stringLength = mcIn.fontRenderer.getStringWidth(refElement.getRefDesc());
			if (stringLength > maxListLabelWidth) {
				maxListLabelWidth = stringLength;
			}
			
			listEntries[i++] = new ReferenceEntry(refElement);
		}
		this.registerScrollButtons(7, 8);
		
		RoleplayUtilities.utilitiesLog.info("getContentHeight value: " + this.getContentHeight());
		RoleplayUtilities.utilitiesLog.info("getMaxScroll value: " + this.getMaxScroll());
	}

	@Override
	public IGuiListEntry getListEntry(int index) {
		return this.listEntries[index];
	}

	@Override
	protected int getSize() {
		return this.listEntries.length;
	}
	
	public int getListWidth() {
        return super.getListWidth() + 32;
    }
	
	protected int getScrollBarX() {
		return this.width - 30;
	}
	
	@SideOnly(Side.CLIENT)
	public class ReferenceEntry implements GuiListExtended.IGuiListEntry {
		
		private final String refDesc;
		private final String refNote;
		private final GuiButton refInfoButton;
		
		private ReferenceEntry(MainRefElement refElement) {
			this.refDesc = refElement.getRefDesc();
			this.refNote = refElement.getNotation();
			this.refInfoButton = new GuiButton(0, 0, 0, (listWidth/4), 20, this.refDesc);
		}

		@Override
		public void drawEntry(int slotIndex, int x, int y, int listWidth, int slotHeight, int mouseX, int mouseY, boolean isSelected, float partialTicks) {
			GuiNBTReferenceList.this.mc.fontRenderer.drawString(this.refDesc, x + 90 - GuiNBTReferenceList.this.maxListLabelWidth, y + slotHeight/2 - GuiNBTReferenceList.this.mc.fontRenderer.FONT_HEIGHT/2, 16777215);
			this.refInfoButton.x = x + (listWidth/3*2);
            this.refInfoButton.y = y + slotHeight/2 - refInfoButton.height/2;
            this.refInfoButton.displayString = this.refDesc;
            refInfoButton.drawButton(GuiNBTReferenceList.this.mc, mouseX, mouseY, partialTicks);
		}
		
		@Override
		public void updatePosition(int slotIndex, int x, int y, float partialTicks) {
		}

		@Override
		public boolean mousePressed(int slotIndex, int mouseX, int mouseY, int mouseEvent, int relativeX, int relativeY) {
			return false;
		}

		@Override
		public void mouseReleased(int slotIndex, int x, int y, int mouseEvent, int relativeX, int relativeY) {
			
		}
		
	}
	

}
