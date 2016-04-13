package main.com.example;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;


import main.com.example.entity.Device;
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
	final String UPLOAD_DIRECTORY = "D:\\Thesis\\UploadSpace";
	final String SETTING_PY = "Setting.py";

	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		System.out.println("receive request");

		HashMap<String, List<Device>> deviceNumber = parseDevices(req);
		final String testScriptLocation = CoreOptions.TEST_SCRIPT_DIR + "\\" + req.getParameter(HTML_NAME_TESTSCRIPT);

		try {
			// upload file to server
			for (Part part : req.getParts()) {
				if (HTML_NAME_TESTSCRIPT.equals(part.getName()) || "apk".equals(part.getName())) {
					uploadToServer(part);
				}
			}
			List<String> output = executeTest(deviceNumber);

			// 將結果傳去report.jsp
			req.setAttribute(TAG_REPORT_SIZE, output.size());
			int lineCnt = 1;
			for (String line : output) {
				req.setAttribute(TAG_REPORT + "_" + lineCnt++, line);
			}
			// go to "report.jsp"
			req.getRequestDispatcher("report.jsp").forward(req, resp);

			// After testing, delete the test scripts
			deleteFile();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void uploadToServer(Part part) throws Exception {
		String fileName = getFileName(part);
		File file = new File(fileName);
		part.write(UPLOAD_DIRECTORY + File.separator + file.getName());
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
		File uploadSpace = new File(UPLOAD_DIRECTORY);
		File[] files = uploadSpace.listFiles();
		for (File file : files) {
			String name = file.getName();
			if (name.equals(SETTING_PY))
				continue;

			file.delete();
		}
	}

	private List<String> executeTest(HashMap<String, List<Device>> deviceNumber)
			throws IOException, InterruptedException {
		List<String> output;
		AndroidPythonUiautomatorExecutor executor = new AndroidPythonUiautomatorExecutor(deviceNumber);
		output = executor.executeTest();

		return output;
	}

	private HashMap<String, List<Device>> parseDevices(HttpServletRequest req) {
		HashMap<String, List<Device>> deviceNumber = new HashMap<String, List<Device>>();
		List<Device> lstPhone = new ArrayList<Device>();
		List<Device> lstWear = new ArrayList<Device>();
		for (Device device : ADB.getDevices()) {
			if (req.getParameter(device.getModelAliasWithDash()) != null) {
				if (device.isWearable())
					lstWear.add(device);
				else
					lstPhone.add(device);
			}
		}
		deviceNumber.put(TAG_MOBILE, lstPhone);
		deviceNumber.put(TAG_WEAR, lstWear);
		return deviceNumber;
	}
}
