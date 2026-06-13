package org.infpancakefactory.ccb.core.entry.neoforge;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.fml.common.Mod;
import org.infpancakefactory.ccb.core.CommonEntrypoint;
import org.infpancakefactory.ccb.core.statics.config.ModCommonConfig;


@Mod(CommonEntrypoint.MOD_ID)
public class NeoForgeEntrypoint {
    public NeoForgeEntrypoint(ModContainer container) {
        CommonEntrypoint.init();
        if (FMLEnvironment.dist == Dist.CLIENT)
            container.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
        container.registerConfig(ModConfig.Type.COMMON, ModCommonConfig.spec, CommonEntrypoint.MOD_ID + "-common.toml");
    }
}