package austeretony.lockeddrop.common.core;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.ClassNode;

import net.minecraft.launchwrapper.IClassTransformer;

public class LockedDropClassTransformer implements IClassTransformer {

    public static final Logger CORE_LOGGER = LogManager.getLogger("Locked Drop Core");

    @Override
    public byte[] transform(String name, String transformedName, byte[] basicClass) {   
        switch (transformedName) {
        case "net.minecraft.entity.player.EntityPlayerMP":                    
            return patch(basicClass, EnumInputClasses.MC_ENTITY_PLAYER_MP); 
        case "net.minecraft.entity.player.InventoryPlayer":                    
            return patch(basicClass, EnumInputClasses.MC_INVENTORY_PLAYER);       

        case "baubles.common.event.EventHandlerEntity":                    
            return patch(basicClass, EnumInputClasses.BAUBLES_EVENT_HANDLER_ENTITY);       
        }       
        return basicClass;
    }

    private byte[] patch(byte[] basicClass, EnumInputClasses enumInput) {
        ClassNode classNode = new ClassNode();
        ClassReader classReader = new ClassReader(basicClass);
        classReader.accept(classNode, enumInput.readerFlags);
        if (enumInput.patch(classNode))
            CORE_LOGGER.info(enumInput.domain + " <" + enumInput.clazz + ".class> patched!");
        ClassWriter writer = new ClassWriter(enumInput.writerFlags);        
        classNode.accept(writer);
        return writer.toByteArray();  
    }
}

