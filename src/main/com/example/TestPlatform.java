package main.com.example;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import main.com.example.entity.Device;
import main.com.example.entity.ExecutorBuilder;
import main.com.example.entity.Pair;
import main.com.example.entity.Report;
import main.com.example.entity.TestData;
import net.lingala.zip4j.exception.ZipException;

public class TestPlatform {
	private List<Device> devices, phones, wearable = null;
	private TestData testData;

	public TestPlatform() throws InterruptedException, IOException {
		this.wearable = new ArrayList<Device>();
		this.phones = new ArrayList<Device>();
		getDevice();
	}

	public void getDevice() throws InterruptedException, IOException {
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

	public void setTestData(TestData testData) {
		this.testData = testData;
	}

	public void execute(TestData testData) {
		ExecutorService executor = Executors.newFixedThreadPool(5);
		Runnable runnable = new ExecuteThread(testData);
		executor.execute(runnable);
		executor.shutdown();
//		ExecutorBuilder builder = new ExecutorBuilder();
//		TestExecutor executor = builder.build(this.testData.getTool());
//		List<Report> output = null;
//		try {
//			output = this.executeTest(executor, this.testData);
//		} catch (InterruptedException | ZipException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		return output;
	}

	public List<Pair> getPairs() {
		return this.testData.getPairs();
	}

	private List<Report> executeTest(TestExecutor executor, TestData testData)
			throws IOException, InterruptedException, ZipException {
		executor.execute(testData);
		return executor.getTestReport();
	}
}
