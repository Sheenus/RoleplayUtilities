package com.sheenus.roleplayutilities.events;

import com.sheenus.roleplayutilities.RoleplayUtilities;
import com.sheenus.roleplayutilities.util.Reference;

import net.minecraft.block.Block;
import net.minecraft.command.CommandBase;
import net.minecraft.command.NumberInvalidException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import scala.actors.threadpool.Arrays;

@EventBusSubscriber(modid = Reference.MOD_ID)
public class SetBlockHandler {

	/**
	 * Event handler to make sick hacks. Will detect a "SetBlock" NBT tag on the activated item, and run the SetBlock command based on the specified parameters.
	 * NBT structure should be as follows:
	 * {...,SetBlock:{x:a,y:b,z:c,block:g,}}
	 * 
	 * setblock <pos> <block> [destroy|keep|replace]
	 * 
	 * @param event
	 */
	/*@SubscribeEvent
	public static void playerSetBlockOnUseEffect(RightClickItem event) {
		String keyword = "SetBlockActivate";
		
		if (event.getEntity() instanceof EntityPlayer && event.getEntityPlayer().getHeldItem(event.getHand()).hasTagCompound() && event.getEntityPlayer().getHeldItem(event.getHand()).getTagCompound().hasKey(keyword)) {
			
			EntityPlayer player = event.getEntityPlayer();
			ItemStack item = event.getEntityPlayer().getHeldItem(event.getHand());
			NBTTagCompound setBlockNBT = item.getTagCompound().getCompoundTag(keyword);
			
			boolean checkNBT = errorCheckSetBlockNBT(player, setBlockNBT);
			if (checkNBT == false) {
				return;
			}
			
			String[] variables = getSetBlockVariables(player, setBlockNBT);
			
			
		}
	} */
	
	/**
	 * helper method for {@link PotionEffectsEventHandler#playerSetBlockOnUseEffect} and similar event handlers to error-check the NBT tag used.
	 */
	
	private static boolean errorCheckSetBlockNBT(EntityPlayer player, NBTTagCompound setBlockNBT) {
		boolean result = true;
		
		if (setBlockNBT.getSize() < 4) {
			TextComponentString notEnoughValuesMessage = new TextComponentString("This item does not have enough arguments in its SetBlock NBT tag. Please contact a server op to fix this.");
			notEnoughValuesMessage.getStyle().setColor(TextFormatting.RED);
			if (player.world.isRemote) { player.sendMessage(notEnoughValuesMessage); }
			result = false;
			return result;
		}
		
		if (!(setBlockNBT.hasKey("x") && setBlockNBT.hasKey("y") && setBlockNBT.hasKey("z") && setBlockNBT.hasKey("block"))) {
			TextComponentString essentialValuesMessage = new TextComponentString("This item does not have arguments in its SetBlock NBT tag essential to its function. Please contact a server op to fix this.");
			essentialValuesMessage.getStyle().setColor(TextFormatting.RED);
			if (player.world.isRemote) { player.sendMessage(essentialValuesMessage); }
			result = false;
			return result;
		}
		
		return result;
	}
	
	/**
	 * helper method for {@link PotionEffectsEventHandler#playerSetBlockOnUseEffect} to find the relevant information for use in 
	 * {@link UTilitiesEventHandler#setBlockNBTMethod}. Outputs as a String array.
	 */
	
	private static String[] getSetBlockVariables(EntityPlayer player, NBTTagCompound setBlockNBT) {
		int noOfArgs = setBlockNBT.getSize();
		String[] output = new String[noOfArgs];
		
		if (player.world.isRemote) { RoleplayUtilities.utilitiesLog.info("no of tag entries: " + noOfArgs); }
		
		if (setBlockNBT.hasKey("x") && setBlockNBT.hasKey("y") && setBlockNBT.hasKey("z") && setBlockNBT.hasKey("block")) {
			output[0] = Integer.toString(setBlockNBT.getInteger("x"));
			output[1] = Integer.toString(setBlockNBT.getInteger("y"));
			output[2] = Integer.toString(setBlockNBT.getInteger("z"));
			output[3] = setBlockNBT.getString("block");
		}
		
		if (setBlockNBT.hasKey("method")) {
			output[4] = setBlockNBT.getString("method");
		} else {output[4] = "replace";}
		if (player.world.isRemote) {
			RoleplayUtilities.utilitiesLog.info("no. of values in output: " + output.length);
			RoleplayUtilities.utilitiesLog.info(Arrays.toString(output));
		}
		return output;
	}
	
	/** helper method for {@link PotionEffectsEventHandler#playerSetBlockOnUseEffect} that emulates the SetBlock command, with the inputs string-ified from NBT
	 * values on the item used.
	 */
	
	private static void setBlockFromNBT(EntityPlayer player, String[] args) throws NumberInvalidException {
		BlockPos blockPos = new BlockPos(Integer.parseInt(args[0]), Integer.parseInt(args[1]),Integer.parseInt(args[2]));
		Block block = CommandBase.getBlockByText(player, args[3]);
		
	}
	
}
