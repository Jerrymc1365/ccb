
package org.infpancakefactory.ccb.core.entry.neoforge;

import net.neoforged.fml.loading.FMLEnvironment;
import org.infpancakefactory.ccb.core.Platform;
import net.neoforged.fml.ModList;

public class NeoForgePlatformImpl implements Platform {
    @Override
    public boolean isModLoaded(String modid) {
        return ModList.get().isLoaded(modid);
    }

    @Override
    public String loader() {
        return "neoforge";
    }

    @Override
    public boolean isClientSide() {
        return FMLEnvironment.dist.isClient();
    }
}