package tj.tooltip;

import com.cleanroommc.modularui.screen.RichTooltip;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.eventhandler.Event;

public class RichTooltipEvent extends Event {

    public ItemStack stack;
    public RichTooltip tooltip;
    public boolean replace;

    public RichTooltipEvent(ItemStack stack, RichTooltip tooltip) {
        this.tooltip = tooltip;
        this.stack = stack;
    }

    public void setReplace(boolean replace) {
        this.replace = replace;
    }

    public boolean shouldReplace() {
        return replace;
    }
}
