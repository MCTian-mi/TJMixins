package tj.mixins.gregtech;

import com.llamalad7.mixinextras.sugar.Local;
import gregtech.integration.jei.basic.GTOreCategory;
import gregtech.integration.jei.basic.GTOreInfo;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = GTOreCategory.class, remap = false)
public abstract class GTOreCategoryMixin {

    @Unique
    private static final int NUM_OF_DIM_DISPLAY = 7;

    @Shadow
    private static @Final int SLOT_WIDTH;

    @Shadow
    private static @Final int SLOT_HEIGHT;


    @Inject(method = "setRecipe(Lmezz/jei/api/gui/IRecipeLayout;Lgregtech/integration/jei/basic/GTOreInfo;Lmezz/jei/api/ingredients/IIngredients;)V",
            at = @At(target = "Lmezz/jei/api/gui/IGuiItemStackGroup;addTooltipCallback(Lmezz/jei/api/gui/ITooltipCallback;)V",
                    value = "INVOKE"))
    public void initializeDimDisplayItems(IRecipeLayout recipeLayout, GTOreInfo recipeWrapper, @NotNull IIngredients ingredients,
                                          CallbackInfo ci, @Local IGuiItemStackGroup itemStackGroup) {
        int initialized = 2 + recipeWrapper.getOutputCount();
        int size = ingredients.getInputs(VanillaTypes.ITEM).size() - 2;
        for (int j = 0; j < size; j++)
            itemStackGroup.init(j + initialized, true,
                    22 + (j % NUM_OF_DIM_DISPLAY) * SLOT_WIDTH,
                    112 + (j / NUM_OF_DIM_DISPLAY) * SLOT_HEIGHT);
    }
}
