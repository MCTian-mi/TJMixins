package tj.mixins.gregtech;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalIntRef;
import gregtech.integration.jei.basic.BasicRecipeCategory;
import gregtech.integration.jei.basic.GTOreCategory;
import gregtech.integration.jei.basic.GTOreInfo;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import org.jetbrains.annotations.NotNull;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = GTOreCategory.class, remap = false)
public abstract class GTOreCategoryMixin extends BasicRecipeCategory<GTOreInfo, GTOreInfo> {

    @Unique
    private static final int NUM_OF_DIM_DISPLAY = 7;

    @Shadow
    private static @Final int NUM_OF_SLOTS;

    @Shadow
    private static @Final int SLOT_WIDTH;

    @Shadow
    private static @Final int SLOT_HEIGHT;

    /// Are you happy how javac?
    GTOreCategoryMixin() {
        super(null, null, null, null);
    }

    @ModifyConstant(method = "<init>", constant = @Constant(intValue = 166, ordinal = 0))
    private static int reducePanelHeight(int who_cares) {
        return 108;
    }

    @ModifyConstant(method = "setRecipe(Lmezz/jei/api/gui/IRecipeLayout;Lgregtech/integration/jei/basic/GTOreInfo;Lmezz/jei/api/ingredients/IIngredients;)V",
            constant = @Constant(intValue = 22, ordinal = 1))
    private int relocateSurfaceIdentifierX(int who_cares) {
        return 22 + SLOT_WIDTH;
    }

    @ModifyConstant(method = "setRecipe(Lmezz/jei/api/gui/IRecipeLayout;Lgregtech/integration/jei/basic/GTOreInfo;Lmezz/jei/api/ingredients/IIngredients;)V",
            constant = @Constant(intValue = 73, ordinal = 0))
    private int relocateSurfaceIdentifierY(int who_cares, @Local(ordinal = 0) int baseYPose) {
        return baseYPose;
    }

    @ModifyConstant(method = "drawExtras",
            constant = @Constant(intValue = 22, ordinal = 1))
    private int relocateSurfaceIdentifierSlotX(int who_cares) {
        return 22 + SLOT_WIDTH;
    }

    @ModifyConstant(method = "drawExtras",
            constant = @Constant(intValue = 73, ordinal = 0))
    private int relocateSurfaceIdentifierSlotY(int who_cares, @Local(ordinal = 1) int baseYPose) {
        return baseYPose;
    }

    @Inject(method = "drawExtras",
            at = @At(target = "Lgregtech/api/util/GTStringUtils;drawCenteredStringWithCutoff(Ljava/lang/String;Lnet/minecraft/client/gui/FontRenderer;I)V",
                    value = "INVOKE"))
    private void reCalculateBaseXYPos(Minecraft who_cares, CallbackInfo ci,
                                      @Local(ordinal = 0) LocalIntRef baseXPos,
                                      @Local(ordinal = 1) LocalIntRef baseYPos) {
        baseXPos.set(22);
        baseYPos.set(baseYPos.get() + FONT_HEIGHT / 2);
    }

    @ModifyConstant(method = "drawExtras",
            constant = @Constant(intValue = 90, ordinal = 0))
    private int skipDumbCheck(int who_cares) {
        return 0;
    }

    @Redirect(method = "drawExtras",
            at = @At(target = "Lgregtech/integration/jei/basic/GTOreCategory;FONT_HEIGHT:I",
                    value = "FIELD",
                    opcode = Opcodes.GETFIELD))
    private int betterLineSpacing(GTOreCategory why_is_this_a_protected_field_you_deserve_it) {
        /// 1.5 * FONT_HEIGHT
        return FONT_HEIGHT + FONT_HEIGHT / 2;
    }

    @WrapWithCondition(method = "drawExtras",
            at = @At(target = "Lgregtech/integration/jei/utils/JEIResourceDepositCategoryUtils;drawMultiLineCommaSeparatedDimensionList(Lit/unimi/dsi/fastutil/ints/Int2ObjectMap;[ILnet/minecraft/client/gui/FontRenderer;III)V",
                    value = "INVOKE"))
    private boolean skipDimensionString(Int2ObjectMap<String> who, int[] cares, FontRenderer even, int the, int slightest, int bit) {
        return false;
    }

    @WrapWithCondition(method = "drawExtras",
            at = @At(target = "Lnet/minecraft/client/gui/FontRenderer;drawSplitString(Ljava/lang/String;IIII)V",
                    value = "INVOKE"))
    private boolean skipSurfaceIdentifierString(FontRenderer i, String dont, int care, int about, int such, int shit) {
        return false;
    }

    @Inject(method = "setRecipe(Lmezz/jei/api/gui/IRecipeLayout;Lgregtech/integration/jei/basic/GTOreInfo;Lmezz/jei/api/ingredients/IIngredients;)V",
            at = @At(target = "Lmezz/jei/api/gui/IGuiItemStackGroup;addTooltipCallback(Lmezz/jei/api/gui/ITooltipCallback;)V", value = "INVOKE"))
    public void initializeDimDisplayItems(IRecipeLayout who_cares, GTOreInfo recipeWrapper, @NotNull IIngredients ingredients,
                                          CallbackInfo ci, @Local IGuiItemStackGroup itemStackGroup) {
        int initialized = 2 + recipeWrapper.getOutputCount();
        int size = ingredients.getInputs(VanillaTypes.ITEM).size() - 2;
        int height = 19 + (((initialized - 2) / NUM_OF_SLOTS) + 1) * SLOT_HEIGHT + 4 * FONT_HEIGHT + FONT_HEIGHT / 2;
        for (int j = 0; j < size; j++)
            itemStackGroup.init(j + initialized, true,
                    22 + (j % NUM_OF_DIM_DISPLAY) * SLOT_WIDTH,
                    height + (j / NUM_OF_DIM_DISPLAY) * SLOT_HEIGHT);
    }
}
