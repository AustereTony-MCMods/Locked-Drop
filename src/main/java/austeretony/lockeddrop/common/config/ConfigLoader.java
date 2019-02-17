package austeretony.lockeddrop.common.config;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import austeretony.lockeddrop.common.enchantments.EnumEnchantmentProperties;
import austeretony.lockeddrop.common.main.DataManager;
import austeretony.lockeddrop.common.main.LockedDropMain;
import austeretony.lockeddrop.common.main.LockedItem;
import austeretony.lockeddrop.common.main.MetaItem;
import austeretony.lockeddrop.common.reference.CommonReference;
import austeretony.lockeddrop.common.util.LDUtils;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.util.ResourceLocation;

public class ConfigLoader {

    private static final String 
    EXT_CONFIGURATION_FILE = CommonReference.getGameFolder() + "/config/lockeddrop/config.json",
    EXT_DATA_FILE = CommonReference.getGameFolder() + "/config/lockeddrop/locked_items.json",
    EXT_ENCH_FILE = CommonReference.getGameFolder() + "/config/lockeddrop/enchantments.json";

    private static final DateFormat BACKUP_DATE_FORMAT = new SimpleDateFormat("yy_MM_dd-HH-mm-ss");

    public static void loadEnchantmentProperties() {
        JsonObject internalConfig;
        try {       
            internalConfig = (JsonObject) LDUtils.getInternalJsonData("assets/lockeddrop/config.json");  
        } catch (IOException exception) {       
            LockedDropMain.LOGGER.error("Internal configuration files damaged!");
            exception.printStackTrace();
            return;
        }
        if (EnumConfigSettings.EXTERNAL_CONFIG.initBoolean(internalConfig)) {
            Path configPath = Paths.get(EXT_CONFIGURATION_FILE);      
            if (Files.exists(configPath)) {
                JsonObject externalConfig;
                try {       
                    externalConfig = updateConfig(internalConfig); 
                } catch (IOException exception) {       
                    LockedDropMain.LOGGER.error("External configuration files damaged!");
                    exception.printStackTrace();
                    return;
                }
                if (EnumConfigSettings.ENABLE_ENCHANTMENTS.initBoolean(externalConfig))
                    loadExternalEnchantments();
            }
        } else {
            if (EnumConfigSettings.ENABLE_ENCHANTMENTS.initBoolean(internalConfig))
                loadInternalEnchantments();
        }
    }

    private static void loadInternalEnchantments() {
        JsonArray config;
        try {       
            config = (JsonArray) LDUtils.getInternalJsonData("assets/lockeddrop/enchantments.json");  
        } catch (IOException exception) {     
            LockedDropMain.LOGGER.error("Internal enchantments configuration file damaged!");
            exception.printStackTrace();
            return;
        }
        loadEnchantments(config);
    }

    private static void loadExternalEnchantments() {
        Path configPath = Paths.get(EXT_ENCH_FILE);      
        if (Files.exists(configPath)) {
            JsonArray config;
            try {                   
                config = (JsonArray) LDUtils.getExternalJsonData(EXT_ENCH_FILE);   
            } catch (IOException exception) {  
                LockedDropMain.LOGGER.error("External enchantments configuration file damaged!");
                exception.printStackTrace();
                return;
            }       
            loadEnchantments(config);
        } else {                
            try {               
                Files.createDirectories(configPath.getParent());
                JsonArray config = (JsonArray) LDUtils.getInternalJsonData("assets/lockeddrop/enchantments.json");
                LDUtils.createExternalJsonFile(EXT_ENCH_FILE, config);    
                loadEnchantments(config);
            } catch (IOException exception) {               
                exception.printStackTrace();
            }                       
        }
    }

    private static void loadEnchantments(JsonArray config) { 
        JsonObject enchObject;
        EnumEnchantmentProperties enumProp;
        Enchantment.Rarity rarity;
        EnumEnchantmentType type;
        EntityEquipmentSlot[] equipmentSlots;
        for (JsonElement enchElement : config) {
            enchObject = enchElement.getAsJsonObject();
            enumProp = EnumEnchantmentProperties.getOf(enchObject.get("id").getAsString());
            enumProp.setEnabled(enchObject.get("enabled").getAsBoolean());
            enumProp.setName(enchObject.get("name").getAsString());
            rarity = Enchantment.Rarity.valueOf(enchObject.get("rarity").getAsString());
            enumProp.setRarity(rarity == null ? Enchantment.Rarity.COMMON : rarity);
            enumProp.setMinEnchantability(enchObject.get("min_ench_level").getAsInt());
            enumProp.setMaxEnchantability(enchObject.get("max_ench_level").getAsInt());
            type = EnumEnchantmentType.valueOf(enchObject.get("valid_for").getAsString());
            enumProp.setType(type == null ? EnumEnchantmentType.ALL : type);
            enumProp.setEquipmentSlots(getSlots(enchObject.get("valid_equipment_slots").getAsJsonArray()));  
            for (JsonElement incompatElement : enchObject.get("incompatible_with").getAsJsonArray())
                enumProp.addIncompatibleEnchantment(new ResourceLocation(incompatElement.getAsString()));
            for (JsonElement incompatElement : enchObject.get("invalid_items").getAsJsonArray())
                enumProp.addIvalidItem(new ResourceLocation(incompatElement.getAsString()));
        }
    }

