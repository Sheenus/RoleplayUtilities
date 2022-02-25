package com.sheenus.roleplayutilities.gui;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;


@SideOnly(Side.CLIENT)
public class MainRefElement {

	private String refDesc;
	private String notation;
	
	MainRefElement(String refDesc, String notation) {
		
		this.refDesc = refDesc;
		this.notation = notation;
	}
	
	public String getRefDesc() {
		return this.refDesc;
	}
	
	public String getNotation() {
		return this.notation;
	}
}
