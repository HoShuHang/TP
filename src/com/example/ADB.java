package com.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class ADB {
	private final static String ANDROID_HOME = System.getenv("ANDROID_HOME");
	private final static String ADB = ANDROID_HOME + "\\platform-tools\\adb.exe";
	
	public static String show() {
		return System.getenv("ANDROID_HOME");
	}

	public static List<String> getDevices() {
		List<String> lstDevices = new ArrayList<String>();
		adbCmd(ADB, "kill-server");
		adbCmd(ADB, "start-server");
		List<String> lstResults = adbCmd(ADB, "devices");

		for(String line : lstResults){
			if(line.contains("List of devices attached")) continue;
			lstDevices.add(line.split("	")[0]);
		}
		return lstDevices;

	}
	
	public static List<String> getDeviceName(){
		List<String> lstDevices = new ArrayList<String>();
		ProcessBuilder proc = new ProcessBuilder(ADB, "devices", "-l");
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
}
