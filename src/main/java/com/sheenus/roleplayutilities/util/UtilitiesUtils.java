package com.sheenus.roleplayutilities.util;

import java.util.List;

import com.sheenus.roleplayutilities.RoleplayUtilities;

import net.minecraft.item.Item;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemLingeringPotion;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionType;
import net.minecraft.potion.PotionUtils;

public class UtilitiesUtils {

	public static boolean isATool(ItemStack itemStack) {
		Item item = itemStack.getItem();
		return (item instanceof ItemSword || item instanceof ItemTool || item instanceof ItemHoe);
	}
	
	public static boolean isALingeringWaterBottle(ItemStack itemStack) {
		Item item = itemStack.getItem();
		return (item instanceof ItemLingeringPotion && itemStack.hasTagCompound() && itemStack.getTagCompound().hasKey("Potion") && (itemStack.getTagCompound().getString("Potion").equals("minecraft:water") || itemStack.getTagCompound().getString("Potion").equals("minecraft:mundane") || itemStack.getTagCompound().getString("Potion").equals("minecraft:thick") || itemStack.getTagCompound().getString("Potion").equals("minecraft:awkward")));
	}
	
	public static boolean isAWaterBottle(ItemStack itemStack) {
		Item item = itemStack.getItem();
		return (item instanceof ItemPotion && itemStack.hasTagCompound() && itemStack.getTagCompound().hasKey("Potion") && itemStack.getTagCompound().getString("Potion").equals("minecraft:water"));
	}
	
	public static NBTTagList appendEffectsToNBT(ItemStack potion) {
		
		NBTTagList potionEffectsNBTList = new NBTTagList();
		
		if (potion.hasTagCompound() && potion.getTagCompound().hasKey("Potion") && !potion.getTagCompound().getString("Potion").equals("minecraft:water")) {
			PotionType potionType = PotionUtils.getPotionFromItem(potion);
			List<PotionEffect> potionEffects = potionType.getEffects();
			
			
			for (PotionEffect effect : potionEffects) {
				NBTTagCompound potionEffect = new NBTTagCompound(); 
				
				potionEffect.setInteger("id", Potion.getIdFromPotion(effect.getPotion()));
				if (effect.getPotion().isInstant()) {
					potionEffect.setInteger("time", effect.getDuration());
				} else {
					potionEffect.setInteger("time", Math.round(effect.getDuration() / 80));
				}
				potionEffect.setInteger("lvl", (byte)effect.getAmplifier());
				potionEffect.setInteger("hits", 4);
				
				potionEffectsNBTList.appendTag(potionEffect);
			}
		}
		if (potion.hasTagCompound() && potion.getTagCompound().hasKey("CustomPotionEffects")){
			// RoleplayUtilities.utilitiesLog.info("CustomPotionEffects tag found!");
			NBTTagList customEffectList = potion.getTagCompound().getTagList("CustomPotionEffects", 10);
			// RoleplayUtilities.utilitiesLog.info("CustomPotionEffects tag: " + customEffectList);
			
			for (NBTBase customEffect : customEffectList) {
				NBTTagCompound customEffectCompound = (NBTTagCompound)customEffect;
				NBTTagCompound potionEffect = new NBTTagCompound();
				potionEffect.setInteger("id", customEffectCompound.getByte("Id"));
				potionEffect.setInteger("time", customEffectCompound.getInteger("Duration") / 80);
				potionEffect.setInteger("lvl", customEffectCompound.getByte("Amplifier"));
				potionEffect.setInteger("hits", 4);
				
				// RoleplayUtilities.utilitiesLog.info("added potion effect NBT tag: " + potionEffect);
				potionEffectsNBTList.appendTag(potionEffect);
			}
			
		}
		return potionEffectsNBTList;
	}
}
