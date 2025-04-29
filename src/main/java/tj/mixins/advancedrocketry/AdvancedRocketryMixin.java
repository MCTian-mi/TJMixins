package tj.mixins.advancedrocketry;

import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import zmaster587.advancedRocketry.AdvancedRocketry;
import zmaster587.advancedRocketry.api.ARConfiguration;
import zmaster587.advancedRocketry.dimension.DimensionManager;
import zmaster587.advancedRocketry.dimension.DimensionProperties;
import zmaster587.advancedRocketry.util.Asteroid;
import zmaster587.advancedRocketry.util.OreGenProperties;
import zmaster587.advancedRocketry.util.XMLAsteroidLoader;
import zmaster587.advancedRocketry.util.XMLOreLoader;
import zmaster587.libVulpes.util.HashedBlockPosition;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

@Mixin(value = AdvancedRocketry.class, remap = false)
public abstract class AdvancedRocketryMixin {

    @Shadow
    public static @Final Logger logger;

    @Shadow
    private boolean resetFromXml;

    @Inject(method = "serverStarting",
            at = @At(target = "Lzmaster587/advancedRocketry/tile/multiblock/machine/TileChemicalReactor;reloadRecipesSpecial()V",
                    value = "INVOKE_ASSIGN"),
            cancellable = true)
    public void skipDimensionCreation(FMLServerStartingEvent who_cares, CallbackInfo ci) {
        ci.cancel();
    }

    @SuppressWarnings({
            "CallToPrintStackTrace",
            "ResultOfMethodCallIgnored",
            "ExtractMethodRecommender",
            "ReassignedVariable",
            "StringConcatenationArgumentToLogCall"
    })
    @Inject(method = "preInit", at = @At("TAIL"))
    public void registerDimensions(FMLPreInitializationEvent event, CallbackInfo ci) {
        File file = new File("./config/advRocketry/asteroidConfig.xml");
        logger.info("Checking for asteroid config at " + file.getAbsolutePath());
        if (!file.exists()) {
            logger.info(file.getAbsolutePath() + " not found, generating");

            try {
                file.createNewFile();
                BufferedWriter stream = new BufferedWriter(new FileWriter(file));
                stream.write("<Asteroids>\n\t<asteroid name=\"Small Asteroid\" distance=\"10\" mass=\"200\" massVariability=\"0.5\" minLevel=\"0\" probability=\"20\" richness=\"0.3\" richnessVariability=\"0.5\">\n\t\t<ore itemStack=\"minecraft:iron_ore\" chance=\"15\" />\n\t\t<ore itemStack=\"minecraft:gold_ore\" chance=\"10\" />\n\t\t<ore itemStack=\"minecraft:redstone_ore\" chance=\"10\" />\n\t</asteroid>\n\t<asteroid name=\"Light Asteroid\" distance=\"60\" mass=\"200\" massVariability=\"0.5\" minLevel=\"0\" probability=\"15\" richness=\"0.2\" richnessVariability=\"0.5\">\n\t\t<ore itemStack=\"libvulpes:ore0;9\" chance=\"20\" />\n\t\t<ore itemStack=\"libvulpes:ore0;8\" chance=\"10\" />\n\t\t<ore itemStack=\"minecraft:quartz_block\" chance=\"5\" />\n\t</asteroid>\n\t<asteroid name=\"Iridium Enriched asteroid\" distance=\"100\" mass=\"75\" massVariability=\"0.5\" minLevel=\"0\" probability=\"2\" richness=\"0.2\" richnessVariability=\"0.3\">\n\t\t<ore itemStack=\"minecraft:iron_ore\" chance=\"25\" />\n\t\t<ore itemStack=\"libvulpes:ore0 10\" chance=\"5\" />\n\t</asteroid>\n\t<asteroid name=\"Strange Asteroid\" distance=\"120\" mass=\"50\" massVariability=\"0.5\" minLevel=\"0\" probability=\"1\" richness=\"0.2\" richnessVariability=\"0.5\">\n\t\t<ore itemStack=\"libvulpes:ore0;0\" chance=\"20\" />\n\t\t<ore itemStack=\"minecraft:emerald_ore\" chance=\"5\" />\n\t</asteroid>\n</Asteroids>");
                stream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        XMLAsteroidLoader load = new XMLAsteroidLoader();

        try {
            if (load.loadFile(file)) {
                for (Asteroid asteroid : load.loadPropertyFile()) {
                    ARConfiguration.getCurrentConfig().asteroidTypes.put(asteroid.ID, asteroid);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        file = new File("./config/advRocketry/oreConfig.xml");
        logger.info("Checking for ore config at " + file.getAbsolutePath());
        if (!file.exists()) {
            logger.info(file.getAbsolutePath() + " not found, generating");

            try {
                file.createNewFile();
                BufferedWriter stream = new BufferedWriter(new FileWriter(file));
                stream.write("<OreConfig>\n</OreConfig>");
                stream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            XMLOreLoader oreLoader = new XMLOreLoader();

            try {
                if (oreLoader.loadFile(file)) {
                    for (Map.Entry<HashedBlockPosition, OreGenProperties> entry : oreLoader.loadPropertyFile()) {
                        int pressure = entry.getKey().x;
                        int temp = entry.getKey().y;
                        if (pressure == -1) {
                            if (temp != -1) {
                                OreGenProperties.setOresForTemperature(DimensionProperties.Temps.values()[temp], entry.getValue());
                            }
                        } else if (temp == -1) {
                            OreGenProperties.setOresForPressure(DimensionProperties.AtmosphereTypes.values()[pressure], entry.getValue());
                        } else {
                            OreGenProperties.setOresForPressureAndTemp(DimensionProperties.AtmosphereTypes.values()[pressure], DimensionProperties.Temps.values()[temp], entry.getValue());
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        DimensionManager.getInstance().createAndLoadDimensions(this.resetFromXml);
    }
}
