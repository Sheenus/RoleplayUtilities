package com.sheenus.roleplayutilities.events;

import java.util.Arrays;

import com.sheenus.roleplayutilities.RoleplayUtilities;
import com.sheenus.roleplayutilities.util.Reference;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.command.CommandBase;
import net.minecraft.command.NumberInvalidException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickItem;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@EventBusSubscriber(modid = Reference.MOD_ID)
public class SetRedstoneHandler {
	
	@SubscribeEvent
	public static void playerSetRedstoneOnUseEffect(RightClickItem event) throws NumberInvalidException {
		String keyword = "SetRedstoneActivate";
		
		if (event.getEntity() instanceof EntityPlayer && event.getEntityPlayer().getHeldItem(event.getHand()).getMaxItemUseDuration() == 0 && event.getEntityPlayer().getHeldItem(event.getHand()).hasTagCompound() && event.getEntityPlayer().getHeldItem(event.getHand()).getTagCompound().hasKey(keyword)) {
			
			EntityPlayer player = event.getEntityPlayer();
			ItemStack item = event.getEntityPlayer().getHeldItem(event.getHand());
			NBTTagCompound setBlockNBT = item.getTagCompound().getCompoundTag(keyword);
			
			boolean checkNBT = errorCheckSetRedstoneNBT(player, setBlockNBT);
			if (checkNBT == false) {
				return;
			}
			
			String[] variables = getSetRedstoneVariables(player, setBlockNBT);
			
			setRedstoneFromNBT(player, variables);
		}
	}
	
	@SubscribeEvent
	public static void playerSetRedstoneOnFinishUseEffect(LivingEntityUseItemEvent.Finish event) throws NumberInvalidException {
		String keyword = "SetRedstoneActivate";
		
		if (event.getEntity() instanceof EntityPlayer && event.getItem().getMaxItemUseDuration() > 0 && event.getItem().hasTagCompound() && event.getItem().getTagCompound().hasKey(keyword)) {
			
			EntityPlayer player = (EntityPlayer)event.getEntity();
			ItemStack item = event.getItem();
			NBTTagCompound setBlockNBT = item.getTagCompound().getCompoundTag(keyword);
			
			boolean checkNBT = errorCheckSetRedstoneNBT(player, setBlockNBT);
			if (checkNBT == false) {
				return;
			}
			
			String[] variables = getSetRedstoneVariables(player, setBlockNBT);
			
			setRedstoneFromNBT(player, variables);
		}
	}
	
	private static boolean errorCheckSetRedstoneNBT(EntityPlayer player, NBTTagCompound setBlockNBT) {
		boolean result = true;
		
		if (setBlockNBT.getSize() < 3) {
			TextComponentString notEnoughValuesMessage = new TextComponentString(I18n.format("warnings.setredstoneactivate.notenoughargs"));
			notEnoughValuesMessage.getStyle().setColor(TextFormatting.RED);
			if (player.world.isRemote) { player.sendMessage(notEnoughValuesMessage); }
			result = false;
			return result;
		}
		
		if (!(setBlockNBT.hasKey("x", 3) && setBlockNBT.hasKey("y", 3) && setBlockNBT.hasKey("z", 3))) {
			TextComponentString coordTagsWrongTypeMessage = new TextComponentString(I18n.format("warnings.setredstoneactivate.coordtagswrongtype"));
			coordTagsWrongTypeMessage.getStyle().setColor(TextFormatting.RED);
			if (player.world.isRemote) { player.sendMessage(coordTagsWrongTypeMessage); }
			result = false;
			return result;
		}
		
		return result;
	}
	
	private static String[] getSetRedstoneVariables(EntityPlayer player, NBTTagCompound setBlockNBT) {
		int noOfArgs = 4;
		String[] output = new String[noOfArgs];
		
		// if (player.world.isRemote) { RoleplayUtilities.utilitiesLog.info("no of tag entries: " + noOfArgs); }
		
		if (setBlockNBT.hasKey("x", 3) && setBlockNBT.hasKey("y", 3) && setBlockNBT.hasKey("z", 3)) {
			output[0] = Integer.toString(setBlockNBT.getInteger("x"));
			output[1] = Integer.toString(setBlockNBT.getInteger("y"));
			output[2] = Integer.toString(setBlockNBT.getInteger("z"));
		}
		
		output[3] = "redstone_wire";
		
		/*if (player.world.isRemote) {
			RoleplayUtilities.utilitiesLog.info("no. of values in output: " + output.length);
			RoleplayUtilities.utilitiesLog.info(Arrays.toString(output));
		} */
		return output;
	}
	
	private static void setRedstoneFromNBT(EntityPlayer player, String[] args) throws NumberInvalidException {
		BlockPos blockPos = new BlockPos(Integer.parseInt(args[0]), Integer.parseInt(args[1]),Integer.parseInt(args[2]));
		Block block = CommandBase.getBlockByText(player, args[3]);
		IBlockState blockState = block.getDefaultState();
		
		World world = player.getEntityWorld();
		
		if (!world.isBlockLoaded(blockPos))
        {
            return;
        }
		
		world.setBlockState(blockPos, blockState, 2);
		
		
	}
}
