package com.sheenus.roleplayutilities.init;

import com.sheenus.roleplayutilities.RoleplayUtilities;
import com.sheenus.roleplayutilities.items.crafting.ToolCoatingClearRecipe;
import com.sheenus.roleplayutilities.items.crafting.ToolCoatingRecipe;
import com.sheenus.roleplayutilities.util.Reference;

import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.registries.IForgeRegistry;

public class RegisterRecipes {

	public static final IRecipe RECIPE_TOOLCOATING = new ToolCoatingRecipe().setRegistryName(Reference.MOD_ID + ":tool_coating");
	public static final IRecipe RECIPE_TOOLCOATINGCLEAR = new ToolCoatingClearRecipe().setRegistryName(Reference.MOD_ID + ":tool_coating_clear");
	
	public static void initCraftingRecipes(final IForgeRegistry<IRecipe> iForgeRegistry) {
		iForgeRegistry.register(RECIPE_TOOLCOATING);
		RoleplayUtilities.utilitiesLog.info("registered tool coating recipe at ResourceLocation: " + RECIPE_TOOLCOATING.getRegistryName());
		iForgeRegistry.register(RECIPE_TOOLCOATINGCLEAR);
		RoleplayUtilities.utilitiesLog.info("registered tool coating clear recipe at ResourceLocation: " + RECIPE_TOOLCOATINGCLEAR.getRegistryName());
	}
}
