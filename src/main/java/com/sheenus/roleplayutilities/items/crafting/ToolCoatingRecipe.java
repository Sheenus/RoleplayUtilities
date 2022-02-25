package com.sheenus.roleplayutilities.items.crafting;

import java.util.List;

import com.google.common.base.MoreObjects;
import com.google.common.collect.Lists;
import com.sheenus.roleplayutilities.util.UtilitiesUtils;

import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemLingeringPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;

public class ToolCoatingRecipe extends net.minecraftforge.registries.IForgeRegistryEntry.Impl<IRecipe> implements IRecipe {
	
	@Override
	public boolean matches(InventoryCrafting inv, World worldIn) {
		List<ItemStack> craftingItemList = Lists.<ItemStack>newArrayList();
		
		for (int i = 0; i < inv.getSizeInventory(); i++) {
			ItemStack itemStack = inv.getStackInSlot(i); 
			if (!itemStack.isEmpty()) {
				craftingItemList.add(itemStack);
				if (!UtilitiesUtils.isATool(itemStack) && !(itemStack.getItem() instanceof ItemLingeringPotion) || UtilitiesUtils.isALingeringWaterBottle(itemStack)) {
					return false;
				}
				
				if (itemStack.hasTagCompound() && (itemStack.getTagCompound().hasKey("Coating")) || itemStack.getTagCompound().hasKey("NoCoating")) {
					return false;
				}
			}
		}
		return craftingItemList.size() == 2;
	}

	@Override
	public ItemStack getCraftingResult(InventoryCrafting inv) {
		
		ItemStack result = ItemStack.EMPTY;
		NBTTagList potionEffects = new NBTTagList();
		for (int i = 0; i < inv.getSizeInventory(); i++) {
			ItemStack itemStack = inv.getStackInSlot(i); 
			if (UtilitiesUtils.isATool(itemStack)) {
				result = itemStack.copy();
			}
			else if (itemStack.getItem() == Items.LINGERING_POTION) {
				potionEffects = UtilitiesUtils.appendEffectsToNBT(itemStack);				
			}
		}
		if (result != ItemStack.EMPTY) {
			NBTTagCompound resultTagCompound = (NBTTagCompound)MoreObjects.firstNonNull(result.getTagCompound(), new NBTTagCompound());
			resultTagCompound.setTag("Coating", potionEffects);
            result.setTagCompound(resultTagCompound);
		}
		return result;
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
