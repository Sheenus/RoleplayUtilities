package com.sheenus.roleplayutilities.events;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.google.common.collect.Lists;
import com.sheenus.roleplayutilities.RoleplayUtilities;
import com.sheenus.roleplayutilities.util.Reference;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.StringUtils;
import net.minecraft.util.Tuple;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickItem;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@EventBusSubscriber(modid = Reference.MOD_ID)
public class PotionEffectsEventHandler {
	
	/**
	 * Event handler to deliver custom potion effects (i.e. non-PotionType PotionEffects) upon use of an item with no activation time; e.g. not items like food
	 * and drink that take a short amount of time to consume. Should activate only on items without an activation time, i.e. items with the getMaxItemUseDuration 
	 * variable set to 0.
	 * NBT structure for added PotionEffects should be as follows:
	 * {...,PotionEffects:[{id:x,time:y,lvl:z},{id:a,time:b,lvl:c}],etc.],...} where:
	 * id: the MobEffect ID, as in the registry. Can be either the numeric ID or the ResourceLocation string. Will not fire if it doesn't read a proper effect.
	 * time: the time in seconds the PotionEffect should take effect for. Will default to 0, and always be instant for instant effects.
	 * lvl: the magnitude of the potion effect. works like /effect, where 0 is level I, 1 is level II, etc.
	 * 
	 * @param event the on-use event that fires on item activation.
	 */
	
	@SubscribeEvent
	public static void playerOnUseAddEffect(RightClickItem event) {
		// sanity checks first, to make sure the entity using the item is a player, the item even has NBT tags to read, and has the correct tag list to begin with...
		if (event.getEntity() instanceof EntityPlayer && event.getEntityPlayer().getHeldItem(event.getHand()).getMaxItemUseDuration() == 0 && event.getEntityPlayer().getHeldItem(event.getHand()).hasTagCompound() && event.getEntityPlayer().getHeldItem(event.getHand()).getTagCompound().hasKey("PotionEffects")) {
			
			EntityPlayer player = event.getEntityPlayer();
			NBTTagList potionEffectsNBT = getPotionEffectsTagList(event.getEntityPlayer().getHeldItem(event.getHand()), "PotionEffects");	// grab the tag list of intended effects
			
			for (int i = 0; i < potionEffectsNBT.tagCount(); i++) {	// for loop for each intended PotionEffect:
				
				NBTTagCompound potionTagCompound = getPotionValuesList(potionEffectsNBT, i);	// get the current PotionEffect variable tag list
				
				PotionEffect potionEffect = getPotionEffectFromCompoundNBT(potionTagCompound);
				// if(player.world.isRemote) { RoleplayUtilities.utilitiesLog.info("potion effect to add to player: " + potionEffect.toString()); } 	// DEBUGGING
				
				if (potionEffect != null) {
					player.addPotionEffect(potionEffect);
				}
			}
		}
	}
	
	/**
	 * Event handler to deliver custom potion effects (i.e. non-PotionType PotionEffects) during the use of an item with an activation time; e.g. while eating, 
	 * drinking, using a shield, drawing a bow, etc. Should activate only on items with an activation time, i.e. items with the getMaxItemUseDuration > 0.
	 * 
	 * Will refresh every tick the item is being used (i.e. when activate is held, before the use is finished), so long effect times are often unnecessary.
	 * 
	 * NBT structure for added PotionEffects should be as follows:
	 * {...,WhileUseEffects:[{id:x,time:y,lvl:z},{id:a,time:b,lvl:c}],etc.],...} where:
	 * id: the MobEffect ID, as in the registry. Can be either the numeric ID or the ResourceLocation string. Will not fire if it doesn't read a proper effect.
	 * time: the time in seconds the PotionEffect should take effect for. Will default to 0, and always be instant for instant effects.
	 * lvl: the magnitude of the potion effect. works like /effect, where 0 is level I, 1 is level II, etc.
	 * 
	 * @param event
	 */
	
