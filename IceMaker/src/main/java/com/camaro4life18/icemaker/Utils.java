package com.camaro4life18.icemaker;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Utils {
	private static Logger logger = LogManager.getLogger(Utils.class);
	private static Properties properties = null;
	private static String propertiesFile = "config.properties";
	
	public static void initializeProperties() {
		try {
			if(properties == null) {
				properties = new Properties();				
			}else {
				properties.clear();
			}
			InputStream input = new FileInputStream(propertiesFile);
			properties.load(input);
		} catch (IOException e) {
			logger.error(e.getMessage());
		}
	}

	public static double getStartIce() {
		if(properties == null) {
			Utils.initializeProperties();
		}
		return Double.parseDouble(properties.getProperty("startIce"));
	}

	public static double getStopIce() {
		if(properties == null) {
			Utils.initializeProperties();
		}
		return Double.parseDouble(properties.getProperty("stopIce"));
	}

	public static long getDrainTime() {
		if(properties == null) {
			Utils.initializeProperties();
		}
		return Long.parseLong(properties.getProperty("drainTime")) * 60 * 1000;
	}

	public static long getWaterFillTime() {
		if(properties == null) {
			Utils.initializeProperties();
		}
		return Long.parseLong(properties.getProperty("waterFillTime")) * 60 * 1000;
	}

	public static double getProductionTemp() {
		if(properties == null) {
			Utils.initializeProperties();
		}
		return Double.parseDouble(properties.getProperty("production"));
	}

	public static long getCompressorDelay() {
		if(properties == null) {
			Utils.initializeProperties();
		}
		return Long.parseLong(properties.getProperty("compressorDelay"));
	}

	public static double getHarvestTemp() {
		if(properties == null) {
			Utils.initializeProperties();
		}
		return Double.parseDouble(properties.getProperty("harvest"));
	}

	public static long getIceCutTime() {
		if(properties == null) {
			Utils.initializeProperties();
		}
		return Long.parseLong(properties.getProperty("iceCutTime")) * 60 * 1000;
	}

	public static String getGridCutterPin() {
		if(properties == null) {
			Utils.initializeProperties();
		}
		return properties.getProperty("gridCutterPin");
	}

	public static String getWaterPumpPin() {
		if(properties == null) {
			Utils.initializeProperties();
		}
		return properties.getProperty("waterPumpPin");
	}

	public static String getHotGasPin() {
		if(properties == null) {
			Utils.initializeProperties();
		}
		return properties.getProperty("hotGasPin");
	}

	public static String getDrainPin() {
		if(properties == null) {
			Utils.initializeProperties();
		}
		return properties.getProperty("drainPin");
	}

	public static String getFanPin() {
		if(properties == null) {
			Utils.initializeProperties();
		}
		return properties.getProperty("fanPin");
	}

	public static String getWaterPin() {
		if(properties == null) {
			Utils.initializeProperties();
		}
		return properties.getProperty("waterPin");
	}

	public static String getCompressorPin() {
		if(properties == null) {
			Utils.initializeProperties();
		}
		return properties.getProperty("compressorPin");
	}

	public static String getOnSwitchPin() {
		if(properties == null) {
			Utils.initializeProperties();
		}
		return properties.getProperty("onPin");
	}

	public static String getCleanSwitchPin() {
		if(properties == null) {
			Utils.initializeProperties();
		}
		return properties.getProperty("cleanPin");
	}
	
	public static void compressorOn() {
		logger.debug("Setting Compressor On State");
		properties.setProperty("compressorState", "1");		
		storeProperties();
	}
	
	public static void compressorOff() {
		logger.debug("Setting Compressor Off State");
		properties.setProperty("compressorState", "0");
		storeProperties();
	}
	
	public static boolean getCompressorState() {
		if(properties.getProperty("compressorState").equals("1")) {
			logger.debug("Compressor State = " + properties.getProperty("compressorState"));
			return true;
		}
		return false;
	}

	public static Level getLogLevel() {
		if(properties == null) {
			Utils.initializeProperties();
		}		
		return Level.toLevel(properties.getProperty("logLevel"));
	}
	
	private static void storeProperties() {
		try {
			OutputStream os = new FileOutputStream(propertiesFile);
			properties.store(os, "");
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}

	public static String getBinSensor() {
		if(properties == null) {
			Utils.initializeProperties();
		}
		return properties.getProperty("binSensor");
	}

	public static String getEvapSensor() {
		if(properties == null) {
			Utils.initializeProperties();
		}
		return properties.getProperty("evapSensor");
	}

	public static String getOatSensor() {
		if(properties == null) {
			Utils.initializeProperties();
		}
		return properties.getProperty("oatSensor");
	}

	public static double getDefaultOatTemp() {
		if(properties == null) {
			Utils.initializeProperties();
		}
		return Double.parseDouble(properties.getProperty("defaultOat"));
	}
}
