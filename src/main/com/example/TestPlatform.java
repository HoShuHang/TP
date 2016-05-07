package main.com.example;

import java.util.ArrayList;
import java.util.List;

import main.com.example.entity.*;

public class TestPlatform {
	private List<Device> devices, phones, wearable = null;

	public TestPlatform() throws InterruptedException {
		this.wearable = new ArrayList<Device>();
		this.phones = new ArrayList<Device>();
		getDevice();
	}

	public void getDevice() throws InterruptedException {
		this.devices = ADB.getDevices();
		for (Device device : devices) {
			if (device.isWearable()) {
				wearable.add(device);
			} else {
				phones.add(device);
			}
		}
	}

	public List<Device> getPhones() {
		return this.phones;
	}

	public List<Device> getWearable() {
		return this.wearable;
	}
}
