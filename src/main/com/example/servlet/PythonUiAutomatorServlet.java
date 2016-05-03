package main.com.example.servlet;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.servlet.AsyncContext;
import javax.servlet.AsyncEvent;
import javax.servlet.AsyncListener;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import main.com.example.ADB;
import main.com.example.AndroidPythonUiautomatorExecutor;
import main.com.example.ExecuteRunnable;
import main.com.example.TestExecutor;
import main.com.example.entity.Device;
import main.com.example.entity.ExecutorBuilder;
import main.com.example.entity.TestData;
import main.com.example.utility.CoreOptions;
import net.lingala.zip4j.exception.ZipException;
import test.com.example.entity.Tool;

@WebServlet(name = "PythonUiAutomatorServlet", urlPatterns = { "/execute" }, asyncSupported=true)
@MultipartConfig
public class PythonUiAutomatorServlet extends HttpServlet {
	final String HTML_NAME_TESTSCRIPT = "testscript";
	final String HTML_NAME_MOBILE_SERIAL_NUMBER = "mobile_serial_number";
	final String HTML_NAME_WEAR_SERIAL_NUMBER = "wear_serial_number";
	final String TAG_REPORT = "report";
	final String TAG_REPORT_SIZE = TAG_REPORT + "_size";
	final String TAG_MOBILE = "mobile";
	final String TAG_WEAR = "wear";
	final String SETTING_PY = "Setting.py";
	final String[] POST_PARAMS = { HTML_NAME_TESTSCRIPT, "apk" };

	private Lock lock = new ReentrantLock();
	private LinkedList<AsyncContext> asyncContexts = new LinkedList<>();

	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//		Runnable runnable = new ExecuteRunnable(req);
//		java.util.concurrent.Executor executor = new MyExecutor();
//		executor.execute(runnable);
		System.out.println("doPost");
		
		TestData testData = null;
		try {
			testData = this.parseTestData(req);
		} catch (ZipException e) {
			e.printStackTrace();
		}
		ExecutorBuilder builder = new ExecutorBuilder();
		TestExecutor executor = builder.build(testData.getTool());
		List<String> output = null;
		try {
			output = this.executeTest(executor, testData);
		} catch (InterruptedException | ZipException e) {
			e.printStackTrace();
		}
		
		req.setAttribute(TAG_REPORT_SIZE, output.size());
		 int lineCnt = 1;
		 for (String line : output) {
		 req.setAttribute(TAG_REPORT + "_" + lineCnt++, line);
		 }
		 // go to "report.jsp"
		 req.getRequestDispatcher("report.jsp").forward(req, resp);
	}

	private TestData parseTestData(HttpServletRequest req) throws ServletException, IOException, ZipException {
		final String HTML_NAME_TESTSCRIPT = "testscript";
		TestData testData = new TestData();
		Part filePart = req.getPart(HTML_NAME_TESTSCRIPT);
		testData.setProject(filePart.getSubmittedFileName(), filePart.getInputStream());
		testData.setTool(parseTool(req));
		filePart.write(CoreOptions.UPLOAD_DIRECTORY + File.separator + filePart.getSubmittedFileName());
		testData.setDevices(this.parseDevices(req));
		return testData;
	}

	private List<Device> parseDevices(HttpServletRequest req) {
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

	// deprecated
	// protected void doPost(HttpServletRequest req, HttpServletResponse resp)
	// throws ServletException, IOException {
	// System.out.println("receive request");
	// HashMap<String, List<Device>> deviceNumber = parseDevices(req);
	//
	// try {
	// // upload file to server
	// for (Part part : req.getParts()) {
	// if(Arrays.asList(POST_PARAMS).contains(part.getName())){
	// uploadToServer(part);
	// }
	// }
	// List<String> output = executeTest(deviceNumber);
	//
	// // 將結果傳去report.jsp
	// req.setAttribute(TAG_REPORT_SIZE, output.size());
	// int lineCnt = 1;
	// for (String line : output) {
	// req.setAttribute(TAG_REPORT + "_" + lineCnt++, line);
	// }
	// // go to "report.jsp"
	// req.getRequestDispatcher("report.jsp").forward(req, resp);
	//
	// // After testing, delete the test scripts
	// deleteFile();
	// } catch (Exception e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// }

	private void uploadToServer(Part part) throws Exception {
		String fileName = getFileName(part);
		File file = new File(fileName);
		part.write(CoreOptions.UPLOAD_DIRECTORY + File.separator + file.getName());
	}

	private String getFileName(final Part part) {
		for (String content : part.getHeader("content-disposition").split(";")) {
			if (content.trim().startsWith("filename")) {
				return content.substring(content.indexOf('=') + 1).trim().replace("\"", "");
			}
		}
		return null;
	}

	private void deleteFile() {
		File uploadSpace = new File(CoreOptions.UPLOAD_DIRECTORY);
		File[] files = uploadSpace.listFiles();
		for (File file : files) {
			String name = file.getName();
			if (name.equals(SETTING_PY))
				continue;

			file.delete();
		}
	}

	// deprecated
	// private List<String> executeTest(HashMap<String, List<Device>>
	// deviceNumber)
	// throws IOException, InterruptedException {
	// List<String> output;
	// AndroidPythonUiautomatorExecutor executor = new
	// AndroidPythonUiautomatorExecutor(deviceNumber);
	// output = executor.executeTest();
	//
	// return output;
	// }

	// deprecated
	// private HashMap<String, List<Device>> parseDevices(HttpServletRequest
	// req) {
	// HashMap<String, List<Device>> deviceNumber = new HashMap<String,
	// List<Device>>();
	// List<Device> lstPhone = new ArrayList<Device>();
	// List<Device> lstWear = new ArrayList<Device>();
	// for (Device device : ADB.getDevices()) {
	// if (req.getParameter(device.getModelAliasWithDash()) != null) {
	// if (device.isWearable())
	// lstWear.add(device);
	// else
	// lstPhone.add(device);
	// }
	// }
	// deviceNumber.put(TAG_MOBILE, lstPhone);
	// deviceNumber.put(TAG_WEAR, lstWear);
	// return deviceNumber;
	// }
}

class MyExecutor implements java.util.concurrent.Executor {

	@Override
	public void execute(Runnable command) {
		command.run();
	}

}
