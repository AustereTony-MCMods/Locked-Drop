package austeretony.lockeddrop.common.commands;

import austeretony.lockeddrop.common.config.ConfigLoader;
import austeretony.lockeddrop.common.config.EnumConfigSettings;
import austeretony.lockeddrop.common.main.DataManager;
import austeretony.lockeddrop.common.main.EnumChatMessages;
import austeretony.lockeddrop.common.main.LatestItem;
import austeretony.lockeddrop.common.network.NetworkHandler;
import austeretony.lockeddrop.common.network.client.CPSyncData;
import austeretony.lockeddrop.common.reference.CommonReference;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;

public class CommandLD extends CommandBase {

    public static final String 
    NAME = "ld",
    USAGE = "/ld <arg>, type </ld help> for available arguments.";

    @Override
    public String getName() {               
        return NAME;
    }

    @Override
    public String getUsage(ICommandSender sender) {         
        return USAGE;
    }

    @Override
    public boolean checkPermission(MinecraftServer server, ICommandSender sender) {     
        return sender instanceof EntityPlayerMP && CommonReference.isOpped((EntityPlayerMP) sender);
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {  
        EnumCommandLDArgs arg;
        if (args.length != 1 || (arg = EnumCommandLDArgs.get(args[0])) == null)        
            throw new WrongUsageException(this.getUsage(sender));   
        EntityPlayer player = getCommandSenderAsPlayer(sender);  
        ItemStack itemStack = player.getHeldItemMainhand();
        DataManager.latestItem = new LatestItem(
                itemStack.getItem().getRegistryName(), 
                itemStack.getMetadata(), 
                itemStack.getUnlocalizedName() + ".name");
        switch (arg) {
        case HELP:
            EnumChatMessages.COMMAND_LD_HELP.sendMessage(player);
            break;
        case ENABLE_SETTINGS:
            DataManager.setSettingsEnabled(true);
            this.save();
            this.sync(CPSyncData.EnumAction.SYNC_STATUS);
            EnumChatMessages.COMMAND_LD_ENABLE.sendMessage(player);
            break;
        case DISABLE_SETTINGS:
            DataManager.setSettingsEnabled(false);
            this.save();
            this.sync(CPSyncData.EnumAction.SYNC_STATUS);
            EnumChatMessages.COMMAND_LD_DISABLE.sendMessage(player);
            break;  
        case STATUS:
            EnumChatMessages.COMMAND_LD_STATUS.sendMessage(player);
            break; 
        case LIST:
            EnumChatMessages.COMMAND_LD_STATUS.sendMessage(player);
            EnumChatMessages.COMMAND_LD_LIST.sendMessage(player);
            break;
        case DENY_GLOBAL:
            if (!this.validAction(player, true, false, false, false))  break;
            DataManager.disableDropGlobalLatestItemServer();
            this.save();
            this.sync(CPSyncData.EnumAction.SYNC_LATEST);
            EnumChatMessages.COMMAND_LD_DENY_GLOBAL.sendMessage(player);
            break;
        case ALLOW_GLOBAL:
            if (!this.validAction(player, true, true, false, false)) break;
            DataManager.enableDropGlobalLatestItemServer();
            this.save();
            this.sync(CPSyncData.EnumAction.REMOVE_LATEST);
            EnumChatMessages.COMMAND_LD_ALLOW_GLOBAL.sendMessage(player);
            break;
        case DENY_SUBTYPES:
            if (!this.validAction(player, true, true, false, false))  break;
            DataManager.getServer(DataManager.latestItem.registryName).setMainMeta(DataManager.latestItem.meta);
            this.save();
            this.sync(CPSyncData.EnumAction.SYNC_LATEST);
            EnumChatMessages.COMMAND_LD_DENY_SUBTYPES.sendMessage(player);
            break;
        case ALLOW_SUBTYPES:
            if (!this.validAction(player, true, true, false, false))  break;
            DataManager.getServer(DataManager.latestItem.registryName).resetMainMeta();
            this.save();
            this.sync(CPSyncData.EnumAction.SYNC_LATEST);
            EnumChatMessages.COMMAND_LD_ALLOW_SUBTYPES.sendMessage(player);
            break;
        case DENY_THIS:
            if (!this.validAction(player, true, false, false, false))  break;
            DataManager.disableDropHeldItemNBTServer(player);
            EnumChatMessages.COMMAND_LD_DENY_THIS.sendMessage(player);
            break;
        case ALLOW_THIS:
            if (!this.validAction(player, true, false, true, false)) break;
            DataManager.enableDropHeldItemNBTServer(player);           
            EnumChatMessages.COMMAND_LD_ALLOW_THIS.sendMessage(player);
            break;
        case CLEAR_ALL:
            if (!this.validAction(player, false, false, false, true)) break;
            DataManager.clearDataServer();
            this.save();
            this.sync(CPSyncData.EnumAction.CLEAR_LIST);
            EnumChatMessages.COMMAND_LD_CLEAR_ALL.sendMessage(player);
            break;
        case SAVE:
            if (!this.validAction(player, false, false, false, true)) break;
            ConfigLoader.save();
            EnumChatMessages.COMMAND_LD_SAVE.sendMessage(player);
            break;
        case RELOAD:
            if (!this.validAction(player, false, false, false, false)) break;
            DataManager.init();
            this.sync(CPSyncData.EnumAction.SYNC_STATUS);
            this.sync(CPSyncData.EnumAction.SYNC_TOOLTIPS);
            this.sync(CPSyncData.EnumAction.SYNC_LIST);
            EnumChatMessages.COMMAND_LD_RELOAD.sendMessage(player);
            break;
        case BACKUP:
            if (!this.validAction(player, false, false, false, true)) break;
            ConfigLoader.backup();
            EnumChatMessages.COMMAND_LD_BACKUP.sendMessage(player);
            break;
        }
    }

    private boolean validAction(EntityPlayer player, boolean checkItemInHand, boolean checkItemLockedGlobal, boolean checkItemLockedNBT, boolean checkDataExist) {
        if (!EnumConfigSettings.EXTERNAL_CONFIG.isEnabled()) {
            EnumChatMessages.COMMAND_LD_ERR_EXTERNAL_CONFIG_DISABLED.sendMessage(player);
            return false;
        } else if (checkItemInHand && CommonReference.isMainHandEmpty(player)) {
            EnumChatMessages.COMMAND_LD_ERR_MAIN_HAND_EMPTY.sendMessage(player);
            return false;
        } else if (checkItemLockedGlobal && !DataManager.existMetaLatestServer()) {
            EnumChatMessages.COMMAND_LD_ERR_NO_DATA_LATEST.sendMessage(player);
            return false;
        } else if (checkItemLockedNBT && !CommonReference.isMainHandEmpty(player) 
                && (!player.getHeldItemMainhand().hasTagCompound() 
                        || (player.getHeldItemMainhand().hasTagCompound() && !player.getHeldItemMainhand().getTagCompound().hasKey(DataManager.NBT_TAG_KEEP_ON_DEATH)))) {
            EnumChatMessages.COMMAND_LD_ERR_NO_NBT_TAG.sendMessage(player);
            return false;
        } else if (checkDataExist && !DataManager.hasDataServer()) {
            EnumChatMessages.COMMAND_LD_ERR_NO_DATA.sendMessage(player);
            return false;
        }
        return true;
    }

    private void save() {
        if (EnumConfigSettings.AUTOSAVE.isEnabled())
            ConfigLoader.save();
    }

    private void sync(CPSyncData.EnumAction enumAction) {
        if (!EnumConfigSettings.SERVER_ONLY.isEnabled() && EnumConfigSettings.SETTINGS_TOOLTIPS.isEnabled())
            for (EntityPlayerMP playerMP : CommonReference.getPlayersListServer())
                NetworkHandler.sendTo(new CPSyncData(enumAction), playerMP);
    }
}
