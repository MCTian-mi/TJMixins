package tj.asm;


import net.minecraft.launchwrapper.IClassTransformer;
import tj.asm.transformers.CrLCompatTransformer;

public class TJTransformer implements IClassTransformer {

    @Override
    public byte[] transform(String name, String transformedName, byte[] basicClass) {

        return switch (transformedName) {
            case "net.neoforged.fml.loading.toposort.StronglyConnectedComponentDetector" ->
                    CrLCompatTransformer.transformSCCD(transformedName, basicClass);
            case "net.neoforged.fml.loading.toposort.TopologicalSort" ->
                    CrLCompatTransformer.transformTopoSort(transformedName, basicClass);
            case "tj.recycling.RecyclingManager" ->
                    CrLCompatTransformer.transformRecyclingManager(transformedName, basicClass);

            default -> basicClass;
        };
    }
}
