package com.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class ADB {
	public static String show() {
		return System.getenv("ANDROID_HOME");
	}
	
	public static String getSerialNumber(){
		BufferedReader results = null;
		String serialNumber = "";
		List<String> lstDevices = new ArrayList<String>();
		try {
			final String ANDROID_HOME = System.getenv("ANDROID_HOME");
			final String ADB = ANDROID_HOME + "\\platform-tools\\adb.exe";
			ProcessBuilder proc = new ProcessBuilder(ADB, "devices");
			Process p = proc.start();
			results = new BufferedReader(new InputStreamReader(p.getInputStream()));
			String line = results.readLine();
			line = results.readLine();
			while(!line.contains("device")){
				p = proc.start();
				results = new BufferedReader(new InputStreamReader(p.getInputStream()));
				line = results.readLine();
				line = results.readLine();
			}
			serialNumber = line.split("	")[0];
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return serialNumber;
	}
	
	public static List<String> getDevices(){
		List<String> lstDevices = new ArrayList<String>();
		try {
			final String ANDROID_HOME = System.getenv("ANDROID_HOME");
			final String ADB = ANDROID_HOME + "\\platform-tools\\adb.exe";
			ProcessBuilder proc = new ProcessBuilder(ADB, "devices");
			Process p = proc.start();
			BufferedReader results = new BufferedReader(new InputStreamReader(p.getInputStream()));
			String line = results.readLine();
			line = results.readLine();
			if(line != null){
				while(!line.contains("device")){
					//p = proc.start();
					results = new BufferedReader(new InputStreamReader(p.getInputStream()));
					line = results.readLine();
					line = results.readLine();
				}
				while(line != null && line != ""){
					lstDevices.add(line.split("	")[0]);
					line = results.readLine();
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return lstDevices;
	}
	
//	public void shell(String command){
//		final String ANDROID_HOME = System.getenv("ANDROID_HOME");
//		final String ADB = ANDROID_HOME + "\\platform-tools\\adb.exe";
//		System.out.println(command);
//		ProcessBuilder proc = new ProcessBuilder(ADB, command);
//		try {
//			Process p = proc.start();
//			BufferedReader results = new BufferedReader(new InputStreamReader(p.getInputStream()));
//			
//			String line = "";
//			while((line = results.readLine()) != null){
//				System.out.println(line);
//			}
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			
//			e.printStackTrace();
//		}
//	}
}
