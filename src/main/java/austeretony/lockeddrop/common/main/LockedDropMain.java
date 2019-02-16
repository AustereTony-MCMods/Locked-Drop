package austeretony.lockeddrop.common.main;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import austeretony.lockeddrop.common.commands.CommandLD;
import austeretony.lockeddrop.common.network.NetworkHandler;
import austeretony.lockeddrop.common.reference.CommonReference;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

@Mod(
        modid = LockedDropMain.MODID, 
        name = LockedDropMain.NAME, 
        version = LockedDropMain.VERSION,
        certificateFingerprint = "@FINGERPRINT@",
        updateJSON = LockedDropMain.VERSIONS_FORGE_URL)
public class LockedDropMain {

    public static final String 
    MODID = "lockeddrop",
    NAME = "Locked Drop",
    VERSION = "1.0.0",
    VERSIONS_FORGE_URL = "https://raw.githubusercontent.com/AustereTony-MCMods/Locked-Drop/info/mod_versions_forge.json";

    public static final Logger LOGGER = LogManager.getLogger(NAME);

    @EventHandler
    public void init(FMLInitializationEvent event) { 
        NetworkHandler.init();
        CommonReference.registerEvent(new LockedDropEvents());
    }

    @EventHandler
    public void serverStarting(FMLServerStartingEvent event) {  
        DataManager.initServerData();
        CommonReference.registerCommand(event, new CommandLD());
    }
}
