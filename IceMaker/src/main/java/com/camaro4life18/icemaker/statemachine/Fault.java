package com.camaro4life18.icemaker.statemachine;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Represents a latched fault hold state.
 * <p>
 * When entered due to invalid sensor data or other safety faults,
 * all outputs stay off and the controller will not auto-drain cycle.
 * Recovery requires an explicit operator reset by toggling the ON switch off.
 * </p>
 */
public class Fault extends State {
    private static final Logger logger = LogManager.getLogger(Fault.class);

    @Override
    public void run() throws InterruptedException {
        logger.error("Running Fault State - all outputs disabled until operator reset");
        everythingOff();

        while (true) {
            if (!onSwitch.isOn()) {
                logger.info("Fault reset detected (ON switch is off). Returning to Off state.");
                current = off;
                return;
            }

            Thread.sleep(1000);
        }
    }
}
