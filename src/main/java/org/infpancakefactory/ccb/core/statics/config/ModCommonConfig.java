package org.infpancakefactory.ccb.core.statics.config;

import net.neoforged.neoforge.common.ModConfigSpec;

public class ModCommonConfig {
    static ModConfigSpec.Builder builder = new ModConfigSpec.Builder();

    static ModConfigSpec.ConfigValue v1;

    public static ModConfigSpec spec = builder.build();
}
