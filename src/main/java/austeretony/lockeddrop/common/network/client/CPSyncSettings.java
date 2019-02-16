package austeretony.lockeddrop.common.network.client;

import austeretony.lockeddrop.common.config.EnumConfigSettings;
import austeretony.lockeddrop.common.main.DataManager;
import austeretony.lockeddrop.common.network.ProxyPacket;
import net.minecraft.network.INetHandler;
import net.minecraft.network.PacketBuffer;

public class CPSyncSettings extends ProxyPacket {

    private boolean isSettingsEnabled, allowTooltips;

    public CPSyncSettings() {
        this.isSettingsEnabled = DataManager.isSettingsEnabled();
        this.allowTooltips = EnumConfigSettings.SETTINGS_TOOLTIPS.isEnabled();
    }

    @Override
    public void write(PacketBuffer buffer) {  
        buffer.writeBoolean(this.isSettingsEnabled);
        buffer.writeBoolean(this.allowTooltips);
    }

    @Override
    public void read(PacketBuffer buffer) { 
        this.isSettingsEnabled = buffer.readBoolean();
        this.allowTooltips = buffer.readBoolean();
    }

    @Override
    public void process(INetHandler netHandler) {
        DataManager.setSettingsEnabledClient(this.isSettingsEnabled);
        DataManager.setSettingsTooltipsAllowedClient(this.allowTooltips);
    }
}
