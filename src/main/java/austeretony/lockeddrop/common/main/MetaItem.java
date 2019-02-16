package austeretony.lockeddrop.common.main;

public class MetaItem {

    public final int meta;

    public final String unlocalizedName;

    private boolean dropOnDeath;//currently unused

    public MetaItem(int meta, String unlocalizedName) {
        this.meta = meta;
        this.unlocalizedName = unlocalizedName;
    }

    public boolean canBeDroppedOnDeath() {
        return this.dropOnDeath;
    }

    public void setCanBeDroppedOnDeath(boolean flag) {
        this.dropOnDeath = flag;
    }
}