	@SubscribeEvent
	public static void playerWhileUseAddEffect(LivingEntityUseItemEvent.Tick event) {
		
		// sanity checks first, to make sure the entity using the item is a player, the item even has NBT tags to read, and has the correct tag list to begin with...
		if (event.getEntity() instanceof EntityPlayer && event.getItem().hasTagCompound() && event.getItem().getTagCompound().hasKey("WhileUseEffects") && event.getItem().getMaxItemUseDuration() > 0) {
			
			EntityPlayer player = (EntityPlayer)event.getEntity();
			NBTTagList potionEffectsNBT = getPotionEffectsTagList(event.getItem(), "WhileUseEffects");	// grab the tag list of intended effects
			
			for (int i = 0; i < potionEffectsNBT.tagCount(); i++) { // for loop for each intended PotionEffect:
				
				NBTTagCompound potionTagCompound = getPotionValuesList(potionEffectsNBT, i);	// get the current PotionEffect variable tag list
				
				PotionEffect potionEffect = getPotionEffectFromCompoundNBT(potionTagCompound);
				if(player.world.isRemote) { RoleplayUtilities.utilitiesLog.info("potion effect to add to player: " + potionEffect.toString()); } 	// DEBUGGING
				
				if (potionEffect != null) {
					player.addPotionEffect(potionEffect);
				}
			}
			
		}
	}
	
	/**
	 * Event handler to deliver custom potion effects (i.e. non-PotionType PotionEffects) upon use of items finishing activation, via (and hidden by) NBT tags.
	 * NBT structure for added PotionEffects should be as follows:
	 * {...,PotionEffects:[{id:x,time:y,lvl:z},{id:a,time:b,lvl:c}],etc.],...} where:
	 * id: the MobEffect ID, as in the registry. Can be either the numeric ID or the ResourceLocation string. Will not fire if it doesn't read a proper effect.
	 * time: the time in seconds the PotionEffect should take effect for. Will default to 0, and always be instant for instant effects.
	 * lvl: the magnitude of the potion effect. works like /effect, where 0 is level I, 1 is level II, etc.
	 * 
	 * @param event the on-use event that fires on item activation completion.
	 */
	@SubscribeEvent
	public static void playerOnFinishUseAddEffect(LivingEntityUseItemEvent.Finish event) {
		
		// sanity checks first, to make sure the entity using the item is a player, the item even has NBT tags to read, and has the correct tag list to begin with...
		if (event.getEntity() instanceof EntityPlayer && event.getItem().hasTagCompound() && event.getItem().getTagCompound().hasKey("PotionEffects")) {
			
			EntityPlayer player = (EntityPlayer)event.getEntity();
			NBTTagList potionEffectsNBT = getPotionEffectsTagList(event.getItem(), "PotionEffects");	// grab the tag list of intended effects
			
			for (int i = 0; i < potionEffectsNBT.tagCount(); i++) { // for loop for each intended PotionEffect:
				
				NBTTagCompound potionTagCompound = getPotionValuesList(potionEffectsNBT, i);	// get the current PotionEffect variable tag list
				
				PotionEffect potionEffect = getPotionEffectFromCompoundNBT(potionTagCompound);
				// if(player.world.isRemote) { RoleplayUtilities.utilitiesLog.info("potion effect to add to player: " + potionEffect.toString()); } 	// DEBUGGING
				
				if (potionEffect != null) {
					player.addPotionEffect(potionEffect);
				}
			}
			
		}
	}

	
	
	
	/**
	 * Event handler for magic weapons; detects whether the item the attacked entity was hit with had the relevant on-hit PotionEffect NBT tag.
	 * If so, enacts the specified effect on the attacked entity.
	 * NBT structure for magic weapons should be as follows:
	 * {...WeaponEffects:[{id:x,time:y,lvl:z,},{id:a,time:b,lvl:c}],...}, where the formatting is exactly as for {@link PotionEffectsEventHandler#playerOnUseAddEffect}
	 * 
	 * @param event the LivingAttackEvent where an EntityLiving is attacked, potentially by a magic weapon.
	 */
	@SubscribeEvent
	public static void entityOnHitMagicWeaponEffects(LivingAttackEvent event) {
		
		EntityLivingBase targetEntity = event.getEntityLiving();
		DamageSource damageSource = event.getSource();
		
		if (damageSource.getTrueSource() instanceof EntityLivingBase) {
			
			EntityLivingBase sourceEntity = (EntityLivingBase)(damageSource).getTrueSource();
			ItemStack sourceItem = sourceEntity.getHeldItemMainhand();
			
			if (sourceItem.hasTagCompound() && sourceItem.getTagCompound().hasKey("WeaponEffects")) {
				
				NBTTagList potionEffectsNBT = getPotionEffectsTagList(sourceItem, "WeaponEffects");
				
				for (int i = 0; i < potionEffectsNBT.tagCount(); i++) {
					
					NBTTagCompound potionTagCompound = getPotionValuesList(potionEffectsNBT, i);
					
					PotionEffect potioneffect = getPotionEffectFromCompoundNBT(potionTagCompound);
				            targetEntity.addPotionEffect(potioneffect);
				}					
			}
		}		
	}
	
