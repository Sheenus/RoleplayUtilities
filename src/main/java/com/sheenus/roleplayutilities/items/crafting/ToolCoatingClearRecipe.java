package com.sheenus.roleplayutilities.items.crafting;

import java.util.List;

import com.google.common.collect.Lists;
import com.sheenus.roleplayutilities.RoleplayUtilities;
import com.sheenus.roleplayutilities.util.UtilitiesUtils;

import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;

public class ToolCoatingClearRecipe extends net.minecraftforge.registries.IForgeRegistryEntry.Impl<IRecipe> implements IRecipe {

	@Override
	public boolean matches(InventoryCrafting inv, World worldIn) {
		List<ItemStack> craftingItemList = Lists.<ItemStack>newArrayList();
		
		for (int i = 0; i < inv.getSizeInventory(); i++) {
			ItemStack itemStack = inv.getStackInSlot(i); 
			if (!itemStack.isEmpty()) {
				craftingItemList.add(itemStack);
				if (!UtilitiesUtils.isAWaterBottle(itemStack) && (!itemStack.hasTagCompound() || !itemStack.getTagCompound().hasKey("Coating"))) {
				return false;
				}
			
			}
		}
		return  craftingItemList.size() == 2;
	}

	@Override
	public ItemStack getCraftingResult(InventoryCrafting inv) {
		ItemStack result = ItemStack.EMPTY;
		for (int i = 0; i < inv.getSizeInventory(); i++) {
			ItemStack itemStack = inv.getStackInSlot(i); 
			if (UtilitiesUtils.isATool(itemStack)) {
				result = itemStack.copy();
			}
			if (result != ItemStack.EMPTY) {
				if (result.hasTagCompound() && result.getTagCompound().hasKey("Coating", 9)) {
					result.getTagCompound().removeTag("Coating");
					return result;
				}
			}
		}
		return result;
	}

	@Override
	public NonNullList<ItemStack> getRemainingItems(InventoryCrafting inv) {
		NonNullList<ItemStack> ret = NonNullList.withSize(inv.getSizeInventory(), ItemStack.EMPTY);
        for (int i = 0; i < ret.size(); i++) {
        	RoleplayUtilities.utilitiesLog.info("item at spot " + (i+1) + " is: " + inv.getStackInSlot(i));
        	RoleplayUtilities.utilitiesLog.info("is item at spot " + (i+1) + " a water bottle?: " + UtilitiesUtils.isAWaterBottle(inv.getStackInSlot(i)));
        	if (UtilitiesUtils.isAWaterBottle(inv.getStackInSlot(i))) {
        		ret.set(i, new ItemStack(Items.GLASS_BOTTLE));
        		RoleplayUtilities.utilitiesLog.info("Glass Bottle set!");
        	}
        	else { ret.set(i, ForgeHooks.getContainerItem(inv.getStackInSlot(i))); }
        }
        RoleplayUtilities.utilitiesLog.info(ret);
        return ret;
	}
	
	@Override
	public boolean canFit(int width, int height) {
		return width >= 2 && height >= 2;
	}

	@Override
	public ItemStack getRecipeOutput() {
		return ItemStack.EMPTY;
	}
	
	public boolean isDynamic() {
		return true;
	}
	
	
}