    private static EntityEquipmentSlot[] getSlots(JsonArray jsonArray) {
        EntityEquipmentSlot[] slots = new EntityEquipmentSlot[jsonArray.size()];
        String slotName;
        int i = 0;
        EntityEquipmentSlot slot;
        for (JsonElement slotElement : jsonArray) {
            slotName = slotElement.getAsString();
            slot = EntityEquipmentSlot.valueOf(slotName);
            if (slot == null)
                slot = EntityEquipmentSlot.MAINHAND;
            slots[i] = slot;
            i++;
        }
        if (slots.length == 0)
            slots = new EntityEquipmentSlot[] {EntityEquipmentSlot.MAINHAND};     
        return slots;
    }

    public static void load() {
        LockedDropMain.LOGGER.error("Loading data...");
        JsonObject internalConfig, internalSettings;
        try {       
            internalConfig = (JsonObject) LDUtils.getInternalJsonData("assets/lockeddrop/config.json");  
            internalSettings = (JsonObject) LDUtils.getInternalJsonData("assets/lockeddrop/locked_items.json");  
        } catch (IOException exception) {       
            LockedDropMain.LOGGER.error("Internal configuration files damaged!");
            exception.printStackTrace();
            return;
        }
        if (EnumConfigSettings.EXTERNAL_CONFIG.isEnabled()) {               
            loadExternalConfig(internalConfig, internalSettings);
        } else                  
            loadData(internalConfig, internalSettings);
    }

    private static void loadExternalConfig(JsonObject internalConfig, JsonObject internalSettings) {
        Path configPath = Paths.get(EXT_CONFIGURATION_FILE);      
        if (Files.exists(configPath)) {
            JsonObject externalConfig, externalSettings;
            try {                   
                externalConfig = updateConfig(internalConfig);    
                externalSettings = (JsonObject) LDUtils.getExternalJsonData(EXT_DATA_FILE);       
            } catch (IOException exception) {  
                LockedDropMain.LOGGER.error("External configuration file damaged!");
                exception.printStackTrace();
                return;
            }       
            loadData(externalConfig, externalSettings);
        } else {                
            Path dataPath = Paths.get(EXT_DATA_FILE);      
            try {               
                Files.createDirectories(configPath.getParent());                                
                Files.createDirectories(dataPath.getParent());                            
                createExternalCopyAndLoad(internalConfig, internalSettings);  
            } catch (IOException exception) {               
                exception.printStackTrace();
            }                       
        }
    }

    private static JsonObject updateConfig(JsonObject internalConfig) throws IOException {
        JsonObject externalConfigOld, externalConfigNew, externalGroupNew;
        try {                   
            externalConfigOld = (JsonObject) LDUtils.getExternalJsonData(EXT_CONFIGURATION_FILE);       
        } catch (IOException exception) {  
            LockedDropMain.LOGGER.error("External configuration file damaged!");
            exception.printStackTrace();
            return null;
        }
        JsonElement versionElement = externalConfigOld.get("version");
        if (versionElement == null || isOutdated(versionElement.getAsString(), LockedDropMain.VERSION)) {
            LockedDropMain.LOGGER.info("Updating external config file...");
            externalConfigNew = new JsonObject();
            externalConfigNew.add("version", new JsonPrimitive(LockedDropMain.VERSION));
            Map<String, JsonElement> 
            internalData = new LinkedHashMap<String, JsonElement>(),
            externlDataOld = new HashMap<String, JsonElement>(),
            internalGroup, externlGroupOld;
            for (Map.Entry<String, JsonElement> entry : internalConfig.entrySet())
                internalData.put(entry.getKey(), entry.getValue());
            for (Map.Entry<String, JsonElement> entry : externalConfigOld.entrySet())
                externlDataOld.put(entry.getKey(), entry.getValue());      
            for (String key : internalData.keySet()) {
                internalGroup = new LinkedHashMap<String, JsonElement>();
                externlGroupOld = new HashMap<String, JsonElement>();
                externalGroupNew = new JsonObject();
                for (Map.Entry<String, JsonElement> entry : internalData.get(key).getAsJsonObject().entrySet())
                    internalGroup.put(entry.getKey(), entry.getValue());
                if (externlDataOld.containsKey(key)) {                    
                    for (Map.Entry<String, JsonElement> entry : externlDataOld.get(key).getAsJsonObject().entrySet())
                        externlGroupOld.put(entry.getKey(), entry.getValue());   
                    for (String k : internalGroup.keySet()) {
                        if (externlGroupOld.containsKey(k))
                            externalGroupNew.add(k, externlGroupOld.get(k));
                        else 
                            externalGroupNew.add(k, internalGroup.get(k));
                    }
                } else {
                    for (String k : internalGroup.keySet())
                        externalGroupNew.add(k, internalGroup.get(k));
                }
                externalConfigNew.add(key, externalGroupNew);
                LDUtils.createExternalJsonFile(EXT_CONFIGURATION_FILE, externalConfigNew);
            }
            return externalConfigNew;
        }
        LockedDropMain.LOGGER.info("External config up-to-date!");
        return externalConfigOld;
    }

