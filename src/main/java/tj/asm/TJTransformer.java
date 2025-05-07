package tj.asm;


import net.minecraft.launchwrapper.IClassTransformer;
import tj.asm.transformers.GuavaGraphTransformer;

public class TJTransformer implements IClassTransformer {

    @Override
    public byte[] transform(String name, String transformedName, byte[] basicClass) {

        return switch (transformedName) {
            case "net.neoforged.fml.loading.toposort.StronglyConnectedComponentDetector" ->
                    GuavaGraphTransformer.transformSCCD(transformedName, basicClass);
            case "net.neoforged.fml.loading.toposort.TopologicalSort" ->
                    GuavaGraphTransformer.transformTopoSort(transformedName, basicClass);
            case "tj.recycling.RecyclingManager" ->
                    GuavaGraphTransformer.transformRecyclingManager(transformedName, basicClass);

            default -> basicClass;
        };
    }
}