	/**
	 * Event handler for coated tools; detects whether the item the attacked entity was hit with had the relevant on-hit PotionEffect NBT tag.
	 * If so, enacts the specified effect on the attacked entity.
	 * NBT structure for tool coating should be as follows:
	 * {...Coating:[{id:w,time:x,lvl:y,hits:z},{id:a,time:b,lvl:c,hits:d}],...}, where the formatting is exactly as for {@link PotionEffectsEventHandler#playerOnUseAddEffect}
	 * including one extra attribute:
	 * hits: the number of hits before the coating effect expires.
	 * hits will automatically decrease with each successful hit made by the owner until the tag is reduced to zero and the tag is automatically removed. 
	 * 
	 * @param event the LivingAttackEvent where an EntityLiving is attacked, potentially by a magic weapon.
	 */
	@SubscribeEvent
	public static void entityOnHitCoatingEffect(LivingAttackEvent event) {
	
		EntityLivingBase targetEntity = event.getEntityLiving();
		DamageSource damageSource = event.getSource();
		
		if (damageSource.getTrueSource() instanceof EntityLivingBase) {
			EntityLivingBase sourceEntity = (EntityLivingBase)(damageSource).getTrueSource();
			ItemStack sourceItem = sourceEntity.getHeldItemMainhand();
			
			if (sourceItem.hasTagCompound() && sourceItem.getTagCompound().hasKey("Coating")) {
				NBTTagCompound sourceItemTagCompound = sourceItem.getTagCompound();
				NBTTagList potionEffectsNBT = getPotionEffectsTagList(sourceItem, "Coating");
				
				for (int i = 0; i < potionEffectsNBT.tagCount(); i++) {
					
					NBTTagCompound potionTagCompound = getPotionValuesList(potionEffectsNBT, i);
					
					PotionEffect potionEffect = getPotionEffectFromCompoundNBT(potionTagCompound);
					
					if (potionTagCompound.hasKey("hits") && potionTagCompound.getTagId("hits") == 3 && potionTagCompound.getInteger("hits") >= 1) {	// if the tag has any hits remaining:
			            targetEntity.addPotionEffect(potionEffect);
			            
			            int prevHits = potionTagCompound.getInteger("hits");
			            if (prevHits > 1) {
				            potionTagCompound.setInteger("hits", prevHits - 1);
				            
				            potionEffectsNBT.set(i, potionTagCompound);
				            sourceItemTagCompound.setTag("Coating", potionEffectsNBT);
				            sourceItem.setTagCompound(sourceItemTagCompound);
			            } 
			            else {
			            	sourceItemTagCompound.removeTag("Coating");
			            }
					}
				}					
			}
		}		
	}
	
