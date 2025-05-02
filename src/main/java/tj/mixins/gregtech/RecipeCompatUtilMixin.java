package tj.mixins.gregtech;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import gregtech.api.GTValues;
import gregtech.api.items.metaitem.MetaItem;
import gregtech.api.unification.material.Material;
import gregtech.integration.RecipeCompatUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import tj.util.TJHooks;

@Mixin(value = RecipeCompatUtil.class, remap = false)
public class RecipeCompatUtilMixin {

    @ModifyReturnValue(method = "getMetaItemId", at = @At(value = "RETURN", ordinal = 0))
    private static String addNameSpace(String name, @Local MetaItem<?>.MetaValueItem metaValueItem) {
        String namespace = metaValueItem.getMetaItem().getRegistryName().getNamespace();
        if (namespace.equals(GTValues.MODID)) {
            return name;
        }
        return namespace + ":" + name;
    }

    @ModifyReturnValue(method = "getMetaItemId", at = {
            @At(value = "RETURN", ordinal = 2),
            @At(value = "RETURN", ordinal = 3),
            @At(value = "RETURN", ordinal = 4)
    })
    private static String addNameSpace(String name, @Share("material") LocalRef<Material> mat) {
        Material material = mat.get();
        return TJHooks.getRLPrefix(material) + material;
    }

    @Redirect(method = "getMetaItemId",
            at = @At(target = "Lgregtech/api/unification/material/Material;toCamelCaseString()Ljava/lang/String;",
                    value = "INVOKE"))
    private static String exposeLocals(Material material, @Share("material") LocalRef<Material> mat) {
        mat.set(material);
        return material.toCamelCaseString();
    }
}
