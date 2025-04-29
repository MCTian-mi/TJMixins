package tj.mixins.advancedrocketry;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import zmaster587.advancedRocketry.dimension.DimensionManager;
import zmaster587.libVulpes.network.BasePacket;

@Mixin(value = DimensionManager.class, remap = false)
public class DimensionManagerMixin {

    @WrapWithCondition(method = "registerDim",
            at = @At(target = "Lzmaster587/libVulpes/network/PacketHandler;sendToAll(Lzmaster587/libVulpes/network/BasePacket;)V",
                    value = "INVOKE"))
    public boolean shouldSendPacketToAll(BasePacket who_cares) {
        return Minecraft.getMinecraft().isIntegratedServerRunning();
    }
}
