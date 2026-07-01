package com.camaro4life18.icemaker;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.LoggerConfig;

import com.camaro4life18.icemaker.statemachine.BinFull;
import com.camaro4life18.icemaker.statemachine.Clean;
import com.camaro4life18.icemaker.statemachine.Harvest;
import com.camaro4life18.icemaker.statemachine.Initial;
import com.camaro4life18.icemaker.statemachine.Off;
import com.camaro4life18.icemaker.statemachine.Production;
import com.camaro4life18.icemaker.statemachine.State;

public class IceMaker {
	public static Logger logger = LogManager.getLogger(IceMaker.class);
	
	public static void main(String args[]) {		
		logger.info("Starting IceMaker");
		Utils.initializeProperties();
		
		State.initial = new Initial();
		State.production = new Production();
		State.harvest = new Harvest();
		State.binfull = new BinFull();
		State.off = new Off();
		State.clean = new Clean();
		State.current = State.initial;
		
		LoggerContext context = (LoggerContext) LogManager.getContext(false);
		Configuration config = context.getConfiguration();
		LoggerConfig loggerConfig = config.getLoggerConfig(LogManager.ROOT_LOGGER_NAME);
		
		while(true) {
			try {
				loggerConfig.setLevel(Utils.getLogLevel());
				context.updateLoggers();
				State.current.run();
			} catch (InterruptedException e) {
				logger.error("Sleep error: " + e.getMessage());
			} catch (RuntimeException e) {
				logger.error("Fatal runtime error in controller loop", e);
				State.current = State.off;
				try {
					State.current.run();
				} catch (InterruptedException interruptedException) {
					logger.error("Interrupted while entering safe shutdown", interruptedException);
				} catch (RuntimeException shutdownException) {
					logger.error("Safe shutdown also failed", shutdownException);
				}
				break;
			}
		}
	}
}
