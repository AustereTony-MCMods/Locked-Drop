package austeretony.lockeddrop.common.main;

import java.util.HashMap;
import java.util.Map;

import austeretony.lockeddrop.common.config.ConfigLoader;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class DataManager {

    public static final String NBT_TAG_KEEP_ON_DEATH = LockedDropMain.MODID + ":keep_on_death";

    private static Map<ResourceLocation, LockedItem> lockedItemsClient, lockedItemsServer;

    public static LatestItem latestItem;

    private static boolean 
    isSettingsEnabled = true,
    hasDataServer,
    hasDataClient;

    @SideOnly(Side.CLIENT)
    private static boolean 
    settingsEnabledClient,
    settingsTooltipsClient;

    public static void init() {
        lockedItemsServer = new HashMap<ResourceLocation, LockedItem>();
        ConfigLoader.load();
    }

    @SideOnly(Side.CLIENT)
    public static void initClient() {
        lockedItemsClient = new HashMap<ResourceLocation, LockedItem>();
    }

    public static boolean hasDataServer() {
        return hasDataServer;
    }

    @SideOnly(Side.CLIENT)
    public static boolean hasDataClient() {
        return hasDataClient;
    }

    public static Map<ResourceLocation, LockedItem> getItemsServer() {
        return lockedItemsServer;
    }

    @SideOnly(Side.CLIENT)
    public static Map<ResourceLocation, LockedItem> getItemsClient() {
        return lockedItemsClient;
    }

    public static boolean existServer(ResourceLocation registryName) {
        return hasDataServer && lockedItemsServer.containsKey(registryName);
    }

    public static boolean existServer(ItemStack itemStack) {
        return existServer(itemStack.getItem().getRegistryName());
    }

    public static boolean existMetaLatestServer() {
        return existServer(latestItem.registryName) && getServer(latestItem.registryName).hasMetaItem(latestItem.meta);
    }

    @SideOnly(Side.CLIENT)
    public static boolean existClient(ResourceLocation registryName) {
        return hasDataClient && lockedItemsClient.containsKey(registryName);
    }

    public static LockedItem getServer(ResourceLocation registryName) {
        return lockedItemsServer.get(registryName);
    }

    public static LockedItem getServer(ItemStack itemStack) {
        return getServer(itemStack.getItem().getRegistryName());
    }

    @SideOnly(Side.CLIENT)
    public static LockedItem getClient(ResourceLocation registryName) {
        return lockedItemsClient.get(registryName);
    }

    public static void disableDropItemServer(ResourceLocation registryName, int meta, String unlocalizedName) {   
        if (!lockedItemsServer.containsKey(registryName)) {
            LockedItem lockedItem = new LockedItem(registryName);
            lockedItem.createMetaItem(meta, unlocalizedName);
            lockedItemsServer.put(registryName, lockedItem);  
        } else {
            getServer(registryName).createMetaItem(meta, unlocalizedName); 
        }
        hasDataServer = true;
    }

    public static void disableDropGlobalLatestItemServer() {   
        if (!lockedItemsServer.containsKey(latestItem.registryName)) {
            LockedItem lockedItem = new LockedItem(latestItem.registryName);
            lockedItem.createMetaItem(latestItem.meta, latestItem.unlocalizedName);
            lockedItemsServer.put(latestItem.registryName, lockedItem);  
        } else {
            getServer(latestItem.registryName).createMetaItem(latestItem.meta, latestItem.unlocalizedName); 
        }
        hasDataServer = true;
    }

    @SideOnly(Side.CLIENT)
    public static void disableDropItemClient(ResourceLocation registryName, int meta, String unlocalizedName) {   
        if (!lockedItemsClient.containsKey(registryName)) {
            LockedItem lockedItem = new LockedItem(registryName);
            lockedItem.createMetaItem(meta, unlocalizedName);
            lockedItemsClient.put(registryName, lockedItem);  
        } else {
            getClient(registryName).createMetaItem(meta, unlocalizedName); 
        }
        hasDataClient = true;
    }

    public static void enableDropItemServer(ResourceLocation registryName) {
        lockedItemsServer.remove(registryName);
        hasDataServer = !lockedItemsServer.isEmpty();
    }

    public static void enableDropGlobalLatestItemServer() {   
        getServer(latestItem.registryName).removeMetaItem(latestItem.meta);
        if (!getServer(latestItem.registryName).hasData())
            enableDropItemServer(latestItem.registryName);
    }

    @SideOnly(Side.CLIENT)
    public static void enableDropItemClient(ResourceLocation registryName) {
        lockedItemsClient.remove(registryName);
        hasDataClient = !lockedItemsClient.isEmpty();
    }

    public static void clearDataServer() {
        lockedItemsServer.clear();
        hasDataServer = false;
    }

    @SideOnly(Side.CLIENT)
    public static void clearDataClient() {
        lockedItemsClient.clear();
        hasDataClient = false;
    }

    public static String createDisplayKey(ResourceLocation registryName, int meta) {
        return registryName.toString() + "-" + meta;
    }

    public static boolean isSettingsEnabled() {
        return isSettingsEnabled;
    }

    public static void setSettingsEnabled(boolean flag) {
        isSettingsEnabled = flag;
    }

    @SideOnly(Side.CLIENT)
    public static boolean isSettingsEnabledClient() {
        return settingsEnabledClient;
    }

    @SideOnly(Side.CLIENT)
    public static void setSettingsEnabledClient(boolean flag) {
        settingsEnabledClient = flag;
    }

    @SideOnly(Side.CLIENT)
    public static boolean isSettingsTooltipsAllowedClient() {
        return settingsTooltipsClient;
    }

    @SideOnly(Side.CLIENT)
    public static void setSettingsTooltipsAllowedClient(boolean flag) {
        settingsTooltipsClient = flag;
    }

    public static void disableDropHeldItemNBTServer(EntityPlayer player) {
        ItemStack itemStack = player.getHeldItemMainhand();
        NBTTagCompound mainCompound;
        if (!itemStack.hasTagCompound()) {
            mainCompound = new NBTTagCompound();
            itemStack.setTagCompound(mainCompound);
        } else
            mainCompound = itemStack.getTagCompound();
        mainCompound.setBoolean(NBT_TAG_KEEP_ON_DEATH, true);
    }

    public static void enableDropHeldItemNBTServer(EntityPlayer player) {
        player.getHeldItemMainhand().getTagCompound().removeTag(NBT_TAG_KEEP_ON_DEATH);
    }
}
