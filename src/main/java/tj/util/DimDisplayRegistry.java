package tj.util;

import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.item.ItemStack;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Map;

@SuppressWarnings("unused")
@ParametersAreNonnullByDefault
public class DimDisplayRegistry {

    private static final Map<Integer, ItemStack> internal = new Int2ObjectOpenHashMap<>();
    private static ItemStack defaultStack = ItemStack.EMPTY;

    public static void set(int dimId, ItemStack stack) {
        internal.put(dimId, stack);
    }

    public static ItemStack get(int dimId) {
        return internal.getOrDefault(dimId, defaultStack);
    }

    public static void setDefault(ItemStack stack) {
        defaultStack = stack;
    }

    public static void clear() {
        internal.clear();
    }

    public static void set(Map<Integer, ItemStack> setter, boolean doClear) {
        if (doClear) internal.clear();
        internal.putAll(setter);
    }

    public static void set(Map<Integer, ItemStack> setter) {
        set(setter, true);
    }
}
