package tj.asm.transformers;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;
import tj.asm.util.ExplicitTransformer;
import tj.util.TJHooks;

public class GuavaGraphTransformer extends ExplicitTransformer {

    public static final String TOPOSORT_CLASS_NAME = "net/neoforged/fml/loading/toposort/TopologicalSort";
    public static final String SCCD_CLASS_NAME = "net/neoforged/fml/loading/toposort/StronglyConnectedComponentDetector";

    public static final String TOPOSORT_METHOD_NAME = "topologicalSort";
    public static final String TOPOSORT_METHOD_DESC = "(Lcom/google/common/graph/Graph;Ljava/util/Comparator;)Ljava/util/List;";
    public static final String TOPOSORT_METHOD_DESC_TRANSFORMED = "(Lcom/google/common/graph/ValueGraph;Ljava/util/Comparator;)Ljava/util/List;";
    public static final String TOPOSORT_METHOD_SIGN = "<T:Ljava/lang/Object;>(Lcom/google/common/graph/Graph<TT;>;Ljava/util/Comparator<-TT;>;)Ljava/util/List<TT;>;";
    public static final String TOPOSORT_METHOD_SIGN_TRANSFORMED = "<T:Ljava/lang/Object;>(Lcom/google/common/graph/ValueGraph<TT;*>;Ljava/util/Comparator<-TT;>;)Ljava/util/List<TT;>;";

    public static final String GRAPH_CLASS_NAME = "com/google/common/graph/Graph";
    public static final String VALUEGRAPH_CLASS_NAME = "com/google/common/graph/ValueGraph";
    public static final String GRAPH_CLASS_DESC = "Lcom/google/common/graph/Graph;";
    public static final String VALUEGRAPH_CLASS_DESC = "Lcom/google/common/graph/ValueGraph;";

    public static final String SCCD_INIT_NAME = "<init>";
    public static final String SCCD_INIT_DESC = "(Lcom/google/common/graph/Graph;)V";
    public static final String SCCD_INIT_DESC_TRANSFORMED = "(Lcom/google/common/graph/ValueGraph;)V";
    public static final String SCCD_INIT_SIGN = "(Lcom/google/common/graph/Graph<TT;>;)V";
    public static final String SCCD_INIT_SIGN_TRANSFORMED = "(Lcom/google/common/graph/ValueGraph<TT;*>;)V";

    public static final String SCCD_GRAPH_FIELD_NAME = "graph";
    public static final String SCCD_GRAPH_FIELD_DESC = GRAPH_CLASS_DESC;
    public static final String SCCD_GRAPH_FIELD_DESC_TRANSFORMED = VALUEGRAPH_CLASS_DESC;
    public static final String SCCD_GRAPH_FIELD_SIGN = "Lcom/google/common/graph/Graph<TT;>;";
    public static final String SCCD_GRAPH_FIELD_SIGN_TRANSFORMED = "Lcom/google/common/graph/ValueGraph<TT;*>;";

    public static final String RCYM_INIT_NAME = "init";
    public static final String RCYM_INIT_DESC = "()V";

    public static byte[] transformTopoSort(String transformedName, byte[] bytes) {
        if (!TJHooks.IS_CRL) return bytes;
        ClassNode classNode = read(transformedName, bytes);

        if (classNode.methods != null) {
            for (MethodNode methodNode : classNode.methods) {
                if (methodNode.access == (ACC_PUBLIC | ACC_STATIC)) {
                    if (
                            methodNode.name.equals(TOPOSORT_METHOD_NAME) &&
                                    methodNode.desc.equals(TOPOSORT_METHOD_DESC) &&
                                    methodNode.signature.equals(TOPOSORT_METHOD_SIGN)
                    ) {
                        methodNode.desc = TOPOSORT_METHOD_DESC_TRANSFORMED;
                        methodNode.signature = TOPOSORT_METHOD_SIGN_TRANSFORMED;
                    }
                }
                InsnList instructions = methodNode.instructions;
                if (instructions != null) {
                    for (AbstractInsnNode insnNode : instructions.toArray()) {
                        if (insnNode instanceof MethodInsnNode methodInsnNode) {
                            if (methodInsnNode.getOpcode() == Opcodes.INVOKEINTERFACE) {
                                if (methodInsnNode.owner.equals(GRAPH_CLASS_NAME)) {
                                    methodInsnNode.owner = VALUEGRAPH_CLASS_NAME;
                                }
                            } else if (methodInsnNode.getOpcode() == Opcodes.INVOKESPECIAL) {
                                if (
                                        methodInsnNode.owner.equals(SCCD_CLASS_NAME) &&
                                                methodInsnNode.name.equals(SCCD_INIT_NAME) &&
                                                methodInsnNode.desc.equals(SCCD_INIT_DESC)
                                ) {
                                    methodInsnNode.desc = SCCD_INIT_DESC_TRANSFORMED;
                                }
                            }
                        }
                    }
                }
            }
        }

        return write(classNode);
    }

