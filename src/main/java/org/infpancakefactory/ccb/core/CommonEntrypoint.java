package org.infpancakefactory.ccb.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CommonEntrypoint {


    public static final String MOD_ID = "ccb";
    public static final Logger LOG = LoggerFactory.getLogger(MOD_ID);

    public static void init() {
        LOG.info("Initializing {} on {} Platform", MOD_ID, Platform.INSTANCE.loader());
    }

}
