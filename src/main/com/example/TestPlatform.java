package main.com.example;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import main.com.example.entity.Device;
import main.com.example.entity.ExecutorBuilder;
import main.com.example.entity.TestData;
import net.lingala.zip4j.exception.ZipException;

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
	
	public List<HashMap<String, Object>> execute(TestData testData) {
		ExecutorBuilder builder = new ExecutorBuilder();
		TestExecutor executor = builder.build(testData.getTool());
		List<HashMap<String, Object>> output = null;
		try {
			output = this.executeTest(executor, testData);
		} catch (InterruptedException | ZipException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return output;
	}

	private List<HashMap<String, Object>> executeTest(TestExecutor executor, TestData testData)
			throws IOException, InterruptedException, ZipException {
		executor.execute(testData);
		return executor.getTestReport();
	}
}
