package tj.mixins.gregtech;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import gregtech.api.recipes.RecipeMaps;
import gregtech.api.recipes.builders.AssemblerRecipeBuilder;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import tj.recycling.RecyclingManager;

@Mixin(value = RecipeMaps.class, remap = false)
public abstract class RecipeMapsMixin {

    @WrapOperation(method = "lambda$static$2",
            at = @At(target = "Lgregtech/api/recipes/builders/AssemblerRecipeBuilder;isWithRecycling()Z",
                    value = "INVOKE"))
    private static boolean replaceWithOurs(AssemblerRecipeBuilder builder, Operation<Boolean> who_cares) {
        if (builder.isWithRecycling()) {
            ItemStack output = builder.getOutputs().get(0);
            RecyclingManager.addRecycling(output, output.getCount(), builder.getInputs());
        }
        return false;
    }
}
