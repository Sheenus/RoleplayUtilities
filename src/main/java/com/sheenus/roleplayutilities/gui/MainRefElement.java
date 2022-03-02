package com.sheenus.roleplayutilities.gui;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;


@SideOnly(Side.CLIENT)
public class MainRefElement {

	private String refDesc;
	private String notation;
	private int id;
	
	MainRefElement(String refDesc, String notation, int id) {
		
		this.refDesc = refDesc;
		this.notation = notation;
		this.id = id;
	}
	
	public String getRefDesc() {
		return this.refDesc;
	}
	
	public String getNotation() {
		return this.notation;
	}
	
	public int getId() {
		return this.id;
	}
}
