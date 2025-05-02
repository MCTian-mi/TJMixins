package tj.mixins.gregtech;

import gregtech.api.unification.material.Material;
import gregtech.integration.groovy.GroovyHandCommand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import tj.util.TJHooks;

@Mixin(value = GroovyHandCommand.class, remap = false)
public class GroovyHandCommandMixin {

    @ModifyArg(method = "onHandCommand",
            at = @At(target = "Ljava/lang/StringBuilder;append(Ljava/lang/Object;)Ljava/lang/StringBuilder;",
                    value = "INVOKE",
                    ordinal = 0))
    private static Object toNameSpace(Object obj) {
        if (obj instanceof Material material) {
            return TJHooks.getRLPrefix(material) + material;
        } else throw new AssertionError("Unexpected type: " + obj.getClass());
    }
}
