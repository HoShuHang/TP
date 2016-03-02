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
	
	public static String getSerialNumber(){
		BufferedReader results = null;
		String serialNumber = "";
		List<String> lstDevices = new ArrayList<String>();
		try {
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
		String[] lines = buildProcess(ADB, "devices").split("\n");
		for(int index = 1; index < lines.length; index++){
			lstDevices.add(lines[index].split("\t")[0]);
		}
		return lstDevices;
	}
	
	public static List<String> getDeviceName(){
		List<String> lstDevices = new ArrayList<String>();
		ProcessBuilder proc = new ProcessBuilder(ADB, "devices", "-l");
		return lstDevices;
	}
	
	public static String buildProcess(String... strs){
		ProcessBuilder proc = new ProcessBuilder(strs);
		String newLine = "", result = "";
		try {
			Process p = proc.start();
			BufferedReader results = new BufferedReader(new InputStreamReader(p.getInputStream()));
			while((newLine = results.readLine()) != null && !newLine.isEmpty()){
				result = result + newLine + "\n";
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
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
