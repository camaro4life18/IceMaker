package com.camaro4life18.icemaker.statemachine;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.camaro4life18.icemaker.Utils;
import com.camaro4life18.icemaker.devices.GpioRelay;
import com.camaro4life18.icemaker.devices.GpioSwitch;
import com.camaro4life18.icemaker.devices.TempSensor;
import com.pi4j.Pi4J;
import com.pi4j.context.Context;

public abstract class State{
	private static Logger logger = LogManager.getLogger(State.class);
	protected Context pi4j = Pi4J.newAutoContext();
	
	public static State initial;
	public static State harvest;
	public static State production;
	public static State binfull;
	public static State clean;
	public static State off;
	public static State current;
	
	protected GpioRelay gridCutter = new GpioRelay(pi4j, Utils.getGridCutterPin(), "GridCutter ");
	protected GpioRelay waterPump = new GpioRelay(pi4j, Utils.getWaterPumpPin(), "WaterPump ");
	protected GpioRelay fan = new GpioRelay(pi4j, Utils.getFanPin(), "Fan ");
	protected GpioRelay hotGas = new GpioRelay(pi4j, Utils.getHotGasPin(), "HotGas Solenoid ");
	protected GpioRelay drain = new GpioRelay(pi4j, Utils.getDrainPin(), "Drain Solenoid ");
	protected GpioRelay water = new GpioRelay(pi4j, Utils.getWaterPin(), "Water Solenoid ");
	private GpioRelay compressor = new GpioRelay(pi4j, Utils.getCompressorPin(), "Compressor ");
	
	protected TempSensor binTemp = new TempSensor(Utils.getBinSensor(), "Bin");
	protected TempSensor evapTemp = new TempSensor(Utils.getEvapSensor(), "Evap Tray");
	//protected TempSensor oatTemp = new TempSensor(Utils.getOatSensor(), "Evap Tray");
	
	protected GpioSwitch onSwitch = new GpioSwitch(pi4j, Utils.getOnSwitchPin(), "On Switch");
	protected GpioSwitch cleanSwitch = new GpioSwitch(pi4j, Utils.getCleanSwitchPin(), "Clean Switch");

	private long cutterStartTime = 0;
	private Thread iceCutterThread = null;
	
	public State() {
		

	}
	
	public void run() throws InterruptedException {}
	
	protected void everythingOff() {
		this.gridCutter.off();
		this.waterPump.off();
		this.fan.off();
		this.hotGas.off();
		this.drain.off();
		this.water.off();
		this.compressor.off();
	}
	
	protected void deviceStatus() {
		this.gridCutter.status();
		this.waterPump.status();
		this.fan.status();
		this.hotGas.status();
		this.drain.status();
		this.water.status();
		this.compressor.status();
		this.binTemp.getTemp();
		this.evapTemp.getTemp();
	}
	
	protected void cutIce(){
		Runnable runnable = new Runnable() {
			@Override
			public void run() {
				long iceCutTime = Utils.getIceCutTime();
				logger.info("Cutting Ice for " + iceCutTime);
				gridCutter.on();
				while(System.currentTimeMillis() - getCutterStartTime() <= (iceCutTime)) {
					//wait
				}
				gridCutter.off();
				zeroCutterStartTime();
				iceCutterThread = null;
			}
		};
		
		if(iceCutterThread == null || !iceCutterThread.isAlive()) {
			resetCutterStartTime();
			iceCutterThread = new Thread(runnable);
			logger.info("Starting Ice Cutter Thread");
			iceCutterThread.start();
		}else {
			resetCutterStartTime();			
		}
	}
	
	synchronized private void resetCutterStartTime() {
		cutterStartTime = System.currentTimeMillis();
		logger.debug("Resetting cutter start time: " + getCutterStartTime());
	}
	
	synchronized private void zeroCutterStartTime() {
		cutterStartTime = 0;
		logger.debug("Zeroing cutter start time: " + getCutterStartTime());
	}
	
	synchronized private long getCutterStartTime() {
		return cutterStartTime;
	}
	
	protected void compressorOn() {
		if(Utils.getCompressorState()) {
			long delay = Utils.getCompressorDelay();
			logger.info("Waiting " + delay + " minutes for compressor safety");
			try {
				Thread.sleep(delay * 60 * 1000);
			} catch (InterruptedException e) {
				logger.error(e.getMessage());
			}
		}
		Utils.compressorOn();
		compressor.on();
	}
	
	protected void compressorOff() {
		compressor.off();
		Utils.compressorOff();
	}
	
	protected double getHarvestTemp() {
		double harvestTemp = Utils.getHarvestTemp();
		//double oat = oatTemp.getTemp();
		//double oatAdjust = oat / Utils.getDefaultOatTemp();
		//logger.debug("OAT is " + oat);
		//logger.debug("OAT Adjust is " + oatAdjust);
		//harvestTemp = harvestTemp * oatAdjust;
		logger.debug("Harvest temp is " + harvestTemp);
		
		return harvestTemp;
	}
}
