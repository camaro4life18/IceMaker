package com.camaro4life18.icemaker;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.camaro4life18.icemaker.statemachine.BinFull;
import com.camaro4life18.icemaker.statemachine.Clean;
import com.camaro4life18.icemaker.statemachine.Harvest;
import com.camaro4life18.icemaker.statemachine.Initial;
import com.camaro4life18.icemaker.statemachine.Off;
import com.camaro4life18.icemaker.statemachine.Production;
import com.camaro4life18.icemaker.statemachine.State;
import com.pi4j.Pi4J;
import com.pi4j.context.Context;

public class IceMaker {
	public static Logger logger = LogManager.getLogger(IceMaker.class.getName());
	
	public static void main(String args[]) {		
		logger.info("Starting IceMaker");
		
		State.initial = new Initial();
		State.production = new Production();
		State.harvest = new Harvest();
		State.binfull = new BinFull();
		State.off = new Off();
		State.clean = new Clean();
		State.current = State.initial;
		
		while(true) {
			try {
				State.current.enter();
				State.current.update();
			} catch (InterruptedException e) {
				logger.error("Sleep error: " + e.getMessage());
			}
		}
	}
}
