package dev.zeroinputlag;

import net.fabricmc.api.ClientModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** ZeroInputLag: client-only input latency reduction. No config, no gameplay assists. */
public final class ZeroInputLagClient implements ClientModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("ZeroInputLag");

    @Override
    public void onInitializeClient() {
        LOGGER.info("ZeroInputLag loaded: late input polling + raw unsmoothed mouse.");
    }
}
