package tj.mixins.gregtech;

import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import gregtech.common.creativetab.GTCreativeTabs;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(value = GTCreativeTabs.class, remap = false)
public class GTCreativeTabsMixin {

    /// Super brute-force yea
    @Expression("true")
    @ModifyExpressionValue(method = "<clinit>", at = @At("MIXINEXTRAS:EXPRESSION"))
    private static boolean wftWhoUsesSearchBarsWhenYouHaveJEI(boolean who_care) {
        return false;
    }
}
