package austeretony.lockeddrop.common.network.client;

import austeretony.lockeddrop.common.main.DataManager;
import austeretony.lockeddrop.common.main.LockedItem;
import austeretony.lockeddrop.common.main.MetaItem;
import austeretony.lockeddrop.common.network.ProxyPacket;
import net.minecraft.network.INetHandler;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;

public class CPSyncData extends ProxyPacket {

    private byte action;

    public CPSyncData() {}

    public CPSyncData(EnumAction enumAction) {
        this.action = (byte) enumAction.ordinal();
    }

    @Override
    public void write(PacketBuffer buffer) { 
        buffer.writeByte(this.action);
        if (this.action == EnumAction.SYNC_ALL.ordinal()) {
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
        } else if (this.action == EnumAction.SYNC_LATEST.ordinal()) {
            String regNameStr, biomeRegNameStr, boundItemRegNameStr;
            LockedItem lockedItem = DataManager.getServer(DataManager.latestItem.registryName);
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
    }

    @Override
    public void read(PacketBuffer buffer) {
        this.action = buffer.readByte();
        if (this.action == EnumAction.SYNC_ALL.ordinal()) {
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
                    DataManager.lockItemClient(
                            registryName, 
                            meta, 
                            "");      
                    metaItem = DataManager.getClient(registryName).getMetaItem(meta);
                    metaItem.setCanBeDroppedOnDeath(buffer.readBoolean());
                }
                DataManager.getClient(registryName).setMainMeta(mainMeta);
            }
        } else if (this.action == EnumAction.SYNC_LATEST.ordinal()) {
            String regNameStr;
            ResourceLocation registryName;
            int metaAmount, mainMeta, meta, itemMeta;
            MetaItem metaItem;
            regNameStr = buffer.readString(buffer.readByte());
            registryName = new ResourceLocation(regNameStr);
            if (DataManager.existClient(registryName))
                DataManager.removeItemClient(registryName);
            mainMeta = buffer.readByte();
            metaAmount = buffer.readByte();
            for (int j = 0; j < metaAmount; j++) {
                meta = buffer.readByte();
                DataManager.lockItemClient(
                        registryName, 
                        meta, 
                        "");
                metaItem = DataManager.getClient(registryName).getMetaItem(meta);
                metaItem.setCanBeDroppedOnDeath(buffer.readBoolean());
            }
            DataManager.getClient(registryName).setMainMeta(mainMeta);
        }
    }

    @Override
    public void process(INetHandler netHandler) {
        if (this.action == EnumAction.REMOVE_ALL.ordinal())
            DataManager.clearDataClient();
    }

    public enum EnumAction {

        SYNC_ALL,
        SYNC_LATEST,
        REMOVE_ALL;
    }
}
