package austeretony.lockeddrop.common.commands;

public enum EnumCommandLDArgs {
    
    HELP("help"),
    DISABLE_SETTINGS("disable"),
    ENABLE_SETTINGS("enable"),
    STATUS("status"),
    LIST("list"),
    DENY_GLOBAL("deny-global"),
    ALLOW_GLOBAL("allow-global"),
    DENY_SUBTYPES("deny-subtypes"),
    ALLOW_SUBTYPES("allow-subtypes"),
    DENY_THIS("deny-this"),
    ALLOW_THIS("allow-this"),
    CLEAR_ALL("clear-all"),
    SAVE("save"),
    RELOAD("reload"),
    BACKUP("backup");

    public final String arg;

    EnumCommandLDArgs(String arg) {
        this.arg = arg;
    }

    public static EnumCommandLDArgs get(String strArg) {
        for (EnumCommandLDArgs arg : values())
            if (arg.arg.equals(strArg))
                return arg;
        return null;
    }

    @Override
    public String toString() {
        return this.arg;
    }
}
