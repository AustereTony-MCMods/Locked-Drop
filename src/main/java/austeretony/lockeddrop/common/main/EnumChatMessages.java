package austeretony.lockeddrop.common.main;

import austeretony.lockeddrop.common.commands.EnumCommandLDArgs;
import austeretony.lockeddrop.common.reference.CommonReference;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;

public enum EnumChatMessages {

    COMMAND_LD_HELP,
    COMMAND_LD_ENABLE,
    COMMAND_LD_DISABLE,
    COMMAND_LD_STATUS,
    COMMAND_LD_SETTINGS,
    COMMAND_LD_DENY_GLOBAL,
    COMMAND_LD_ALLOW_GLOBAL,
    COMMAND_LD_DENY_SUBTYPES,
    COMMAND_LD_ALLOW_SUBTYPES,
    COMMAND_LD_DENY_THIS,
    COMMAND_LD_ALLOW_THIS,
    COMMAND_LD_CLEAR_ALL,
    COMMAND_LD_SAVE,
    COMMAND_LD_RELOAD,
    COMMAND_LD_BACKUP,
    COMMAND_LD_ERR_EXTERNAL_CONFIG_DISABLED,
    COMMAND_LD_ERR_MAIN_HAND_EMPTY,
    COMMAND_LD_ERR_NO_DATA_LATEST,
    COMMAND_LD_ERR_NO_NBT_TAG,
    COMMAND_LD_ERR_NO_DATA;

    public static final ITextComponent PREFIX;

    static {
        PREFIX = new TextComponentString("[" + LockedDropMain.NAME + "] ");
        PREFIX.getStyle().setColor(TextFormatting.AQUA);                   
    }

    private static ITextComponent prefix() {
        return PREFIX.createCopy();
    }

