package austeretony.lockeddrop.common.config;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public enum EnumConfigSettings {

    EXTERNAL_CONFIG(0, "main", "external_config", true),
    SETTINGS_TOOLTIPS(0, "main", "settings_tooltips"),
    ENCHANTMENTS(0, "main", "enable_enchantments"),
    CUSTOM_LOCALIZATION(0, "main", "enable_custom_localization"),
    AUTOSAVE(0, "settings", "enable_autosave");

    public final int type;//0 - boolean

    public final String configSection, configKey;

    public final boolean exclude;

    private boolean isEnabled;

    EnumConfigSettings(int type, String configSection, String configKey, boolean... exclude) {
        this.type = type;
        this.configSection = configSection;
        this.configKey = configKey;
        this.exclude = exclude.length > 0 ? exclude[0] : false;
    }

    public boolean isEnabled() {
        return this.isEnabled;
    }

    private JsonElement getValue(JsonObject jsonObject) {
        return jsonObject.get(this.configSection).getAsJsonObject().get(this.configKey);
    }

    public void initByType(JsonObject jsonObject) {
        switch (this.type) {
        case 0:
            this.isEnabled = this.getValue(jsonObject).getAsBoolean();
        }
    }

    public static void initAll(JsonObject config) {
        for (EnumConfigSettings enumSetting : values())
            if (!enumSetting.exclude)
                enumSetting.initByType(config);
    }
}
