package austeretony.lockeddrop.common.enchantments;

import java.util.HashSet;
import java.util.Set;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.util.ResourceLocation;

public enum EnumEnchantmentProperties {

    PRESERVATION("preservation");

    public final String id;

    private boolean enabled;

    private String name;

    private Enchantment.Rarity rarity;

    private int 
    minEnchantibility,
    maxEnchantibility;

    private EnumEnchantmentType type;

    private EntityEquipmentSlot[] equipmentSlots;

    private Set<ResourceLocation> 
    incompatibleEnchants = new HashSet<ResourceLocation>(),
    invalidItems = new HashSet<ResourceLocation>();

    EnumEnchantmentProperties(String id) {
        this.id = id;
    }

    public static EnumEnchantmentProperties getOf(String id) {
        for (EnumEnchantmentProperties enumProp : values()) 
            if (enumProp.id.equals(id))
                return enumProp;
        return null;
    }

    public boolean isEnabled() {
        return this.enabled;
    }

    public void setEnabled(boolean flag) {
        this.enabled = flag;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Enchantment.Rarity getRarity() {
        return this.rarity;
    }

    public void setRarity(Enchantment.Rarity rarity) {
        this.rarity = rarity;
    }

    public int getMinEnchantability() {
        return this.minEnchantibility;
    }

    public void setMinEnchantability(int value) {
        this.minEnchantibility = value;
    }

    public int getMaxEnchantability() {
        return this.maxEnchantibility;
    }

    public void setMaxEnchantability(int value) {
        this.maxEnchantibility = value;
    }

    public EnumEnchantmentType getType() {
        return this.type;
    }

    public void setType(EnumEnchantmentType type) {
        this.type = type;
    }

    public EntityEquipmentSlot[] getEquipmentSlots() {
        return this.equipmentSlots;
    }

    public void setEquipmentSlots(EntityEquipmentSlot[] slots) {
        this.equipmentSlots = slots;
    }

    public Set<ResourceLocation> getIncompatibleEnchantments() {
        return this.incompatibleEnchants;
    }

    public boolean incompatible(ResourceLocation registryName) {
        return this.incompatibleEnchants.contains(registryName);
    }

    public void addIncompatibleEnchantment(ResourceLocation registryName) {
        this.incompatibleEnchants.add(registryName);
    }
    
    public Set<ResourceLocation> getInvalidItems() {
        return this.invalidItems;
    }

    public boolean isItemInvalid(ResourceLocation registryName) {
        return this.invalidItems.contains(registryName);
    }

    public void addIvalidItem(ResourceLocation registryName) {
        this.invalidItems.add(registryName);
    }
}
