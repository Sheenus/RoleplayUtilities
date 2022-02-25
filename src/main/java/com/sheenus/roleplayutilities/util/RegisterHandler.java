package com.sheenus.roleplayutilities.util;

import com.sheenus.roleplayutilities.init.RegisterRecipes;

import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid=Reference.MOD_ID)
public class RegisterHandler {

	@SubscribeEvent
	public static void RegisterRecipes(final RegistryEvent.Register<IRecipe> event) {
		RegisterRecipes.initCraftingRecipes(event.getRegistry());
	}
}
