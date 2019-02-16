package austeretony.lockeddrop.common.main;

import net.minecraft.util.ResourceLocation;

public class LatestItem {

    public final ResourceLocation registryName;

    public final int meta;

    public final String unlocalizedName;

    public LatestItem(ResourceLocation registryName, int meta, String unlocalizedName) {
        this.registryName = registryName;
        this.meta = meta;
        this.unlocalizedName = unlocalizedName;
    }
}
