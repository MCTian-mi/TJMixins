package tj.asm.util;

import net.minecraftforge.fml.relauncher.FMLLaunchHandler;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.MethodInsnNode;
import tj.asm.LoadingPlugin;

/// Copied from Fixeroo
public abstract class ExplicitTransformer implements Opcodes {

    protected static ClassNode read(String transformedName, byte[] basicClass) {
        LoadingPlugin.LOGGER.info("Transforming {}", transformedName);
        ClassReader reader = new ClassReader(basicClass);
        ClassNode cls = new ClassNode();
        reader.accept(cls, 0);
        return cls;
    }

    protected static byte[] write(ClassNode cls) {
        return write(cls, 0);
    }

    protected static byte[] write(ClassNode cls, int writerOptions) {
        ClassWriter writer = new ClassWriter(writerOptions);
        cls.accept(writer);
        return writer.toByteArray();
    }

    protected static MethodInsnNode hook(String name, String desc) {
        return new MethodInsnNode(INVOKESTATIC, "tj/util/TJHooks", name, desc, false);
    }

    protected static String getName(String mcp, String srg) {
        return FMLLaunchHandler.isDeobfuscatedEnvironment() ? mcp : srg;
    }

    protected static AbstractInsnNode getReturn(InsnList instructions) {
        AbstractInsnNode node = instructions.getLast();
        while (node.getOpcode() != RETURN) node = node.getPrevious();
        return node;
    }
}