    public void sendMessage(EntityPlayer player, String... args) {
        ITextComponent msg1, msg2, msg3, msg4, msg5;
        switch (this) {
        case COMMAND_LD_HELP:
            CommonReference.sendMessage(player, prefix().appendSibling(new TextComponentTranslation("ld.command.help.title")));
            for (EnumCommandLDArgs arg : EnumCommandLDArgs.values()) {
                if (arg != EnumCommandLDArgs.HELP) {
                    msg1 = new TextComponentString("/ld " + arg);
                    msg2 = new TextComponentString(" - ");
                    msg1.getStyle().setColor(TextFormatting.GREEN);  
                    msg2.getStyle().setColor(TextFormatting.WHITE); 
                    CommonReference.sendMessage(player, msg1.appendSibling(msg2.appendSibling(new TextComponentTranslation("ld.command.help." + arg))));
                }
            }
            break;
        case COMMAND_LD_ENABLE:
            CommonReference.sendMessage(player, prefix().appendSibling(new TextComponentTranslation("ld.command.enable")));
            break;
        case COMMAND_LD_DISABLE:
            CommonReference.sendMessage(player, prefix().appendSibling(new TextComponentTranslation("ld.command.disable")));
            break;
        case COMMAND_LD_STATUS:
            msg1 = DataManager.isSettingsEnabled() ? new TextComponentTranslation("ld.command.status.enabled") : new TextComponentTranslation("ld.command.status.disabled");
            msg1.getStyle().setColor(DataManager.isSettingsEnabled() ? TextFormatting.GREEN : TextFormatting.RED);        
            CommonReference.sendMessage(player, prefix().appendSibling(new TextComponentTranslation("ld.command.status").appendSibling(new TextComponentString(": ")).appendSibling(msg1)));
            break;
        case COMMAND_LD_SETTINGS:
            COMMAND_LD_STATUS.sendMessage(player, args);
            CommonReference.sendMessage(player, prefix().appendSibling(new TextComponentTranslation("ld.command.settings")));
            if (DataManager.getItemsServer().isEmpty())
                CommonReference.sendMessage(player, new TextComponentTranslation("ld.command.settings.empty"));
            for (LockedItem lockedItem : DataManager.getItemsServer().values()) {
                for (MetaItem metaItem : lockedItem.getData().values()) {      
                    if (lockedItem.hasMainMeta() && lockedItem.getMainMeta() != metaItem.meta) continue;
                    msg1 = new TextComponentString(DataManager.createDisplayKey(lockedItem.registryName, metaItem.meta));
                    msg2 = new TextComponentString(" / ");
                    msg3 = new TextComponentTranslation((metaItem.unlocalizedName).trim());
                    msg1.getStyle().setColor(TextFormatting.WHITE); 
                    msg2.getStyle().setColor(TextFormatting.AQUA); 
                    msg3.getStyle().setColor(TextFormatting.WHITE); 
                    CommonReference.sendMessage(player, msg1.appendSibling(msg2).appendSibling(msg3));
                }
                if (lockedItem.hasMainMeta()) {
                    msg1 = new TextComponentTranslation("ld.command.settings.mainMetaSet");
                    msg1.getStyle().setColor(TextFormatting.YELLOW); 
                    CommonReference.sendMessage(player, new TextComponentString(" - ").appendSibling(msg1));
                }
            }
            break;
        case COMMAND_LD_DENY_GLOBAL:
            msg1 = new TextComponentTranslation("ld.command.deny-global");
            msg2 = new TextComponentString(": ");
            msg3 = new TextComponentString(DataManager.createDisplayKey(DataManager.latestItem.registryName, DataManager.latestItem.meta)); 
            msg4 = new TextComponentString(" / ");
            msg5 = new TextComponentTranslation(DataManager.latestItem.unlocalizedName.trim());
            msg3.getStyle().setColor(TextFormatting.WHITE); 
            msg4.getStyle().setColor(TextFormatting.AQUA);   
            msg5.getStyle().setColor(TextFormatting.WHITE);  
            CommonReference.sendMessage(player, prefix().appendSibling(msg1).appendSibling(msg2).appendSibling(msg3).appendSibling(msg4).appendSibling(msg5));
            break;
        case COMMAND_LD_ALLOW_GLOBAL:
            msg1 = new TextComponentTranslation("ld.command.allow-global");
            msg2 = new TextComponentString(": ");
            msg3 = new TextComponentString(DataManager.createDisplayKey(DataManager.latestItem.registryName, DataManager.latestItem.meta)); 
            msg4 = new TextComponentString(" / ");
            msg5 = new TextComponentTranslation(DataManager.latestItem.unlocalizedName.trim());
            msg3.getStyle().setColor(TextFormatting.WHITE); 
            msg4.getStyle().setColor(TextFormatting.AQUA);   
            msg5.getStyle().setColor(TextFormatting.WHITE);  
            CommonReference.sendMessage(player, prefix().appendSibling(msg1).appendSibling(msg2).appendSibling(msg3).appendSibling(msg4).appendSibling(msg5));
            break;
        case COMMAND_LD_DENY_SUBTYPES:
            msg1 = new TextComponentTranslation("ld.command.deny-subtypes");
            msg2 = new TextComponentString(": ");
            msg3 = new TextComponentString(DataManager.createDisplayKey(DataManager.latestItem.registryName, DataManager.latestItem.meta)); 
            msg4 = new TextComponentString(" / ");
            msg5 = new TextComponentTranslation(DataManager.latestItem.unlocalizedName.trim());
            msg3.getStyle().setColor(TextFormatting.WHITE); 
            msg4.getStyle().setColor(TextFormatting.AQUA);   
            msg5.getStyle().setColor(TextFormatting.WHITE);  
            CommonReference.sendMessage(player, prefix().appendSibling(msg1).appendSibling(msg2).appendSibling(msg3).appendSibling(msg4).appendSibling(msg5));
            break;
        case COMMAND_LD_ALLOW_SUBTYPES:
            msg1 = new TextComponentTranslation("ld.command.allow-subtypes");
            msg2 = new TextComponentString(": ");
            msg3 = new TextComponentString(DataManager.createDisplayKey(DataManager.latestItem.registryName, DataManager.latestItem.meta)); 
            msg4 = new TextComponentString(" / ");
            msg5 = new TextComponentTranslation(DataManager.latestItem.unlocalizedName.trim());
            msg3.getStyle().setColor(TextFormatting.WHITE); 
            msg4.getStyle().setColor(TextFormatting.AQUA);   
            msg5.getStyle().setColor(TextFormatting.WHITE);  
            CommonReference.sendMessage(player, prefix().appendSibling(msg1).appendSibling(msg2).appendSibling(msg3).appendSibling(msg4).appendSibling(msg5));
            break;
        case COMMAND_LD_DENY_THIS:
            msg1 = new TextComponentTranslation("ld.command.deny-this");
            msg2 = new TextComponentString(": ");
            msg3 = new TextComponentString(DataManager.createDisplayKey(DataManager.latestItem.registryName, DataManager.latestItem.meta)); 
            msg4 = new TextComponentString(" / ");
            msg5 = new TextComponentTranslation(DataManager.latestItem.unlocalizedName.trim());
            msg3.getStyle().setColor(TextFormatting.WHITE); 
            msg4.getStyle().setColor(TextFormatting.AQUA);   
            msg5.getStyle().setColor(TextFormatting.WHITE);  
            CommonReference.sendMessage(player, prefix().appendSibling(msg1).appendSibling(msg2).appendSibling(msg3).appendSibling(msg4).appendSibling(msg5));
            break;
        case COMMAND_LD_ALLOW_THIS:
            msg1 = new TextComponentTranslation("ld.command.allow-this");
            msg2 = new TextComponentString(": ");
            msg3 = new TextComponentString(DataManager.createDisplayKey(DataManager.latestItem.registryName, DataManager.latestItem.meta)); 
            msg4 = new TextComponentString(" / ");
            msg5 = new TextComponentTranslation(DataManager.latestItem.unlocalizedName.trim());
            msg3.getStyle().setColor(TextFormatting.WHITE); 
            msg4.getStyle().setColor(TextFormatting.AQUA);   
            msg5.getStyle().setColor(TextFormatting.WHITE);  
            CommonReference.sendMessage(player, prefix().appendSibling(msg1).appendSibling(msg2).appendSibling(msg3).appendSibling(msg4).appendSibling(msg5));
            break;
        case COMMAND_LD_CLEAR_ALL:
            CommonReference.sendMessage(player, prefix().createCopy().appendSibling(new TextComponentTranslation("ld.command.clear-all")));    
            break;
        case COMMAND_LD_SAVE:
            CommonReference.sendMessage(player, prefix().appendSibling(new TextComponentTranslation("ld.command.save")));    
            break;
        case COMMAND_LD_RELOAD:
            CommonReference.sendMessage(player, prefix().appendSibling(new TextComponentTranslation("ld.command.reload")));    
            break;
        case COMMAND_LD_BACKUP:
            CommonReference.sendMessage(player, prefix().appendSibling(new TextComponentTranslation("ld.command.backup")));    
            break;
        case COMMAND_LD_ERR_EXTERNAL_CONFIG_DISABLED:
            msg1 = new TextComponentTranslation("ld.command.err.externalConfigDisabled");                    
            msg1.getStyle().setColor(TextFormatting.RED);    
            CommonReference.sendMessage(player, prefix().appendSibling(msg1));  
            break;
        case COMMAND_LD_ERR_MAIN_HAND_EMPTY:
            msg1 = new TextComponentTranslation("ld.command.err.mainHandEmpty");                    
            msg1.getStyle().setColor(TextFormatting.RED);    
            CommonReference.sendMessage(player, prefix().appendSibling(msg1));  
            break;
        case COMMAND_LD_ERR_NO_DATA_LATEST:
            msg1 = new TextComponentTranslation("ld.command.err.noDataLatest");                    
            msg1.getStyle().setColor(TextFormatting.RED);    
            CommonReference.sendMessage(player, prefix().appendSibling(msg1));  
            break;
        case COMMAND_LD_ERR_NO_NBT_TAG:
            msg1 = new TextComponentTranslation("ld.command.err.noNBTTag");                    
            msg1.getStyle().setColor(TextFormatting.RED);    
            CommonReference.sendMessage(player, prefix().appendSibling(msg1));  
            break;
        case COMMAND_LD_ERR_NO_DATA:
            msg1 = new TextComponentTranslation("ld.command.err.noData");                    
            msg1.getStyle().setColor(TextFormatting.RED);    
            CommonReference.sendMessage(player, prefix().appendSibling(msg1));  
            break;
        }
    }
}
