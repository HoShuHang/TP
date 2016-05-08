package main.com.example;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import main.com.example.entity.Device;
import main.com.example.entity.TestData;
import main.com.example.utility.CoreOptions;
import main.com.example.utility.Utility;
import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;

public interface TestExecutor {
	public List<String> report = new ArrayList<String>();
		
	public default void turnOffBluetooth(Device phone) throws IOException, InterruptedException {
		System.out.println("【turnOffBluetooth】 " + phone.getSerialNum());
		Utility.cmd(CoreOptions.PYTHON, CoreOptions.SCRIPT_DIR + "\\turnOffBluetooth.py", phone.getSerialNum());
	}

	public default void turnOnBluetooth(Device phone) throws IOException, InterruptedException {
		System.out.println("【turnOnBluetooth】 " + phone.getSerialNum());
		Utility.cmd(CoreOptions.PYTHON, CoreOptions.SCRIPT_DIR + "\\turnOnBluetooth.py", phone.getSerialNum());
	}

	public default void clearWearGms(Device wear) throws IOException, InterruptedException {
		System.out.println("【clearWearGms】 " + wear.getSerialNum());
		Utility.cmd(CoreOptions.PYTHON, CoreOptions.SCRIPT_DIR + "\\clearGms.py", wear.getSerialNum());
	}
	
	public default void unzipProject(TestData testData) throws IOException, ZipException{
		ZipFile zip = testData.getProject();
		zip.extractAll(CoreOptions.UPLOAD_DIRECTORY);
	}
	
	public default void execute(TestData testData) throws IOException, ZipException, InterruptedException{
		unzipProject(testData);
		executeTest(testData);
	}
	
	public void executeTest(TestData testData) throws IOException, InterruptedException, ZipException;
	
	public default List<String> getTestReport() {
		return report;
	}
}
