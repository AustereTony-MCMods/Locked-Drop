package austeretony.lockeddrop.common.network.client;

import austeretony.lockeddrop.common.config.EnumConfigSettings;
import austeretony.lockeddrop.common.main.DataManager;
import austeretony.lockeddrop.common.main.LockedItem;
import austeretony.lockeddrop.common.main.MetaItem;
import austeretony.lockeddrop.common.network.ProxyPacket;
import net.minecraft.network.INetHandler;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;

public class CPSyncData extends ProxyPacket {

    private EnumAction enumAction;

    public CPSyncData() {}

    public CPSyncData(EnumAction enumAction) {
        this.enumAction = enumAction;
    }

    @Override
    public void write(PacketBuffer buffer) { 
        buffer.writeByte((byte) enumAction.ordinal());
        switch (this.enumAction) {
        case SYNC_STATUS:
            buffer.writeBoolean(DataManager.isSettingsEnabled());
            break;
        case SYNC_TOOLTIPS:
            buffer.writeBoolean(EnumConfigSettings.SETTINGS_TOOLTIPS.isEnabled());
            break;
        case SYNC_LIST:
            String regNameStr;
            buffer.writeByte(DataManager.getItemsServer().size());
            for (LockedItem lockedItem : DataManager.getItemsServer().values()) {
                regNameStr = lockedItem.registryName.toString();
                buffer.writeByte(regNameStr.length());
                buffer.writeString(regNameStr);
                buffer.writeByte(lockedItem.getMainMeta());
                buffer.writeByte(lockedItem.getData().size());
                for (MetaItem metaItem : lockedItem.getData().values()) {
                    buffer.writeByte(metaItem.meta);
                    buffer.writeBoolean(metaItem.canBeDroppedOnDeath());
                }
            }  
            break;
        case SYNC_LATEST:
            String regNameStr2;
            LockedItem lockedItem = DataManager.getServer(DataManager.latestItem.registryName);
            regNameStr2 = lockedItem.registryName.toString();
            buffer.writeByte(regNameStr2.length());
            buffer.writeString(regNameStr2);
            buffer.writeByte(lockedItem.getMainMeta());
            buffer.writeByte(lockedItem.getData().size());
            for (MetaItem metaItem : lockedItem.getData().values()) {
                buffer.writeByte(metaItem.meta);
                buffer.writeBoolean(metaItem.canBeDroppedOnDeath());
            }
            break;
        case REMOVE_LATEST:
            String regNameStr3 = DataManager.latestItem.registryName.toString();
            buffer.writeByte(regNameStr3.length());
            buffer.writeString(regNameStr3);
            buffer.writeByte(DataManager.latestItem.meta);
            break;
        default:
            break;
        }
    }

    @Override
    public void read(PacketBuffer buffer) {
        this.enumAction = EnumAction.values()[buffer.readByte()];
        switch (this.enumAction) {
        case SYNC_STATUS:
            DataManager.setSettingsEnabledClient(buffer.readBoolean());
            break;
        case SYNC_TOOLTIPS:
            DataManager.setSettingsTooltipsAllowedClient(buffer.readBoolean());
            break;
        case SYNC_LIST:
            DataManager.clearDataClient();
            String regNameStr;
            ResourceLocation registryName;
            int amount, metaAmount, mainMeta, meta;
            MetaItem metaItem;
            amount = buffer.readByte();
            for (int i = 0; i < amount; i++) {
                regNameStr = buffer.readString(buffer.readByte());
                registryName = new ResourceLocation(regNameStr);
                mainMeta = buffer.readByte();
                metaAmount = buffer.readByte();
                for (int j = 0; j < metaAmount; j++) {
                    meta = buffer.readByte();
                    DataManager.disableDropItemClient(
                            registryName, 
                            meta, 
                            "");      
                    metaItem = DataManager.getClient(registryName).getMetaItem(meta);
                    metaItem.setCanBeDroppedOnDeath(buffer.readBoolean());
                }
                DataManager.getClient(registryName).setMainMeta(mainMeta);
            }
            break;
        case SYNC_LATEST:
            String regNameStr2;
            ResourceLocation registryName2;
            int metaAmount2, mainMeta2, meta2, itemMeta;
            MetaItem metaItem2;
            regNameStr2 = buffer.readString(buffer.readByte());
            registryName2 = new ResourceLocation(regNameStr2);
            if (DataManager.existClient(registryName2))
                DataManager.enableDropItemClient(registryName2);
            mainMeta2 = buffer.readByte();
            metaAmount2 = buffer.readByte();
            for (int j = 0; j < metaAmount2; j++) {
                meta2 = buffer.readByte();
                DataManager.disableDropItemClient(
                        registryName2, 
                        meta2, 
                        "");
                metaItem2 = DataManager.getClient(registryName2).getMetaItem(meta2);
                metaItem2.setCanBeDroppedOnDeath(buffer.readBoolean());
            }
            DataManager.getClient(registryName2).setMainMeta(mainMeta2);
            break;
        case CLEAR_LIST:
            DataManager.clearDataClient();
            break;
        case REMOVE_LATEST:
            DataManager.enableDropItemClient(new ResourceLocation(buffer.readString(buffer.readByte())), buffer.readByte());
            break;
        }
    }

    @Override
    public void process(INetHandler netHandler) {}

    public enum EnumAction {

        SYNC_STATUS,
        SYNC_TOOLTIPS,   
        SYNC_LIST,
        SYNC_LATEST,
        CLEAR_LIST,
        REMOVE_LATEST;
    }
}