	/**
	 * Event handler for updating the tooltip for an item with any of the PotionEffect NBT tags, to properly display the PotionEffect tooltips when intended.
	 * 
	 * @param event the ItemTooltipEvent that is fired when an item is moused-over
	 */
	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public static void potionEffectTooltipEvent(ItemTooltipEvent event) {
		List<String> tooltip = event.getToolTip();
		
		if (event.getItemStack().hasTagCompound()) {
			ItemStack itemStack = event.getItemStack();
			NBTTagCompound itemStackTagCompound = itemStack.getTagCompound();
			
			if (!hidingPotionEffectTooltip(itemStack)) {
				if (itemStackTagCompound.hasKey("WhileUseEffects")) {
					NBTTagList potionEffectsNBT = getPotionEffectsTagList(itemStack, "WhileUseEffects"); 
					
					if (potionEffectsNBT != null) {
						
						tooltip.add(TextFormatting.DARK_PURPLE + I18n.translateToLocal("effects.whileuse"));
						List<PotionEffect> effectList = Lists.<PotionEffect>newArrayList();
						List<Tuple<String, AttributeModifier>> attributeModList = Lists.<Tuple<String, AttributeModifier>>newArrayList();
					
						for (int i = 0; i < potionEffectsNBT.tagCount(); i++) {
							
							NBTTagCompound potionTagCompound = getPotionValuesList(potionEffectsNBT, i);
							
							PotionEffect potionEffect = getPotionEffectFromCompoundNBT(potionTagCompound);
							if (potionEffect != null) {
								effectList.add(potionEffect);
							}
						}
						
						if (effectList != null) {
							attributeModList = printPotionEffectsToTooltip(tooltip, effectList);
						}
						
						if (!attributeModList.isEmpty()) {
							printAttributeModifiersToTooltip(tooltip, attributeModList);
				        }
					}
				}
				
				if (itemStackTagCompound.hasKey("PotionEffects")) {
					
					NBTTagList potionEffectsNBT = getPotionEffectsTagList(itemStack, "PotionEffects"); 
					
					if (potionEffectsNBT != null) {
						
						tooltip.add(TextFormatting.DARK_PURPLE + I18n.translateToLocal("effects.onuse"));
						List<PotionEffect> effectList = Lists.<PotionEffect>newArrayList();
						List<Tuple<String, AttributeModifier>> attributeModList = Lists.<Tuple<String, AttributeModifier>>newArrayList();
					
						for (int i = 0; i < potionEffectsNBT.tagCount(); i++) {
							
							NBTTagCompound potionTagCompound = getPotionValuesList(potionEffectsNBT, i);
							
							PotionEffect potionEffect = getPotionEffectFromCompoundNBT(potionTagCompound);
							if (potionEffect != null) {
								effectList.add(potionEffect);
							}
						}
						
						if (effectList != null) {
							attributeModList = printPotionEffectsToTooltip(tooltip, effectList);
						}
						
						if (!attributeModList.isEmpty()) {
							printAttributeModifiersToTooltip(tooltip, attributeModList);
				        }
					}
				}
				
				if (itemStackTagCompound.hasKey("WeaponEffects")) {
					
					NBTTagList potionEffectsNBT = getPotionEffectsTagList(itemStack, "WeaponEffects"); 
					
					if (potionEffectsNBT != null) {
						
						tooltip.add(TextFormatting.DARK_PURPLE + I18n.translateToLocal("effects.onhit"));
						List<PotionEffect> effectList = Lists.<PotionEffect>newArrayList();
						List<Tuple<String, AttributeModifier>> attributeModList = Lists.<Tuple<String, AttributeModifier>>newArrayList();
					
						for (int i = 0; i < potionEffectsNBT.tagCount(); i++) {
							
							NBTTagCompound potionTagCompound = getPotionValuesList(potionEffectsNBT, i);
							
							PotionEffect potionEffect = getPotionEffectFromCompoundNBT(potionTagCompound);
							if (potionEffect != null) {
								effectList.add(potionEffect);
							}
						}
						
						if (effectList != null) {
							attributeModList = printPotionEffectsToTooltip(tooltip, effectList);
				        }
					}
				}
			}
			
			if (itemStackTagCompound.hasKey("Coating")) {
				
				NBTTagList potionEffectsNBT = getPotionEffectsTagList(itemStack, "Coating"); 
				
				if (potionEffectsNBT != null) {
					
					tooltip.add(TextFormatting.DARK_PURPLE + I18n.translateToLocal("effects.coating"));
					List<PotionEffect> effectList = Lists.<PotionEffect>newArrayList();
					List<Tuple<String, AttributeModifier>> attributeModList = Lists.<Tuple<String, AttributeModifier>>newArrayList();
					int hitsRemaining = 0;
				
					for (int i = 0; i < potionEffectsNBT.tagCount(); i++) {
						
						NBTTagCompound potionTagCompound = getPotionValuesList(potionEffectsNBT, i);
						
						PotionEffect potionEffect = getPotionEffectFromCompoundNBT(potionTagCompound);
						hitsRemaining = potionTagCompound.getInteger("hits");
						
						if (potionEffect != null) {
							effectList.add(potionEffect);
						}
					}
					
					if (effectList != null) {
						attributeModList = printPotionEffectsToTooltip(tooltip, effectList);
			        }
					
					if (hitsRemaining > 0) {
						if (hitsRemaining > 1) {
							tooltip.add(I18n.translateToLocalFormatted("effects.coating.hits.multiple", hitsRemaining));
						} 
						else {
							tooltip.add(I18n.translateToLocal("effects.coating.hits.single"));
						}
					}
				}
			}
		}
	}
	
