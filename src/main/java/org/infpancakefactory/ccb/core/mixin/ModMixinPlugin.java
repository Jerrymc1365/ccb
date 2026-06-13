package org.infpancakefactory.ccb.core.mixin;

import me.fallenbreath.conditionalmixin.api.checker.RestrictionChecker;
import me.fallenbreath.conditionalmixin.api.checker.RestrictionCheckers;
import org.infpancakefactory.ccb.core.CommonEntrypoint;

import java.util.List;
import java.util.Set;

public class ModMixinPlugin extends me.fallenbreath.conditionalmixin.api.mixin.RestrictiveMixinConfigPlugin {
    private final RestrictionChecker restrictionChecker = RestrictionCheckers.memorized();

    @Override
    protected void onRestrictionCheckFailed(String mixinClassName, String reason) {
        CommonEntrypoint.LOG.debug("[CCB Tweaks] Disabled mixin {} due to {}", mixinClassName, reason);
    }

    @Override
    public String getRefMapperConfig() {
        return null;
    }

    @Override
    public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {
    }

    @Override
    public List<String> getMixins() {
        return null;
    }
}
