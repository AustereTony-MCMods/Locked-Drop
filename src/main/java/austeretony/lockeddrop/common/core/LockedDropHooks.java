package austeretony.lockeddrop.common.core;

import java.util.List;
import java.util.Map;

import austeretony.lockeddrop.common.config.ConfigLoader;
import austeretony.lockeddrop.common.enchantments.EnchantmentsHelper;
import austeretony.lockeddrop.common.main.DataManager;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;

public class LockedDropHooks {
    
    public static void loadCustomLocalization(List<String> languageList, Map<String, String> properties) {
        ConfigLoader.loadCustomLocalization(languageList, properties);
    }

    public static boolean canItemStackBeDropped(ItemStack itemStack) {
        if (DataManager.isSettingsEnabled()) {
            if (itemStack.hasTagCompound() && itemStack.getTagCompound().hasKey(DataManager.NBT_TAG_KEEP_ON_DEATH))
                return false;
            if (EnchantmentsHelper.hasPreservationEnchantment(itemStack))
                return false;
            if (DataManager.existServer(itemStack)) {
                if (DataManager.getServer(itemStack).canBeDroppedOnDeath(itemStack.getMetadata()))
                    return true;
                return false;           
            }
        }
        return true;
    }

    public static void copyInventory(EntityPlayerMP newPlayer, EntityPlayerMP oldPlayer) {
        if (DataManager.isSettingsEnabled())
            newPlayer.inventory.copyInventory(oldPlayer.inventory);//TODO Very unreliable
    }
}