	/**
	 * Helper method for {@link PotionEffectsEventHandler#potionEffectTooltipEvent} to check for, and the value of, the 'NoTooltip' tag.
	 * 
	 */
	private static boolean hidingPotionEffectTooltip(ItemStack itemStack) {
		if (itemStack.getTagCompound().hasKey("NoTooltip") && itemStack.getTagCompound().getByte("NoTooltip") == 1) {
			return true;
		}
		
		else return false;
	}
	
	
	/**
	 * Helper method for the above events to obtain the custom PotionEffect from an item's NBT tag.
	 * Will discern a custom potion effect based on the compound NBT tag:
	 * [...{id:x,time:y,lvl:z,...}...] where:
	 * id: the MobEffect id attached to the PotionEffect. Can be specified via resourcelocation (as a string) or using the numerical ID.
	 * time: the time in seconds the PotionEffect will take effect.
	 * lvl: the magnitude of the PotionEffect. 0 is level I.
	 * See the specific event for the specific NBT tag format required.
	 */
	public static PotionEffect getPotionEffectFromCompoundNBT(NBTTagCompound potionTagCompound) {
		PotionEffect potionEffect = null;
		
		//sanity check for whether we even have a properly formatted effect id tag:
		if (potionTagCompound.hasKey("id")) {
			
			// identify the MobEffect for the intended PotionEffect, store it for later.
			Potion potion;
			//RoleplayUtilities.utilitiesLog.info("ID tag is of NBT type: " + NBTBase.NBT_TYPES[potionTagCompound.getTagId("id")]);
			
			// if the MobEffect ID has been written as a string:
			if (potionTagCompound.getTagId("id") == 8) {
				potion = Potion.getPotionFromResourceLocation(potionTagCompound.getString("id"));
			}
			
			// otherwise, if the tag's value is instead a number:
			else if (potionTagCompound.getTagId("id") == 3 && potionTagCompound.getInteger("id") >= 1) {
				potion = Potion.getPotionById(potionTagCompound.getInteger("id"));
				//RoleplayUtilities.utilitiesLog.info("found Potion MobEffect: " + potion.getName());
			} else {
				potion = null; // just make potion a null value so that it ignores the rest of the next if statement, skipping PotionEffect activation (likely caused due to a mistyped NBT tag)
				//RoleplayUtilities.utilitiesLog.info("found Potion MobEffect: " + potion.getName());
			}
			
			if (potion != null) {
				int time = 0;		// pre-set the time and magnitude values, just in case we have poorly formatted time and magnitude value tags
				int magnitude = 0;
				
				//RoleplayUtilities.utilitiesLog.info("time tag is of NBT type: " + NBTBase.NBT_TYPES[potionTagCompound.getTagId("time")]);		// DEBUG
				// check for if the potion is an instant effect, and if so set time accordingly
				if (potion.isInstant()) {
					time = 1;
				}	// otherwise set the time as indicated (assuming proper tag formatting)
				else if (potionTagCompound.hasKey("time") && potionTagCompound.getTagId("time") == 3 && potionTagCompound.getInteger("time") > 0) {
					time = potionTagCompound.getInteger("time") * 20;
					//RoleplayUtilities.utilitiesLog.info("time set success!");		// DEBUG
				}
				
				//RoleplayUtilities.utilitiesLog.info("lvl tag is of NBT type: " + NBTBase.NBT_TYPES[(potionTagCompound.getTagId("lvl"))]);  	// DEBUG
				if (potionTagCompound.hasKey("lvl") && potionTagCompound.getTagId("lvl") == 3 && potionTagCompound.getInteger("lvl") >= 0) {
					magnitude = potionTagCompound.getInteger("lvl");
					//RoleplayUtilities.utilitiesLog.info("magnitude set success!");	// DEBUG
				}
				
				potionEffect = new PotionEffect(potion, time, magnitude, false, true);
				
			}
		}
		
		return potionEffect;
	}
	
	/**
	 * Helper method for {@link PotionEffectsEventHandler#playerOnFinishUseAddEffect} and others to find the PotionEffects tag list on an item with the appropriate NBT tags.
	 * 
	 * @param item the item used in the event.
	 * @return the PotionEfffects NBT tag list.
	 */
	public static NBTTagList getPotionEffectsTagList(ItemStack item, String listTag) {
		return item.getTagCompound().getTagList(listTag, 10);
	}
	
