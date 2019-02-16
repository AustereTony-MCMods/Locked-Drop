package austeretony.lockeddrop.common.main;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.util.ResourceLocation;

public class LockedItem {

    public final ResourceLocation registryName;

    private final Map<Integer, MetaItem> metaItems = new HashMap<Integer, MetaItem>();

    private int mainMeta = -1;

    private boolean hasMainMeta;

    public LockedItem(ResourceLocation registryName) {
        this.registryName = registryName;
    }

    public boolean hasData() {
        return !this.metaItems.isEmpty();
    }

    public Map<Integer, MetaItem> getData() {
        return this.metaItems;
    }

    public boolean hasMetaItem(int meta) {
        return this.hasData() && this.metaItems.containsKey(meta);
    }

    public MetaItem getMetaItem(int meta) {
        return this.metaItems.get(meta);
    }

    public void addMetaItem(int meta, MetaItem metaPlant) {
        this.metaItems.put(meta, metaPlant);
    }

    public void createMetaItem(int meta, String unlocalizedName) {
        this.metaItems.put(meta, new MetaItem(meta, unlocalizedName));
    }

    public void removeMetaItem(int meta) {
        this.metaItems.remove(meta);
    }

    public boolean hasMainMeta() {
        return this.hasMainMeta;
    }

    public int getMainMeta() {
        return this.mainMeta;
    }

    public MetaItem getMainMetaItem() {
        return this.metaItems.get(this.mainMeta);
    }

    public void setMainMeta(int mainMeta) {
        this.mainMeta = mainMeta;
        this.hasMainMeta = mainMeta != -1;
    }

    public void resetMainMeta() {
        this.mainMeta = -1;
        this.hasMainMeta = false;
    }
    
    private boolean verifyMeta(int meta) {
        return !this.metaItems.containsKey(meta) || this.metaItems.get(meta).canBeDroppedOnDeath();
    }
    
    public boolean canBeDroppedOnDeath(int meta) {
        return this.hasMainMeta ? this.verifyMeta(this.mainMeta) : this.verifyMeta(meta);
    }
}
