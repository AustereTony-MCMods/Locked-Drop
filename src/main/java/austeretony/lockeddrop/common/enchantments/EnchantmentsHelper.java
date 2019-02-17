package austeretony.lockeddrop.common.enchantments;

import austeretony.lockeddrop.common.main.EnchantmentRegistry;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemStack;

public class EnchantmentsHelper {

    public static boolean hasPreservationEnchantment(ItemStack itemStack) {   
        return EnumEnchantmentProperties.PRESERVATION.isEnabled() && EnchantmentHelper.getEnchantmentLevel(EnchantmentRegistry.preservation, itemStack) > 0;
    }
}
