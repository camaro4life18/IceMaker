package com.allen.icemaker;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.allen.icemaker.statemachine.BinFull;
import com.allen.icemaker.statemachine.Clean;
import com.allen.icemaker.statemachine.Harvest;
import com.allen.icemaker.statemachine.Initial;
import com.allen.icemaker.statemachine.Off;
import com.allen.icemaker.statemachine.Production;
import com.allen.icemaker.statemachine.State;

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
