package austeretony.lockeddrop.common.config;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public enum EnumConfigSettings {

    EXTERNAL_CONFIG("main", "external_config"),
    SETTINGS_TOOLTIPS("main", "settings_tooltips"),
    AUTOSAVE("settings", "enable_autosave");

    public final String configSection, configKey;

    private boolean isEnabled;

    EnumConfigSettings(String configSection, String configKey) {
        this.configSection = configSection;
        this.configKey = configKey;
    }

    public boolean isEnabled() {
        return this.isEnabled;
    }

    private JsonElement init(JsonObject jsonObject) {
        return jsonObject.get(this.configSection).getAsJsonObject().get(this.configKey);
    }

    public boolean initBoolean(JsonObject jsonObject) {
        return this.isEnabled = this.init(jsonObject).getAsBoolean();
    }
}
