package org.infpancakefactory.ccb.core;

public class Instance {
    public final static Instance MOD_INSTANCE;

    static {
        MOD_INSTANCE = Instance.create(CommonEntrypoint.MOD_ID);
    }

    public static Instance create(String modId) {
        return new Instance(modId);
    }

    private Instance(String modId) {
        MOD_ID = modId;
    }

    public final String MOD_ID;
}
