package org.infpancakefactory.ccb.core.dyn.crn.apis;

import de.mrjulsen.crn.CreateRailwaysNavigator;
import net.minecraft.resources.ResourceLocation;
import org.infpancakefactory.create.train.moderndisplay.MainEntrypoint;

public class Resources {
    public static ResourceLocation fromNamespaceAndPath(String id, String path) {
        //? if > 1.21 {
        return ResourceLocation.fromNamespaceAndPath(id, path);
        //?} elif < 1.21 {
        /*return new ResourceLocation(id, path);
        *///?}
    }
    public final static ResourceLocation COLOR_GRN = Resources.fromNamespaceAndPath(MainEntrypoint.MOD_ID, "textures/gui/grnw.png");
    public final static ResourceLocation COLOR_RED = Resources.fromNamespaceAndPath(MainEntrypoint.MOD_ID, "textures/gui/redw.png");
    public final static ResourceLocation COLOR_ORG = Resources.fromNamespaceAndPath(MainEntrypoint.MOD_ID, "textures/gui/orgw.png");

    public final static ResourceLocation COLOR_GRY = Resources.fromNamespaceAndPath(MainEntrypoint.MOD_ID, "textures/gui/gryw.png");
    public final static ResourceLocation LINE = Resources.fromNamespaceAndPath(MainEntrypoint.MOD_ID, "textures/gui/line256.png");
    public final static ResourceLocation LINEU = Resources.fromNamespaceAndPath(MainEntrypoint.MOD_ID, "textures/gui/lineu256.png");
    public final static ResourceLocation INL0 = Resources.fromNamespaceAndPath(MainEntrypoint.MOD_ID, "textures/gui/inl0.png");
    public final static ResourceLocation INL1 = Resources.fromNamespaceAndPath(MainEntrypoint.MOD_ID, "textures/gui/inl1.png");
    public final static ResourceLocation INL2 = Resources.fromNamespaceAndPath(MainEntrypoint.MOD_ID, "textures/gui/inl2.png");
    public static final ResourceLocation INL3 = Resources.fromNamespaceAndPath(MainEntrypoint.MOD_ID, "textures/gui/inl3.png");
    public static final ResourceLocation INL4 = Resources.fromNamespaceAndPath(MainEntrypoint.MOD_ID, "textures/gui/inl4.png");

    public static final ResourceLocation INLGRN = Resources.fromNamespaceAndPath(MainEntrypoint.MOD_ID, "textures/gui/inlgrn.png");
    //public static final ResourceLocation ECRN = Resources.fromNamespaceAndPath(MainEntrypoint.MOD_ID, "textures/gui/ecrn.png");

    private static final String keyNextStop = "gui.createrailwaysnavigator.route_overview.next_stop";
    private static final String keyArStation = "gui.createrailwaysnavigator.route_overview.ar_stop";
    private static final String noData = "gui.createrailwaysnavigator.route_overview.no_data";

    private static final String KeyShowThisStation = "map.je.mods.ecrn.show_this_station";
    private static final String ZWF1 = "gui.createrailwaysnavigator.route_overview.no_data";
    private static final String ZWF2 = "gui.createrailwaysnavigator.route_overview.no_data";

}
