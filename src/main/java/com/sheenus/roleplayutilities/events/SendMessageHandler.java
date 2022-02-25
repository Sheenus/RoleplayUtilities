package com.sheenus.roleplayutilities.events;

import com.sheenus.roleplayutilities.util.Reference;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickItem;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@EventBusSubscriber(modid = Reference.MOD_ID)
public class SendMessageHandler {
	
	/**
	 * Event handler to deliver flavor text or roleplay lore via the chat GUI, on use of an item with a use duration, with the correct NBT tag.
	 * Activates on item activation finish.
	 * NBT structure for added lore messages should be as follows:
	 * {...,Message:{text:"your text here",color:x,regular:<true|false>,bold:<true|false>,underlined:<true|false>,strikethrough:<true|false>,obfuscated:<true|false>},...} where:
	 * text: is a String NBT tag, with the message delineated by double apostrophes ("). Necessary for successful use;
	 * color: string tag of the colour of the text, set to the lowercase name of the colour in question. optional;
	 * regular, bold, underlined, strikethrough, obfuscated: boolean (i.e. byte) tags to set the formatting style of the message. all are optional;
	 * defaults to italic gray formatting when colour or styling not specified. 
	 * 
	 * @param event the on-use event that fires on item activation completion. 
	 */
	
	/*
	 * TO DO: modify so that the colour and formatting of the message are customizable from the tag.
	 */
	@SubscribeEvent
	public static void playerOnUseFinishSendMessage(LivingEntityUseItemEvent.Finish event) {
		
		//RoleplayUtilities.utilitiesLog.info("Is there a Compound NBT tag attached?: " + event.getItem().hasTagCompound());
		//RoleplayUtilities.utilitiesLog.info("message sent is of NBT type: " + NBTBase.NBT_TYPES[(event.getItem().getTagCompound().getTagId("Message"))]);
		if (event.getEntity() instanceof EntityPlayer && event.getItem().hasTagCompound() && event.getItem().getTagCompound().hasKey("Message")) {	
			
			NBTTagCompound messageTag = event.getItem().getTagCompound().getCompoundTag("Message");
			EntityPlayer player = (EntityPlayer)event.getEntity();
			
			if (messageTag.hasKey("text", 8)) {
				TextComponentString message = setMessage(messageTag);
				if (player.world.isRemote) {
					player.sendMessage(message);
				}
			} else { return; }	
		}
	}

	/**
	 * Event handler to deliver flavor text or roleplay lore via the chat GUI, with the correct NBT tag.
	 * Activates on item activation.
	 {...,Message:{text:"your text here",color:x,regular:<true|false>,bold:<true|false>,underlined:<true|false>,strikethrough:<true|false>,obfuscated:<true|false>},...} where:
	 * text: is a String NBT tag, with the message delineated by double apostrophes ("). Necessary for successful use;
	 * color: string tag of the colour of the text, set to the lowercase name of the colour in question. optional;
	 * regular, bold, underlined, strikethrough, obfuscated: boolean (i.e. byte) tags to set the formatting style of the message. all are optional;
	 * defaults to italic gray formatting when colour or styling not specified. 
	 * 
	 * @param event the on-use event that fires on item activation completion. 
	 */
	
	/*
	 * TO DO: modify so that the colour and formatting of the message are customizable from the tag.
	 */
	@SubscribeEvent
	public static void playerOnUseSendMessage(RightClickItem event) {
		
		//RoleplayUtilities.utilitiesLog.info("Is there a Compound NBT tag attached?: " + event.getItem().hasTagCompound());
		//RoleplayUtilities.utilitiesLog.info("message sent is of NBT type: " + NBTBase.NBT_TYPES[(event.getItem().getTagCompound().getTagId("Message"))]);
		ItemStack item = event.getEntityPlayer().getHeldItem(event.getHand());
		if (event.getEntity() instanceof EntityPlayer && item.getMaxItemUseDuration() == 0 && item.hasTagCompound() && item.getTagCompound().hasKey("Message")) {	
			NBTTagCompound messageTag = item.getTagCompound().getCompoundTag("Message");
			EntityPlayer player = (EntityPlayer)event.getEntity();
			
			if (messageTag.hasKey("text", 8)) {
				TextComponentString message = setMessage(messageTag);
				if (player.world.isRemote) {
					player.sendMessage(message);
				}
			} else { return; }
		}
	}
	/**
	 * Helper function for the above event handlers to set the message to be broadcast to the player using the item to which the appropriate NBT tag has been 
	 * attached.
	 * 
	 * @param messageTag the compound tag to describe the message to be broadcast. Usually a sub-tag to a larger compound tag on the item.
	 * @return
	 */
	private static TextComponentString setMessage(NBTTagCompound messageTag) {
		
		TextComponentString message = new TextComponentString(messageTag.getString("text"));
		message.getStyle().setItalic(true);
		if (messageTag.hasKey("color", 8)) {
			message.getStyle().setColor(TextFormatting.getValueByName(messageTag.getString("color")));
		} else { message.getStyle().setColor(TextFormatting.GRAY); }
		
		if (messageTag.hasKey("regular", 1) && messageTag.getBoolean("regular") == true) { message.getStyle().setItalic(false); }
		
		if (messageTag.hasKey("bold", 1) && messageTag.getBoolean("bold") == true) { message.getStyle().setBold(true); }
		
		if (messageTag.hasKey("underlined", 1) && messageTag.getBoolean("underlined") == true) { message.getStyle().setUnderlined(true); }
		
		if (messageTag.hasKey("strikethrough", 1) && messageTag.getBoolean("strikethrough") == true) { message.getStyle().setStrikethrough(true); }
		
		if (messageTag.hasKey("obfuscated", 1) && messageTag.getBoolean("strikethrough") == true) { message.getStyle().setObfuscated(true); }
		
		return message;
	}
}
