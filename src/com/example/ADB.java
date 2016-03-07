package com.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import com.example.entity.Device;

public class ADB {
	private final static String ANDROID_HOME = System.getenv("ANDROID_HOME");
	private final static String ADB = "/Users/adt-bundle-mac-x86_64-20140702/sdk/platform-tools/adb";
	
	public static String show() {
		return System.getenv("ANDROID_HOME");
	}
	
	public static List<Device> getDevices(){
		List<Device> lstDevices = new ArrayList<Device>();
		for(String deviceSerialNum : getDeviceSerialNums()){
			List<String> modelAliasResult = adbCmd(ADB, "-s", deviceSerialNum, "shell", "getprop", "ro.product.modelalias");
			List<String> serialNumResult = adbCmd(ADB, "-s", deviceSerialNum, "shell", "getprop", "ro.serialno");
			if(modelAliasResult != null && !modelAliasResult.isEmpty()){
				String modelAlias = modelAliasResult.get(0);
				String serialNum = serialNumResult.get(0);
				lstDevices.add(new Device(serialNum, modelAlias));
			}
		}
		return lstDevices;
	}

	public static List<String> getDeviceSerialNums() {
		List<String> lstDevices = new ArrayList<String>();
		adbCmd(ADB, "kill-server");
		adbCmd(ADB, "start-server");
		List<String> lstResults = adbCmd(ADB, "devices");

		for(String line : lstResults){
			if(!line.contains("List of devices attached"))
				lstDevices.add(line.split("	")[0]);
		}
		return lstDevices;

	}
	
//	public static List<String> getDeviceName(){
//		List<String> lstDevices = new ArrayList<String>();
//		for(String deviceSerialNum : getDeviceSerialNums()){
//			List<String> result = adbCmd(ADB, "-s", deviceSerialNum, "shell", "getprop", "ro.product.modelalias");
//			if(result != null && !result.isEmpty())
//				lstDevices.add(result.get(0));
//		}
//		return lstDevices;
//	}

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
}
