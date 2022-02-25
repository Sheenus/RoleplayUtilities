package com.sheenus.roleplayutilities;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

import com.sheenus.roleplayutilities.proxy.IProxy;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.sheenus.roleplayutilities.RoleplayUtilities;
import com.sheenus.roleplayutilities.util.Reference;

@Mod(modid = Reference.MOD_ID, 
name = Reference.NAME, 
version = Reference.VERSION, 
acceptedMinecraftVersions = Reference.ACCEPTED_VERSIONS, 
dependencies = Reference.DEPENDANCY)

public class RoleplayUtilities
{
	@Instance(Reference.MOD_ID)
	public static RoleplayUtilities instance = new RoleplayUtilities();
	
	@SidedProxy(clientSide = Reference.CLIENT_PROXY_CLASS, serverSide = Reference.SERVER_PROXY_CLASS)
	public static IProxy proxy;
	
	public static final Logger utilitiesLog = LogManager.getLogger("RoleplayUtilities");

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        //logger = event.getModLog();
    	proxy.preInit(event);
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
    	proxy.init(event);
    }
    
    @EventHandler
	public static void postInit(FMLPostInitializationEvent event) {
		
		proxy.postInit(event);
		utilitiesLog.info("just checking if the mod's loading...");
	}
	
	@EventHandler
	public void serverStarting(FMLServerStartingEvent event) {
		
		proxy.serverStarting(event);
	}
}
