package austeretony.lockeddrop.common.core;

import java.util.Iterator;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

public enum EnumInputClasses {

    MC_LOCALE("Minecraft", "Locale", 0, ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES),
    MC_ENTITY_PLAYER_MP("Minecraft", "EntityPlayerMP", 0, ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES),
    MC_INVENTORY_PLAYER("Minecraft", "InventoryPlayer", 0, ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES),

    BAUBLES_EVENT_HANDLER_ENTITY("Baubles", "EventHandlerEntity", 0, 0);


    private static final String HOOKS_CLASS = "austeretony/lockeddrop/common/core/LockedDropHooks";

    public final String domain, clazz;

    public final int readerFlags, writerFlags;

    EnumInputClasses(String domain, String clazz, int readerFlags, int writerFlags) {
        this.domain = domain;
        this.clazz = clazz;
        this.readerFlags = readerFlags;
        this.writerFlags = writerFlags;
    }

    public boolean patch(ClassNode classNode) {
        switch (this) {
        case MC_LOCALE:
            return pathcMCLocale(classNode);
        case MC_ENTITY_PLAYER_MP:
            return patchMCEntityPlayerMP(classNode);
        case MC_INVENTORY_PLAYER:
            return patchMCInventoryPlayer(classNode);

        case BAUBLES_EVENT_HANDLER_ENTITY:
            return patchBaublesEventHandlerEntity(classNode);
        }
        return false;
    }

    private boolean pathcMCLocale(ClassNode classNode) {
        String
        propertiesFieldName = LockedDropCorePlugin.isObfuscated() ? "a" : "properties",
                loadLocaleDataFilesMethodName = LockedDropCorePlugin.isObfuscated() ? "a" : "loadLocaleDataFiles",
                        localeClassName = LockedDropCorePlugin.isObfuscated() ? "cfb" : "net/minecraft/client/resources/Locale",
                                iResourceManagerClassName = LockedDropCorePlugin.isObfuscated() ? "cep" : "net/minecraft/client/resources/IResourceManager",
                                        listClassName = "java/util/List",
                                        mapClassName = "java/util/Map";
        boolean isSuccessful = false;   
        int invokespecialCount = 0;
        AbstractInsnNode currentInsn;

        for (MethodNode methodNode : classNode.methods) {               
            if (methodNode.name.equals(loadLocaleDataFilesMethodName) && methodNode.desc.equals("(L" + iResourceManagerClassName + ";L" + listClassName + ";)V")) {                         
                Iterator<AbstractInsnNode> insnIterator = methodNode.instructions.iterator();              
                while (insnIterator.hasNext()) {                        
                    currentInsn = insnIterator.next();                  
                    if (currentInsn.getOpcode() == Opcodes.INVOKESPECIAL) {    
                        invokespecialCount++;
                        if (invokespecialCount == 3) {
                            InsnList nodesList = new InsnList();   
                            nodesList.add(new VarInsnNode(Opcodes.ALOAD, 2));
                            nodesList.add(new VarInsnNode(Opcodes.ALOAD, 0));
                            nodesList.add(new FieldInsnNode(Opcodes.GETFIELD, localeClassName, propertiesFieldName, "L" + mapClassName + ";"));
                            nodesList.add(new MethodInsnNode(Opcodes.INVOKESTATIC, HOOKS_CLASS, "loadCustomLocalization", "(L" + listClassName + ";L" + mapClassName + ";)V", false));
                            methodNode.instructions.insertBefore(currentInsn.getPrevious(), nodesList); 
                            isSuccessful = true;                        
                            break;
                        }
                    }
                }    
                break;
            }
        }
        return isSuccessful;
    }