    public static byte[] transformSCCD(String transformedName, byte[] bytes) {
        if (!TJHooks.IS_CRL) return bytes;
        ClassNode classNode = read(transformedName, bytes);

        if (classNode.fields != null) {
            for (FieldNode fieldNode : classNode.fields) {
                if (fieldNode.access == (ACC_PRIVATE | ACC_FINAL)) {
                    if (
                            fieldNode.name.equals(SCCD_GRAPH_FIELD_NAME) &&
                                    fieldNode.desc.equals(SCCD_GRAPH_FIELD_DESC) &&
                                    fieldNode.signature.equals(SCCD_GRAPH_FIELD_SIGN)
                    ) {
                        fieldNode.desc = SCCD_GRAPH_FIELD_DESC_TRANSFORMED;
                        fieldNode.signature = SCCD_GRAPH_FIELD_SIGN_TRANSFORMED;
                    }
                }
            }
        }

        if (classNode.methods != null) {
            for (MethodNode methodNode : classNode.methods) {
                if (methodNode.access == ACC_PUBLIC) {
                    if (
                            methodNode.name.equals(SCCD_INIT_NAME) &&
                                    methodNode.desc.equals(SCCD_INIT_DESC) &&
                                    methodNode.signature.equals(SCCD_INIT_SIGN)
                    ) {
                        methodNode.desc = SCCD_INIT_DESC_TRANSFORMED;
                        methodNode.signature = SCCD_INIT_SIGN_TRANSFORMED;
                    }

                }
                InsnList instructions = methodNode.instructions;
                if (instructions != null) {
                    for (AbstractInsnNode insnNode : instructions.toArray()) {
                        if (insnNode instanceof FieldInsnNode fieldInsnNode) {
                            if (fieldInsnNode.getOpcode() == Opcodes.GETFIELD) {
                                if (
                                        fieldInsnNode.owner.equals(SCCD_CLASS_NAME) &&
                                                fieldInsnNode.name.equals(SCCD_GRAPH_FIELD_NAME) &&
                                                fieldInsnNode.desc.equals(SCCD_GRAPH_FIELD_DESC)
                                ) {
                                    fieldInsnNode.desc = SCCD_GRAPH_FIELD_DESC_TRANSFORMED;
                                }
                            }

                        } else if (insnNode instanceof MethodInsnNode methodInsnNode) {
                            if (methodInsnNode.getOpcode() == Opcodes.INVOKEINTERFACE) {
                                if (
                                        methodInsnNode.owner.equals(GRAPH_CLASS_NAME)
                                ) {
                                    methodInsnNode.owner = VALUEGRAPH_CLASS_NAME;
                                }
                            }
                        }
                    }
                }
            }
        }

        return write(classNode);
    }

    public static byte[] transformRecyclingManager(String transformedName, byte[] bytes) {
        if (!TJHooks.IS_CRL) return bytes;
        ClassNode classNode = read(transformedName, bytes);

        if (classNode.methods != null) {
            for (MethodNode methodNode : classNode.methods) {
                if (methodNode.access == (ACC_PUBLIC | ACC_STATIC)) {
                    if (
                            methodNode.name.equals(RCYM_INIT_NAME) &&
                                    methodNode.desc.equals(RCYM_INIT_DESC)
                    ) {
                        InsnList instructions = methodNode.instructions;
                        if (instructions != null) {
                            for (AbstractInsnNode insnNode : instructions.toArray()) {
                                if (insnNode instanceof MethodInsnNode methodInsnNode) {
                                    if (methodInsnNode.getOpcode() == Opcodes.INVOKESTATIC) {
                                        if (
                                                methodInsnNode.owner.equals(TOPOSORT_CLASS_NAME) &&
                                                        methodInsnNode.name.equals(TOPOSORT_METHOD_NAME) &&
                                                        methodInsnNode.desc.equals(TOPOSORT_METHOD_DESC)
                                        ) {
                                            methodInsnNode.desc = TOPOSORT_METHOD_DESC_TRANSFORMED;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        return write(classNode);
    }
}
