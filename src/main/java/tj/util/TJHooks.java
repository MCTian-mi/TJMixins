package tj.util;

import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.screen.RichTooltip;
import com.cleanroommc.modularui.screen.viewport.GuiContext;
import com.cleanroommc.modularui.widget.sizer.Area;
import gregtech.api.GTValues;
import gregtech.api.unification.material.Material;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import tj.tooltip.RichTooltipEvent;

import java.util.List;

public class TJHooks {

    public static String getRLPrefix(Material material) {
        return material.getModid().equals(GTValues.MODID) ? "" : material.getModid() + ":";
    }

    public static boolean postRichTooltipEvent(ItemStack stack, List<String> textLines,
                                               int x, int y, int w, int h) {
        RichTooltip richTooltip = new RichTooltip(() -> new Area(x, y, w, h));
        textLines.forEach(line -> richTooltip.addLine(IKey.str(line)));
        RichTooltipEvent event = new RichTooltipEvent(stack, richTooltip);
        MinecraftForge.EVENT_BUS.post(event);
        boolean replace = event.shouldReplace();
        if (replace) {
            richTooltip.draw(GuiContext.getDefault(), stack);
        }
        return replace;
    }
}
