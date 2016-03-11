package com.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import com.example.entity.Device;
import com.example.utility.CoreOptions;

public class ADB {
	private static List<Device> devices = null;
	
	public static String show() {
		return System.getenv("ANDROID_HOME");
	}
	
	public static List<Device> getDevices(){
		if(devices == null || devices.isEmpty()){
			devices = new ArrayList<Device>();
			for(String deviceSerialNum : getDeviceSerialNums()){
				List<String> manufactorResult = getDeviceProp(deviceSerialNum, "ro.product.manufacturer");
				List<String> modelResult = getDeviceProp(deviceSerialNum, "ro.product.model");
				List<String> serialNumResult = getDeviceProp(deviceSerialNum, "ro.serialno");
				List<String> characteristicResult = getDeviceProp(deviceSerialNum, "ro.build.characteristics");
				if(manufactorResult != null && !manufactorResult.isEmpty()){
					String modelAlias;
					if(modelResult.get(0).toLowerCase().contains(manufactorResult.get(0).toLowerCase()))
						modelAlias = modelResult.get(0);
					else
						modelAlias = manufactorResult.get(0) + " " + modelResult.get(0);
					String serialNum = serialNumResult.get(0);
					String characteristic = characteristicResult.get(0);
					devices.add(new Device(serialNum, modelAlias, characteristic));
				}
			}
		}
		return devices;
	}

	public static List<String> getDeviceSerialNums() {
		List<String> lstDevices = new ArrayList<String>();
		adbCmd(CoreOptions.ADB, "kill-server");
		adbCmd(CoreOptions.ADB, "start-server");
		List<String> lstResults = adbCmd(CoreOptions.ADB, "devices");

		for(String line : lstResults){
			if(!line.contains("List of devices attached") && !line.isEmpty())
				lstDevices.add(line.split("	")[0]);
		}
		return lstDevices;

	}

	public static List<String> adbCmd(String... command) {
		List<String> lstResults = new ArrayList<String>();
//		System.out.println(command);
		ProcessBuilder proc = new ProcessBuilder(command);
		try {
			Process p = proc.start();
			BufferedReader results = new BufferedReader(new InputStreamReader(p.getInputStream()));

			String line = "";
			while ((line = results.readLine()) != null) {
				lstResults.add(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return lstResults;
	}
	
	private static List<String> getDeviceProp(String deviceSerialNum, String prop){
		return adbCmd(CoreOptions.ADB, "-s", deviceSerialNum, "shell", "getprop", prop);
	}
}
