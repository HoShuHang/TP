package main.com.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import main.com.example.entity.Device;
import main.com.example.utility.CoreOptions;
import main.com.example.utility.Utility;

public class ADB {
	private static List<Device> devices = null;

	public static String show() {
		return System.getenv("ANDROID_HOME");
	}

	public static List<Device> getDevices() throws InterruptedException {
		if (devices == null || devices.isEmpty()) {
			devices = new ArrayList<Device>();
			for (String deviceSerialNum : getDeviceSerialNums()) {
				List<String> manufactorResult = getDeviceProp(deviceSerialNum, "ro.product.manufacturer");
				List<String> modelResult = getDeviceProp(deviceSerialNum, "ro.product.model");
				List<String> serialNumResult = getDeviceProp(deviceSerialNum, "ro.serialno");
				List<String> characteristicResult = getDeviceProp(deviceSerialNum, "ro.build.characteristics");
				if (manufactorResult != null && !manufactorResult.isEmpty()) {
					String modelAlias;
					if (modelResult.get(0).toLowerCase().contains(manufactorResult.get(0).toLowerCase()))
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

	public static List<String> getDeviceSerialNums() throws InterruptedException {
		List<String> lstDevices = new ArrayList<String>();
		List<String> lstResults = null;
		Utility.cmd(CoreOptions.ADB, "kill-server");
		Utility.cmd(CoreOptions.ADB, "start-server");
		lstResults = Utility.cmd(CoreOptions.ADB, "devices");
		for (String line : lstResults) {
			if (!line.contains("List of devices attached") && !line.isEmpty())
				lstDevices.add(line.split("	")[0]);
		}
		return lstDevices;
	}

	private static List<String> getDeviceProp(String deviceSerialNum, String prop) {
		return Utility.cmd(CoreOptions.ADB, "-s", deviceSerialNum, "shell", "getprop", prop);
	}
}
