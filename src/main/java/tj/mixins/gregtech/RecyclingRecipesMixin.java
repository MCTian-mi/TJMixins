package tj.mixins.gregtech;

import gregtech.loaders.recipe.RecyclingRecipes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tj.recycling.RecyclingManager;

@Mixin(value = RecyclingRecipes.class, remap = false)
public abstract class RecyclingRecipesMixin {

    @Inject(method = "init", at = @At("HEAD"))
    private static void initUnificationInfo(CallbackInfo ci) {
        RecyclingManager.init();
    }
}
