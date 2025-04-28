package tj.mixins.gregtech;

import gregtech.api.worldgen.config.OreDepositDefinition;
import gregtech.integration.jei.basic.GTOreInfo;
import gregtech.integration.jei.utils.JEIResourceDepositCategoryUtils;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tj.util.DimDisplayRegistry;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Mixin(value = GTOreInfo.class, remap = false)
public abstract class GTOreInfoMixin {

    @Shadow
    private @Final OreDepositDefinition definition;

    @Shadow
    private @Final List<List<ItemStack>> groupedInputsAsItemStacks;

    @Inject(method = "<init>", at = @At("TAIL"))
    public void onConstruct(CallbackInfo ci) {
        int[] dims = JEIResourceDepositCategoryUtils.getAllRegisteredDimensions(definition.getDimensionFilter());

        Arrays.stream(dims)
                .mapToObj(DimDisplayRegistry.INSTANCE::get)
                .filter(stack -> !stack.isEmpty())
                .forEach(display -> groupedInputsAsItemStacks.add(Collections.singletonList(display)));
    }
}
