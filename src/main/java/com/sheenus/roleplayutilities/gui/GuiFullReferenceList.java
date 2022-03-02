package com.sheenus.roleplayutilities.gui;

import org.apache.commons.lang3.ArrayUtils;
import com.sheenus.roleplayutilities.util.UtilitiesUtils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiListExtended;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class GuiFullReferenceList extends GuiListExtended {	
	
	private int listWidth = this.width;
	private int listHeight = this.height;
	
	private final GuiFullReference fullReferenceGUI;
	private final Minecraft mc;
	private final GuiListExtended.IGuiListEntry[] listEntries;
	private int maxListLabelWidth;
	
	private final MainRefElement noTooltipRef = new MainRefElement(I18n.format("reference.home.notooltip"), I18n.format("reference.notooltip.template"), 10);
	private final MainRefElement messageRef = new MainRefElement(I18n.format("reference.home.message"), I18n.format("reference.message.template"), 11);
	private final MainRefElement potionEffectsRef = new MainRefElement(I18n.format("reference.home.potioneffects"), I18n.format("reference.potioneffects.template"), 12);
	private final MainRefElement whileUseEffectsRef = new MainRefElement(I18n.format("reference.home.whileuseeffects"), I18n.format("reference.whileuseeffects.template"), 13);
	private final MainRefElement weaponEffectsRef = new MainRefElement(I18n.format("reference.home.weaponeffects"), I18n.format("reference.weaponeffects.template"), 14);
	private final MainRefElement setRedstoneActivateRef = new MainRefElement(I18n.format("reference.home.setredstoneactivate"), I18n.format("reference.setredstoneactivate.template"), 15);

	public GuiFullReferenceList(GuiFullReference fullReference, Minecraft mcIn) {
		super(mcIn, fullReference.width, fullReference.height, 35, fullReference.height - fullReference.height/6, 25);
		this.fullReferenceGUI = fullReference;
		this.mc = mcIn;
		MainRefElement[] refElements = ArrayUtils.addAll(new MainRefElement[] {this.noTooltipRef, this.messageRef, this.potionEffectsRef, this.whileUseEffectsRef, this.weaponEffectsRef, this.setRedstoneActivateRef});
		listEntries = new GuiListExtended.IGuiListEntry[refElements.length];
		int i = 0;
		for (MainRefElement refElement : refElements) {
			
			int stringLength = mcIn.fontRenderer.getStringWidth(refElement.getRefDesc());
			if (stringLength > maxListLabelWidth) {
				maxListLabelWidth = stringLength;
			}
			
			listEntries[i++] = new ReferenceEntry(refElement);
		}
		
		//RoleplayUtilities.utilitiesLog.info("getContentHeight value: " + this.getContentHeight());
		//RoleplayUtilities.utilitiesLog.info("getMaxScroll value: " + this.getMaxScroll());
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
		private final GuiButton refButton;
		
		private ReferenceEntry(MainRefElement refElement) {
			this.refDesc = refElement.getRefDesc();
			this.refNote = refElement.getNotation();
			int buttonWidth = 100;
			this.refButton = new GuiButton(refElement.getId(), 0, 0, buttonWidth, 20, GuiFullReferenceList.this.mc.fontRenderer.trimStringToWidth(this.refDesc, buttonWidth));
		}

		@Override
		public void drawEntry(int slotIndex, int x, int y, int listWidth, int slotHeight, int mouseX, int mouseY, boolean isSelected, float partialTicks) {
			mc.fontRenderer.drawString(this.refDesc, GuiFullReferenceList.this.width/2 - maxListLabelWidth, y + (slotHeight/2 - mc.fontRenderer.FONT_HEIGHT/2), 16777215);
			this.refButton.x = x + (listWidth/2);
            this.refButton.y = y + slotHeight/2 - refButton.height/2;
            
            this.refButton.displayString = UtilitiesUtils.fitStringToButton(mc, refNote, refButton);
            
            refButton.drawButton(mc, mouseX, mouseY, partialTicks);
		}
		
		@Override
		public void updatePosition(int slotIndex, int x, int y, float partialTicks) {
		}

		@Override
		public boolean mousePressed(int slotIndex, int mouseX, int mouseY, int mouseEvent, int relativeX, int relativeY) {
			if (this.refButton.mousePressed(GuiFullReferenceList.this.mc, mouseX, mouseY)) {
				switch (refButton.id) {
					case 10: { mc.displayGuiScreen(new GuiNoTooltip(GuiFullReferenceList.this.fullReferenceGUI)); break; }
					case 11: { mc.displayGuiScreen(new GuiMessage(GuiFullReferenceList.this.fullReferenceGUI)); break; }
					case 12: { mc.displayGuiScreen(new GuiPotionEffects(GuiFullReferenceList.this.fullReferenceGUI)); break; }
					case 13: { mc.displayGuiScreen(new GuiWhileUseEffects(GuiFullReferenceList.this.fullReferenceGUI)); break; }
					case 14: { mc.displayGuiScreen(new GuiWeaponEffects(GuiFullReferenceList.this.fullReferenceGUI)); break;}
					case 15: { mc.displayGuiScreen(new GuiSetRedstoneActivate(GuiFullReferenceList.this.fullReferenceGUI)); break;}
				default: { break; }
				}
				// RoleplayUtilities.utilitiesLog.info("button pressed!");
				return true;
			}
			else { return false; }
		}

		@Override
		public void mouseReleased(int slotIndex, int x, int y, int mouseEvent, int relativeX, int relativeY) {
			this.refButton.mouseReleased(x, y);
		}
		
	}
	

}
