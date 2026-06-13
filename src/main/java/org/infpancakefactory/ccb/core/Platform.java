package org.infpancakefactory.ccb.core;

import org.infpancakefactory.ccb.core.entry.neoforge.NeoForgePlatformImpl;

public interface Platform {
    Platform INSTANCE = new NeoForgePlatformImpl();

    boolean isModLoaded(String modid);
    String loader();

    boolean isClientSide();
}
