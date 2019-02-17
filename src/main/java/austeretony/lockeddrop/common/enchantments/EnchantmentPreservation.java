package austeretony.lockeddrop.common.enchantments;

import austeretony.lockeddrop.common.main.LockedDropMain;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;

public class EnchantmentPreservation extends Enchantment {

    public EnchantmentPreservation(String enchantmentName, Rarity rarity, EnumEnchantmentType type, EntityEquipmentSlot... slots) {
        super(rarity, type, slots);
        this.setEnchantmentName(enchantmentName);
    }

    private void setEnchantmentName(String enchantmentName) {
        this.setRegistryName(LockedDropMain.MODID, EnumEnchantmentProperties.PRESERVATION.id);
        this.setName(enchantmentName);
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public boolean canApply(ItemStack itemStack) {
        return !EnumEnchantmentProperties.PRESERVATION.isItemInvalid(itemStack.getItem().getRegistryName());
    }

    @Override
    public boolean canApplyTogether(Enchantment enchantment) {
        return super.canApplyTogether(enchantment) && !EnumEnchantmentProperties.PRESERVATION.incompatible(enchantment.getRegistryName());
    }

    @Override
    public int getMinEnchantability(int enchantmentLevel) {
        return EnumEnchantmentProperties.PRESERVATION.getMinEnchantability();
    }

    @Override
    public int getMaxEnchantability(int enchantmentLevel) {
        return EnumEnchantmentProperties.PRESERVATION.getMaxEnchantability();
    }
}