    private static void createExternalCopyAndLoad(JsonObject internalConfig, JsonObject internalSettings) {       
        try {           
            LDUtils.createExternalJsonFile(EXT_CONFIGURATION_FILE, internalConfig); 
            LDUtils.createExternalJsonFile(EXT_DATA_FILE, internalSettings);                                                                                                    
        } catch (IOException exception) {               
            exception.printStackTrace();
        }
        loadData(internalConfig, internalSettings);
    }

    private static void loadData(JsonObject configFile, JsonObject settingsFile) {          
        EnumConfigSettings.AUTOSAVE.initBoolean(configFile); 
        EnumConfigSettings.SETTINGS_TOOLTIPS.initBoolean(configFile); 
        JsonObject itemObject, metaObject;
        ResourceLocation registryName;
        int meta;
        MetaItem metaItem;
        for (Map.Entry<String, JsonElement> plantEntry : settingsFile.entrySet()) {
            if (plantEntry.getKey().equals("enabled")) {
                DataManager.setSettingsEnabled(settingsFile.get("enabled").getAsBoolean());
                continue;
            }
            itemObject = plantEntry.getValue().getAsJsonObject();
            registryName = new ResourceLocation(plantEntry.getKey());
            for (Map.Entry<String, JsonElement> metaEntry : itemObject.entrySet()) { 
                if (metaEntry.getKey().equals("main_meta")) continue;
                meta = Integer.parseInt(metaEntry.getKey());
                metaObject = metaEntry.getValue().getAsJsonObject();
                DataManager.lockItemServer(
                        registryName,
                        Integer.parseInt(metaEntry.getKey()), 
                        metaObject.get("unlocalized").getAsString());
                metaItem = DataManager.getServer(registryName).getMetaItem(meta);
                metaItem.setCanBeDroppedOnDeath(metaObject.get("drop_on_death").getAsBoolean());
            }
            DataManager.getServer(registryName).setMainMeta(itemObject.get("main_meta").getAsInt());
        }
    }

    public static void save() {
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .create();  
        JsonObject 
        dataObject = new JsonObject(),
        plantObject, metaObject;
        dataObject.add("enabled", new JsonPrimitive(DataManager.isSettingsEnabled()));
        for (LockedItem lockedItem : DataManager.getItemsServer().values()) {
            plantObject = new JsonObject();
            plantObject.add("main_meta", new JsonPrimitive(lockedItem.getMainMeta()));
            for (MetaItem metaItem : lockedItem.getData().values()) {
                metaObject = new JsonObject();
                metaObject.add("unlocalized", new JsonPrimitive(metaItem.unlocalizedName));
                metaObject.add("drop_on_death", new JsonPrimitive(metaItem.canBeDroppedOnDeath()));
                plantObject.add(String.valueOf(metaItem.meta), metaObject);
            }
            dataObject.add(lockedItem.registryName.toString(), plantObject);
        } 
        try (Writer writer = new FileWriter(EXT_DATA_FILE)) {             
            gson.toJson(dataObject, writer);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public static void backup() {
        try {
            LDUtils.createExternalJsonFile(
                    CommonReference.getGameFolder() + "/config/lockeddrop/locked_items_" + BACKUP_DATE_FORMAT.format(new Date()) + ".json", 
                    EnumConfigSettings.EXTERNAL_CONFIG.isEnabled() ? LDUtils.getExternalJsonData(EXT_DATA_FILE) : LDUtils.getInternalJsonData("assets/lockeddrop/locked_items.json"));         
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public static boolean isOutdated(String currentVersion, String availableVersion) {                                                               
        String[] 
                cVer = currentVersion.split("[.]"),
                aVer = availableVersion.split("[.]");                           
        int diff;               
        for (int i = 0; i < cVer.length; i++) {                                 
            try {                               
                diff = Integer.parseInt(aVer[i]) - Integer.parseInt(cVer[i]);                                                                                           
                if (diff > 0)
                    return true;                                
                if (diff < 0)
                    return false;
            } catch (NumberFormatException exception) {                         
                exception.printStackTrace();
            }
        }               
        return false;
    }
}
