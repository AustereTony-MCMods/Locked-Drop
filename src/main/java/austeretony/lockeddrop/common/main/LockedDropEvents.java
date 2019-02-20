package austeretony.lockeddrop.common.main;

import java.util.List;

import austeretony.lockeddrop.common.config.EnumConfigSettings;
import austeretony.lockeddrop.common.network.NetworkHandler;
import austeretony.lockeddrop.common.network.client.CPSyncData;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class LockedDropEvents {

    @SubscribeEvent
    public void onPlayerConnectsToServer(FMLNetworkEvent.ClientConnectedToServerEvent event) {
        DataManager.initClient();
    }

    @SubscribeEvent
    public void onPlayerLoggedIn(PlayerLoggedInEvent event) {
        if (EnumConfigSettings.SETTINGS_TOOLTIPS.isEnabled()) {
            NetworkHandler.sendTo(new CPSyncData(CPSyncData.EnumAction.SYNC_STATUS), (EntityPlayerMP) event.player);
            NetworkHandler.sendTo(new CPSyncData(CPSyncData.EnumAction.SYNC_TOOLTIPS), (EntityPlayerMP) event.player);
            NetworkHandler.sendTo(new CPSyncData(CPSyncData.EnumAction.SYNC_LIST), (EntityPlayerMP) event.player);
        }
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void onItemTooltip(ItemTooltipEvent event) {
        if (DataManager.isSettingsTooltipsAllowedClient()) {
            ResourceLocation itemRegistryName = event.getItemStack().getItem().getRegistryName();
            int itemMeta = event.getItemStack().getMetadata();
            if (DataManager.existClient(itemRegistryName) 
                    && (DataManager.getClient(itemRegistryName).hasMetaItem(itemMeta) || DataManager.getClient(itemRegistryName).hasMainMeta()))
                addGlobalTooltip(itemRegistryName, itemMeta, event.getToolTip());
            if (event.getItemStack().hasTagCompound())
                addNBTTooltip(event.getItemStack(), itemRegistryName, itemMeta, event.getToolTip());
        }
    }

    @SideOnly(Side.CLIENT)
    private void addGlobalTooltip(ResourceLocation registryName, int meta, List<String> tooltip) {   
        if (!DataManager.getClient(registryName).canBeDroppedOnDeath(meta)) {
            String formatting = TextFormatting.AQUA.toString() + TextFormatting.ITALIC.toString();
            if (!DataManager.isSettingsEnabledClient())
                formatting += TextFormatting.STRIKETHROUGH.toString();                
            tooltip.add(formatting + I18n.format("ld.tooltip.global.keepsOnDeath"));
        }
    }

    @SideOnly(Side.CLIENT)
    private void addNBTTooltip(ItemStack itemStack, ResourceLocation registryName, int meta, List<String> tooltip) {   
        NBTTagCompound tagCompound = itemStack.getTagCompound();
        if (tagCompound.hasKey(DataManager.NBT_TAG_KEEP_ON_DEATH)) {
            String formatting = TextFormatting.GREEN.toString() + TextFormatting.ITALIC.toString();
            if (!DataManager.isSettingsEnabledClient())
                formatting += TextFormatting.STRIKETHROUGH.toString();                
            tooltip.add(formatting + I18n.format("ld.tooltip.nbt.keepsOnDeath"));
        }
    }
}
