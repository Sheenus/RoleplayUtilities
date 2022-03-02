package com.sheenus.roleplayutilities.gui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiListExtended;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class GuiPotionEffectsList extends GuiListExtended {
	
	private final GuiPotionEffects potionEffectsGUI;
	private final Minecraft mc;
	private final IGuiListEntry[] listEntries;
	private int maxTextWidth;
	private final String description = I18n.format("reference.potioneffects.description");
	
	public GuiPotionEffectsList(GuiPotionEffects potionEffects, Minecraft mcIn) {
		super(mcIn, potionEffects.width, potionEffects.height, Math.max(potionEffects.height/5, 55), potionEffects.height - potionEffects.height/5, 15);
		this.potionEffectsGUI = potionEffects;
		this.mc = mcIn;
		maxTextWidth = potionEffects.width - ((potionEffects.width - (getScrollBarX() - 10))*2);
		
		// List<String> textLineList = mc.fontRenderer.listFormattedStringToWidth(description, maxTextWidth);
		
		List<String> descriptionSplit = Arrays.asList(description.split("<BR>"));
		/*for (int i = 0; i < descriptionSplit.size(); i++) {
			RoleplayUtilities.utilitiesLog.info(descriptionSplit.get(i));
		}*/
		List<String> textLineList = new ArrayList<String>();
		for (int i = 0; i < descriptionSplit.size(); i++) {
			List<String> paraLineList = mc.fontRenderer.listFormattedStringToWidth(descriptionSplit.get(i), maxTextWidth);
			for (int j = 0; j < paraLineList.size(); j++) {
				textLineList.add(paraLineList.get(j));
			}
		} 
		
		listEntries = new IGuiListEntry[textLineList.size()];
		// RoleplayUtilities.utilitiesLog.info("textLineList is " + textLineList.size() + " large. listEntries is " + listEntries.length + " large.");
		
		for (int i = 0; i < textLineList.size(); i++) {
			listEntries[i] = new textEntry(textLineList.get(i));
			// textEntry listEntry = (textEntry)listEntries[i];
			// RoleplayUtilities.utilitiesLog.info(listEntry.text);
		}
	}

	@Override
	public IGuiListEntry getListEntry(int index) {
		return this.listEntries[index];
	}

	@Override
	protected int getSize() {
		return listEntries.length;
	}
	
	@Override
	protected int getScrollBarX() {
		return this.width - 30;
	}
	
	@Override
	public int getListWidth() {
        return super.getListWidth() + 32;
    }
	
	@SideOnly(Side.CLIENT)
	public class textEntry implements IGuiListEntry {
		
		private String text;
		
		private textEntry(String textLine) {
			this.text = textLine;
		}

		@Override
		public void drawEntry(int slotIndex, int x, int y, int listWidth, int slotHeight, int mouseX, int mouseY, boolean isSelected, float partialTicks) {
			mc.fontRenderer.drawString(this.text, potionEffectsGUI.width/2 - maxTextWidth/2, y, 16777215);
			
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

