package tj.util;

import gregtech.api.GTValues;
import gregtech.api.unification.material.Material;

public class TJHooks {

    public static String getRLPrefix(Material material) {
        return material.getModid().equals(GTValues.MODID) ? "" : material.getModid() + ":";
    }
}
