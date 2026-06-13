package org.infpancakefactory.ccb.core.dyn.crn.apis.general.resource;

import net.minecraft.resources.ResourceLocation;

public class CCBAPIResourceLocation {
    public static ResourceLocation fromNamespaceAndPath(String s, String p) {
        //? if >1.21
        //return new ResourceLocation(s, p);
        //? if <1.21
        return ResourceLocation.fromNamespaceAndPath(s, p);
    }

    public static ResourceLocation parse(String p) {
        //? if >1.21
        //return new ResourceLocation(p);
        //? if <1.21
        return ResourceLocation.parse(p);
    }
}
