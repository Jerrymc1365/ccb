package org.infpancakefactory.ccb.core.mixin;

import de.mrjulsen.crn.block.display.properties.PassengerInformationDetailedSettings;
import de.mrjulsen.crn.block.display.properties.PassengerInformationScrollingTextSettings;
import de.mrjulsen.crn.block.properties.EDisplayType;
import de.mrjulsen.crn.client.AdvancedDisplaysRegistry;
import de.mrjulsen.crn.client.ber.variants.BERPassengerInfoSimple;
import de.mrjulsen.crn.registry.ModDisplayTypes;
import net.minecraft.client.Minecraft;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Function;

@Pseudo
@Mixin(ModDisplayTypes.class)
public class MixinDisplayType {
    @Inject(method = {"<clinit>"}, at = @At("HEAD"))
    private static void clinit (CallbackInfo ci) {
        final AdvancedDisplaysRegistry.DisplayTypeResourceKey PASSENGER_INFORMATION_MAP = AdvancedDisplaysRegistry.register(
                EDisplayType.PASSENGER_INFORMATION, "detailed_map",
                PassengerInformationDetailedSettings::new, org.infpancakefactory.create.train.moderndisplay.displays.BERPassengerInformationMap::new, new AdvancedDisplaysRegistry.DisplayProperties(false, null));
        final AdvancedDisplaysRegistry.DisplayTypeResourceKey PASSENGER_INFORMATION_MAP_NEXT = AdvancedDisplaysRegistry.register(
                EDisplayType.PASSENGER_INFORMATION, "detailed_map_next",
                PassengerInformationDetailedSettings::new, org.infpancakefactory.create.train.moderndisplay.displays.BERPassengerInformationMapNext::new, new AdvancedDisplaysRegistry.DisplayProperties(false, null));

        final AdvancedDisplaysRegistry.DisplayTypeResourceKey PASSENGER_INFORMATION_MAP_RELOAD = AdvancedDisplaysRegistry.register(
                EDisplayType.PASSENGER_INFORMATION, "detailed_map_reload",
                PassengerInformationDetailedSettings::new, org.infpancakefactory.create.train.moderndisplay.displays.BERPassengerInformationMapReload::new, new AdvancedDisplaysRegistry.DisplayProperties(false, null));
    }
}

