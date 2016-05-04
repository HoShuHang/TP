package main.com.example;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.AsyncContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;

import main.com.example.entity.Device;
import main.com.example.entity.ExecutorBuilder;
import main.com.example.entity.TestData;
import main.com.example.entity.Tool;
import main.com.example.utility.CoreOptions;
import net.lingala.zip4j.exception.ZipException;

public class ExecuteRunnable implements Runnable {
	private AsyncContext asyncContext;
	private HttpServletRequest req;
	private TestData testData;

	public ExecuteRunnable(HttpServletRequest req) {
		this.req = req;
	}

	@Override
	public void run(){
		List<String> report = null;
		try {
			this.testData = this.parseTestData(this.req);
			ExecutorBuilder builder = new ExecutorBuilder();
			TestExecutor executor = builder.build(this.testData.getTool());
			report = this.executeTest(executor, this.testData);
		} catch (ServletException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ZipException e) {
			e.printStackTrace();
		}
		System.out.println("run");
//		asyncContext.complete();
	}

	private TestData parseTestData(HttpServletRequest req) throws ServletException, IOException, ZipException, InterruptedException {
		final String HTML_NAME_TESTSCRIPT = "testscript";
		TestData testData = new TestData();
		Part filePart = req.getPart(HTML_NAME_TESTSCRIPT);
		testData.setProject(filePart.getSubmittedFileName(), filePart.getInputStream());
		testData.setTool(parseTool(req));
		filePart.write(CoreOptions.UPLOAD_DIRECTORY + File.separator + filePart.getSubmittedFileName());
		testData.setDevices(this.parseDevices(req));
		return testData;
	}

	private List<Device> parseDevices(HttpServletRequest req) throws InterruptedException {
		List<Device> devices = new ArrayList<Device>();
		for (Device device : ADB.getDevices()) {
			if (req.getParameter(device.getModelAliasWithDash()) != null) {
				devices.add(device);
			}
		}
		return devices;
	}

	private Tool parseTool(HttpServletRequest req) {
		String test = req.getParameter("tool");
		if (test.equals("uiautomator"))
			return Tool.UIAutomator;
		else
			return Tool.RobotFramework;
	}
	
	private List<String> executeTest(TestExecutor executor, TestData testData) throws IOException, InterruptedException, ZipException {
		executor.execute(testData);
		return executor.getTestReport();
	}
}
