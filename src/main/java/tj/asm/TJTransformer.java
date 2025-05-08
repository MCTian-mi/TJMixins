package tj.asm;


import net.minecraft.launchwrapper.IClassTransformer;
import tj.asm.transformers.CrLCompatTransformer;

@SuppressWarnings("unused")
public class TJTransformer implements IClassTransformer {

    @Override
    public byte[] transform(String name, String transformedName, byte[] basicClass) {

        String internalName = transformedName.replace('.', '/');

        return switch (internalName) {
            case CrLCompatTransformer.SCCD_CLASS_NAME ->
                    CrLCompatTransformer.transformSCCD(transformedName, basicClass);
            case CrLCompatTransformer.TOPOSORT_CLASS_NAME ->
                    CrLCompatTransformer.transformTopoSort(transformedName, basicClass);
            case CrLCompatTransformer.RCYM_CLASS_NAME ->
                    CrLCompatTransformer.transformRecyclingManager(transformedName, basicClass);

            default -> basicClass;
        };
    }
}
