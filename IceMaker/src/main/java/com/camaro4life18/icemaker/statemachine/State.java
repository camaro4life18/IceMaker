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
	
	protected GpioRelay gridCutter = new GpioRelay(pi4j, Utils.getGridCutterPin(), "GridCutter 1");
	protected GpioRelay waterPump = new GpioRelay(pi4j, Utils.getWaterPumpPin(), "WaterPump 2");
	protected GpioRelay fan = new GpioRelay(pi4j, Utils.getFanPin(), "Fan 3");
	protected GpioRelay hotGas = new GpioRelay(pi4j, Utils.getHotGasPin(), "HotGas Solenoid 4");
	protected GpioRelay drain = new GpioRelay(pi4j, Utils.getDrainPin(), "Drain Solenoid 5");
	protected GpioRelay water = new GpioRelay(pi4j, Utils.getWaterPin(), "Water Solenoid 6");
	private GpioRelay compressor = new GpioRelay(pi4j, Utils.getCompressorPin(), "Compressor 7");
	
	protected TempSensor binTemp = new TempSensor("28-00000094c09c", "Bin");
	protected TempSensor evapTemp = new TempSensor("28-0000008660fc", "Evap Tray");
	
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
				logger.info("Cutting Ice for " + (Utils.getIceMelt() / 1000 / 60) +" mins");
				gridCutter.on();
				while(System.currentTimeMillis() - getCutterStartTime() <= Utils.getIceMelt()) {
					//Keep Waiting
				}
				gridCutter.off();
				zeroCutterStartTime();
				iceCutterThread = null;
			}
		};
		
		if(iceCutterThread == null || !iceCutterThread.isAlive()) {
				
		
			iceCutterThread = new Thread(runnable);
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
			logger.info("Waiting 9 minutes for compressor safety");
			try {
				Thread.sleep(Utils.getCompressorDelay());
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
}
