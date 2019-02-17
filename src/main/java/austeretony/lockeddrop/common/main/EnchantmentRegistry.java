package austeretony.lockeddrop.common.main;

import austeretony.lockeddrop.common.enchantments.EnchantmentPreservation;
import austeretony.lockeddrop.common.enchantments.EnumEnchantmentProperties;
import net.minecraft.enchantment.Enchantment;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class EnchantmentRegistry {

    public static Enchantment 
    preservation;

    @SubscribeEvent
    public void registerEnchantments(RegistryEvent.Register<Enchantment> event) {
        if (EnumEnchantmentProperties.PRESERVATION.isEnabled()) {
            preservation = new EnchantmentPreservation(
                    EnumEnchantmentProperties.PRESERVATION.getName(), 
                    EnumEnchantmentProperties.PRESERVATION.getRarity(), 
                    EnumEnchantmentProperties.PRESERVATION.getType(), 
                    EnumEnchantmentProperties.PRESERVATION.getEquipmentSlots());
            event.getRegistry().register(preservation);
        }
    }
}
