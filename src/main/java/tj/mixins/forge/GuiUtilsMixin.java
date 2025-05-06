package tj.mixins.forge;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.client.config.GuiUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tj.util.TJHooks;

import java.util.List;

@Mixin(value = GuiUtils.class, remap = false)
public class GuiUtilsMixin {

    @Inject(method = "drawHoveringText(Lnet/minecraft/item/ItemStack;Ljava/util/List;IIIIILnet/minecraft/client/gui/FontRenderer;)V", at = @At("HEAD"), cancellable = true)
    private static void addRichTextEvent(ItemStack stack, List<String> textLines, int mouseX, int mouseY, int screenWidth, int screenHeight, int maxTextWidth, FontRenderer font, CallbackInfo ci) {
        if (!textLines.isEmpty() && TJHooks.postRichTooltipEvent(stack, textLines, -mouseX, mouseY, screenWidth, screenHeight)) {
            ci.cancel();
        }
    }
}
