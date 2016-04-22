package main.com.example.servlet;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import main.com.example.ADB;
import main.com.example.AndroidPythonUiautomatorExecutor;
import main.com.example.Executor;
import main.com.example.entity.Device;
import main.com.example.entity.TestData;
import main.com.example.utility.CoreOptions;

@WebServlet(name = "PythonUiAutomatorServlet", urlPatterns = { "/PythonUiAutomatorServlet" })
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

	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		TestData testData = this.parseTestData(req);
		Executor executor = new AndroidPythonUiautomatorExecutor();
		try {
			this.executeTest(executor, testData);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private TestData parseTestData(HttpServletRequest req) throws ServletException, IOException {
		TestData testData = new TestData();
		Part filePart = req.getPart(HTML_NAME_TESTSCRIPT);
		testData.setProject(filePart.getSubmittedFileName(), filePart.getInputStream());
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

	private List<String> executeTest(Executor executor, TestData testData) throws IOException, InterruptedException {
		return executor.executeTest(testData);
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