    private boolean patchMCEntityPlayerMP(ClassNode classNode) {
        String
        copyFromMethodName = LockedDropCorePlugin.isObfuscated() ? "a" : "copyFrom",
                entityPlayerMPClassName = LockedDropCorePlugin.isObfuscated() ? "oq" : "net/minecraft/entity/player/EntityPlayerMP";
        boolean isSuccessful = false;   
        AbstractInsnNode currentInsn;

        for (MethodNode methodNode : classNode.methods) {               
            if (methodNode.name.equals(copyFromMethodName) && methodNode.desc.equals("(L" + entityPlayerMPClassName + ";Z)V")) {                         
                Iterator<AbstractInsnNode> insnIterator = methodNode.instructions.iterator();              
                while (insnIterator.hasNext()) {                        
                    currentInsn = insnIterator.next();                  
                    if (currentInsn.getOpcode() == Opcodes.RETURN) {    
                        InsnList nodesList = new InsnList();   
                        nodesList.add(new VarInsnNode(Opcodes.ALOAD, 0));
                        nodesList.add(new VarInsnNode(Opcodes.ALOAD, 1));
                        nodesList.add(new MethodInsnNode(Opcodes.INVOKESTATIC, HOOKS_CLASS, "copyInventory", "(L" + entityPlayerMPClassName + ";L" + entityPlayerMPClassName + ";)V", false));
                        methodNode.instructions.insertBefore(currentInsn, nodesList); 
                        isSuccessful = true;                        
                        break;
                    }
                }    
                break;
            }
        }
        return isSuccessful;
    }

    private boolean patchMCInventoryPlayer(ClassNode classNode) {
        String
        dropAllItemsMethodName = LockedDropCorePlugin.isObfuscated() ? "o" : "dropAllItems",
                itemStackClassName = LockedDropCorePlugin.isObfuscated() ? "aip" : "net/minecraft/item/ItemStack";
        boolean isSuccessful = false;   
        AbstractInsnNode currentInsn;

        for (MethodNode methodNode : classNode.methods) {               
            if (methodNode.name.equals(dropAllItemsMethodName) && methodNode.desc.equals("()V")) {                         
                Iterator<AbstractInsnNode> insnIterator = methodNode.instructions.iterator();              
                while (insnIterator.hasNext()) {                        
                    currentInsn = insnIterator.next();                  
                    if (currentInsn.getOpcode() == Opcodes.IFNE) {                             
                        InsnList nodesList = new InsnList();   
                        nodesList.add(new VarInsnNode(Opcodes.ALOAD, 4));
                        nodesList.add(new MethodInsnNode(Opcodes.INVOKESTATIC, HOOKS_CLASS, "canItemStackBeDropped", "(L" + itemStackClassName + ";)Z", false));
                        nodesList.add(new JumpInsnNode(Opcodes.IFEQ, ((JumpInsnNode) currentInsn).label));
                        methodNode.instructions.insertBefore(currentInsn.getPrevious().getPrevious(), nodesList); 
                        isSuccessful = true;                        
                        break;
                    }
                }    
                break;
            }
        }
        return isSuccessful;
    }

    private boolean patchBaublesEventHandlerEntity(ClassNode classNode) {
        String
        dropItemsAtMethodName = "dropItemsAt",
        getStackInSlotMethodName = "getStackInSlot",
        itemStackClassName = "net/minecraft/item/ItemStack",
        iBaubleItemHandlerClassName = "baubles/api/cap/IBaublesItemHandler";
        boolean isSuccessful = false;   
        AbstractInsnNode currentInsn;

        for (MethodNode methodNode : classNode.methods) {               
            if (methodNode.name.equals(dropItemsAtMethodName)) {                         
                Iterator<AbstractInsnNode> insnIterator = methodNode.instructions.iterator();              
                while (insnIterator.hasNext()) {                        
                    currentInsn = insnIterator.next();                  
                    if (currentInsn.getOpcode() == Opcodes.IFNULL) {                             
                        InsnList nodesList = new InsnList();   
                        nodesList.add(new VarInsnNode(Opcodes.ALOAD, 4));
                        nodesList.add(new VarInsnNode(Opcodes.ILOAD, 5));
                        nodesList.add(new MethodInsnNode(Opcodes.INVOKEINTERFACE, iBaubleItemHandlerClassName, getStackInSlotMethodName, "(I)L" + itemStackClassName + ";", true));
                        nodesList.add(new MethodInsnNode(Opcodes.INVOKESTATIC, HOOKS_CLASS, "canItemStackBeDropped", "(L" + itemStackClassName + ";)Z", false));
                        nodesList.add(new JumpInsnNode(Opcodes.IFEQ, ((JumpInsnNode) currentInsn).label));
                        methodNode.instructions.insertBefore(currentInsn.getPrevious().getPrevious().getPrevious(), nodesList); 
                        isSuccessful = true;                        
                        break;
                    }
                }    
                break;
            }
        }
        return isSuccessful;
    }
}
