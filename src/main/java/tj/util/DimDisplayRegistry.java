package tj.util;

import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.item.ItemStack;

import java.util.Map;

public enum DimDisplayRegistry {
    INSTANCE;

    private final Map<Integer, ItemStack> internal = new Int2ObjectOpenHashMap<>();
    private ItemStack defaultStack = ItemStack.EMPTY;

    public void set(int dimId, ItemStack stack) {
        internal.put(dimId, stack);
    }

    public ItemStack get(int dimId) {
        return internal.getOrDefault(dimId, defaultStack);
    }

    public void setDefault(ItemStack stack) {
        this.defaultStack = stack;
    }

    public void clear() {
        this.internal.clear();
    }
}
