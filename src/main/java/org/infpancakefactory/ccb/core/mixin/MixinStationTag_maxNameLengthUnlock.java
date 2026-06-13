package org.infpancakefactory.ccb.core.mixin;

import de.mrjulsen.crn.data.StationTag;
import de.mrjulsen.mcdragonlib.client.gui.widgets.DLEditBox;
import net.minecraft.client.gui.components.EditBox;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;

@Pseudo
@Mixin({EditBox.class})
public class MixinStationTag_maxNameLengthUnlock {
    @ModifyVariable(method = "setMaxLength", at = @At(value = "HEAD"), argsOnly = true)
    public int a(int value) {
        if ((Object)this instanceof DLEditBox && value == StationTag.MAX_NAME_LENGTH) {
            return 1024;
        }
        return value;
    }
}
