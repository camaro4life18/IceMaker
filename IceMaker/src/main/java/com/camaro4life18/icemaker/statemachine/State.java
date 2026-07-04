package com.camaro4life18.icemaker.statemachine;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.camaro4life18.icemaker.Utils;
import com.camaro4life18.icemaker.devices.GpioRelay;
import com.camaro4life18.icemaker.devices.GpioSwitch;
import com.camaro4life18.icemaker.devices.TempSensor;
import com.pi4j.Pi4J;
import com.pi4j.context.Context;
import com.pi4j.plugin.ffm.providers.gpio.FFMDigitalInputProviderImpl;
import com.pi4j.plugin.raspberrypi.platform.RaspberryPiPlatform;
import com.pi4j.plugin.raspberrypi.provider.gpio.digital.RpiDigitalOutputProviderImpl;

/**
 * Abstract base class for the Ice Maker State Machine.
 * <p>
 * This class serves as the context and base for the state pattern implementation.
 * It manages the shared hardware resources (GPIO pins, Relays, Sensors) via Pi4J
 * and holds static references to all concrete state instances.
 * </p>
 */
public abstract class State{
	private static Logger logger = LogManager.getLogger(State.class);
	// Shared hardware context for all states
	protected static Context pi4j = Pi4J.newContextBuilder()
			.add(RaspberryPiPlatform.newInstance())
			.add(RpiDigitalOutputProviderImpl.newInstance())
			.add(FFMDigitalInputProviderImpl.newInstance())
			.build();
	
	// State instances
	public static State initial;
	public static State harvest;
	public static State production;
	public static State binfull;
	public static State clean;
	public static State off;
	public static State fault;
	public static State current;
	
	// Hardware device definitions
	protected static GpioRelay gridCutter = new GpioRelay(pi4j, Utils.getGridCutterPin(), "GridCutter ");
	protected static GpioRelay waterPump = new GpioRelay(pi4j, Utils.getWaterPumpPin(), "WaterPump ");
	protected static GpioRelay fan = new GpioRelay(pi4j, Utils.getFanPin(), "Fan ");
	protected static GpioRelay hotGas = new GpioRelay(pi4j, Utils.getHotGasPin(), "HotGas Solenoid ");
	protected static GpioRelay drain = new GpioRelay(pi4j, Utils.getDrainPin(), "Drain Solenoid ");
	protected static GpioRelay water = new GpioRelay(pi4j, Utils.getWaterPin(), "Water Solenoid ");
	private static GpioRelay compressor = new GpioRelay(pi4j, Utils.getCompressorPin(), "Compressor ");
	
	protected static TempSensor binTemp = new TempSensor(Utils.getBinSensor(), "Bin");
	protected static TempSensor evapTemp = new TempSensor(Utils.getEvapSensor(), "Evap Tray");
	//protected TempSensor oatTemp = new TempSensor(Utils.getOatSensor(), "Evap Tray", w1Power);
	
	protected static GpioSwitch onSwitch = new GpioSwitch(pi4j, Utils.getOnSwitchPin(), "On Switch");
	protected static GpioSwitch cleanSwitch = new GpioSwitch(pi4j, Utils.getCleanSwitchPin(), "Clean Switch");

	private static long cutterStartTime = 0;
	private static Thread iceCutterThread = null;
	
	public State() {
		

	}
	
	public void run() throws InterruptedException {}
	
	protected void everythingOff() {
		gridCutter.off();
		waterPump.off();
		fan.off();
		hotGas.off();
		drain.off();
		water.off();
		compressor.off();
	}

	protected void enterFault(String reason) {
		logger.error("Entering safe fault state: " + reason);
		everythingOff();
		current = fault;
	}

	protected double readTempOrFault(TempSensor sensor, String sensorName) {
		double temperature = sensor.getTemp();
		if(Double.isNaN(temperature) || Double.isInfinite(temperature)) {
			enterFault(sensorName + " returned an invalid temperature reading");
			return Double.NaN;
		}
		return temperature;
	}
	
	protected void deviceStatus() {
		gridCutter.status();
		waterPump.status();
		fan.status();
		hotGas.status();
		drain.status();
		water.status();
		compressor.status();
		binTemp.getTemp();
		evapTemp.getTemp();
	}
	
	protected void cutIce(){
		Runnable runnable = new Runnable() {
			@Override
			public void run() {
				long iceCutTime = Utils.getIceCutTime();
				logger.info("Cutting Ice for " + iceCutTime);
				gridCutter.on();
				try {
					// Keep cutter on for the specified duration
					while(System.currentTimeMillis() - getCutterStartTime() <= (iceCutTime)) {
						try {
							Thread.sleep(100);
						} catch (InterruptedException e) {
							Thread.currentThread().interrupt();
							break;
						}
					}
				} finally {
					// Ensure cutter turns off even if interrupted
					gridCutter.off();
				}
				zeroCutterStartTime();
				iceCutterThread = null;
			}
		};
		
		// Start a new cutter thread if one isn't already running
		if(iceCutterThread == null || !iceCutterThread.isAlive()) {
			resetCutterStartTime();
			iceCutterThread = new Thread(runnable);
			logger.info("Starting Ice Cutter Thread");
			iceCutterThread.start();
		}else {
			// If already running, extend the timer
			resetCutterStartTime();			
		}
	}
	
	synchronized private static void resetCutterStartTime() {
		cutterStartTime = System.currentTimeMillis();
		logger.debug("Resetting cutter start time: " + getCutterStartTime());
	}
	
	synchronized private static void zeroCutterStartTime() {
		cutterStartTime = 0;
		logger.debug("Zeroing cutter start time: " + getCutterStartTime());
	}
	
	synchronized private static long getCutterStartTime() {
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
