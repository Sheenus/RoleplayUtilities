package com.sheenus.roleplayutilities.proxy;

import com.sheenus.roleplayutilities.RoleplayUtilities;
import com.sheenus.roleplayutilities.gui.GuiHandler;
import com.sheenus.roleplayutilities.init.RegisterKeybinds;
import com.sheenus.roleplayutilities.util.Reference;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

@EventBusSubscriber(value = Side.CLIENT, modid = Reference.MOD_ID)
public class ClientProxy implements IProxy {

	@Override
	public void preInit(FMLPreInitializationEvent event) {
		
	}

	@Override
	public void init(FMLInitializationEvent event) {
		NetworkRegistry.INSTANCE.registerGuiHandler(RoleplayUtilities.instance, new GuiHandler());
		MinecraftForge.EVENT_BUS.register(new RegisterKeybinds());
	}

	@Override
	public void postInit(FMLPostInitializationEvent event) {
		
	}

	@Override
	public void serverStarting(FMLServerStartingEvent event) {
		
	}

	@Override
	public EntityPlayer getPlayerEntityFromContext(MessageContext context) {
		
		return (context.side.isClient() ? Minecraft.getMinecraft().player : context.getServerHandler().player);
	}

	@Override
	public void registerItemRenderer(Item item, int meta, String id) {
		
		ModelLoader.setCustomModelResourceLocation(item, meta, new ModelResourceLocation(item.getRegistryName(), id));
		
	}

}
