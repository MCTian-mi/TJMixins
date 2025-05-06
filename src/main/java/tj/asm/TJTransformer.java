package tj.asm;


import net.minecraft.launchwrapper.IClassTransformer;

public class TJTransformer implements IClassTransformer {

    @Override
    public byte[] transform(String name, String transformedName, byte[] basicClass) {

        switch (transformedName) {
            // TODO
        }

        return basicClass;
    }
}