	/**
	 * Helper method for {@link PotionEffectsEventHandler#playerOnFinishUseAddEffect} and others to find the NBT tag values for a single intended effect from the PotionEffects NBT tag list.
	 * 
	 * @param potionEffectsNBTList the NBT tag list of PotionEffects to be applied to the player using the item so tagged. 
	 * @param index the n'th PotionEffect in the NBT tag list.
	 * @return the compound NBT tag with the values needed to enact the effect. 
	 */
	public static NBTTagCompound getPotionValuesList(NBTTagList potionEffectsNBTList, int index) {
		return potionEffectsNBTList.getCompoundTagAt(index);
	}
	
	/**
	 * Helper method for {@link PotionEffectsEventHandler#potionEffectTooltipEvent} to print the retrieved information for the found potion effects.
	 * 
	 * @param tooltip the tooltip to be modified
	 * @param effectList the PotionEffect list to draw from to help print to the tooltip
	 * @return the AttributeModifier list that passes to {@link PotionEffectsEventHandler#printAttributeModifiersToTooltip} in order to print any attribute modifiers to the tooltip
	 */
	public static List<Tuple<String, AttributeModifier>> printPotionEffectsToTooltip(List<String> tooltip, List<PotionEffect> effectList) {
		List<Tuple<String, AttributeModifier>> attributeModList = Lists.<Tuple<String, AttributeModifier>>newArrayList();
		
		for (PotionEffect potionEffects : effectList) {
			
			String effectName = I18n.translateToLocal(potionEffects.getEffectName()).trim();
			Potion potion = potionEffects.getPotion();
			Map<IAttribute, AttributeModifier> map = potion.getAttributeModifierMap();
			
			if (!map.isEmpty()) {
				for (Entry<IAttribute, AttributeModifier> entry : map.entrySet()) {
					AttributeModifier attributeModDummy = entry.getValue();
					AttributeModifier attributeMod = new AttributeModifier(attributeModDummy.getName(), potion.getAttributeModifierAmount(potionEffects.getAmplifier(), attributeModDummy), attributeModDummy.getOperation());
					attributeModList.add(new Tuple<String, AttributeModifier>(((IAttribute)entry.getKey()).getName(), attributeMod));	
				}
			}
			
			if (potionEffects.getAmplifier() > 0) {
                effectName += " " + I18n.translateToLocal("potion.potency." + potionEffects.getAmplifier()).trim();
            }

            if (potionEffects.getDuration() > 20) {
                effectName += " (" + StringUtils.ticksToElapsedTime(potionEffects.getDuration()) /*Potion.getPotionDurationString(potionEffects, 1.0F) */ + ")";
            }

            if (potion.isBadEffect()) {
                tooltip.add(TextFormatting.RED + effectName);
            }
            else {
                tooltip.add(TextFormatting.BLUE + effectName);
            }
        }
		return attributeModList;
	}
	
	/**
	 * Helper method for {@link PotionEffectsEventHandler#potionEffectTooltipEvent}. Takes the AttributeModifier List from 
	 * {@link PotionEffectsEventHandler#printPotionEffectsToTooltip} in order to generate the print statements for the attribute modifiers to the tooltip.
	 * 
	 * @param tooltip The tooltip to modify
	 * @param attributeModList the List<Tuple<String, AttributeModifier>> we need to print the modifiers to the tooltip
	 */
	public static void printAttributeModifiersToTooltip(List<String> tooltip, List<Tuple<String, AttributeModifier>> attributeModList) {
		tooltip.add("");
        tooltip.add(TextFormatting.DARK_PURPLE + I18n.translateToLocal("potion.whenDrank"));

        for (Tuple<String, AttributeModifier> modifier : attributeModList)
        {
            AttributeModifier attMod = modifier.getSecond();
            double d0 = attMod.getAmount();
            double d1;

            if (attMod.getOperation() != 1 && attMod.getOperation() != 2)
            {
                d1 = attMod.getAmount();
            }
            else
            {
                d1 = attMod.getAmount() * 100.0D;
            }

            if (d0 > 0.0D)
            {
                tooltip.add(TextFormatting.BLUE + I18n.translateToLocalFormatted("attribute.modifier.plus." + attMod.getOperation(), ItemStack.DECIMALFORMAT.format(d1), I18n.translateToLocal("attribute.name." + (String)modifier.getFirst())));
            }
            else if (d0 < 0.0D)
            {
                d1 = d1 * -1.0D;
                tooltip.add(TextFormatting.RED + I18n.translateToLocalFormatted("attribute.modifier.take." + attMod.getOperation(), ItemStack.DECIMALFORMAT.format(d1), I18n.translateToLocal("attribute.name." + (String)modifier.getFirst())));
            }
        }
	}
}
