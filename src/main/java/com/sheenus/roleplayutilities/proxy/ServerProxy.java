package com.sheenus.roleplayutilities.proxy;

import com.sheenus.roleplayutilities.RoleplayUtilities;
import com.sheenus.roleplayutilities.gui.GuiHandler;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class ServerProxy implements IProxy {

	@Override
	public void preInit(FMLPreInitializationEvent event) {
		
	}

	@Override
	public void init(FMLInitializationEvent event) {
		NetworkRegistry.INSTANCE.registerGuiHandler(RoleplayUtilities.instance, new GuiHandler());
	}

	@Override
	public void postInit(FMLPostInitializationEvent event) {
		
	}

	@Override
	public void serverStarting(FMLServerStartingEvent event) {
		
	}

	@Override
	public EntityPlayer getPlayerEntityFromContext(MessageContext context) {
		
		return context.getServerHandler().player;
	}

	@Override
	public void registerItemRenderer(Item item, int meta, String id) {
		
	}

}
