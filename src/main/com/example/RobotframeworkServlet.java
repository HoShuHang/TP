package main.com.example;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
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

@WebServlet(name = "RobotframeworkServlet", urlPatterns = { "/RobotframeworkServlet" })
@MultipartConfig
public class RobotframeworkServlet extends HttpServlet {
	final String HTML_NAME_TESTSCRIPT = "testscript";
	// final String HTML_NAME_MOBILE_SERIAL_NUMBER = "mobile_serial_number";
	// final String HTML_NAME_WEAR_SERIAL_NUMBER = "wear_serial_number";
	// final String TAG_REPORT = "report";
	// final String TAG_REPORT_SIZE = TAG_REPORT + "_size";
	final String TAG_MOBILE = "mobile";
	final String TAG_WEAR = "wear";
	final String UPLOAD_DIRECTORY = "D:\\Thesis\\UploadSpace";
	String outputDirPath;
	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		System.out.println("【RobotframeworkServlet】 receive request");
		HashMap<String, List<Device>> deviceNumber = parseDevices(req);
		try {
			for (Part part : req.getParts()) {
				if (HTML_NAME_TESTSCRIPT.equals(part.getName())) {
					uploadToServer(part);
				}
			}

			outputDirPath = getServletContext().getRealPath("/") + "reports";
			createOutputDir();
			executeTest(deviceNumber);
			setRequestAttribute(deviceNumber, req);
			req.getRequestDispatcher("report_list.jsp").forward(req, resp);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void createOutputDir(){
		System.out.println(outputDirPath);
		File outputDir = new File(outputDirPath);
		if(!outputDir.exists()){
			outputDir.mkdirs();
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

	private List<String> executeTest(HashMap<String, List<Device>> deviceNumber)
			throws IOException, InterruptedException {
		List<String> output;
		AndroidRobotframeworkExecutor executor = new AndroidRobotframeworkExecutor(deviceNumber);
		executor.setOutputDirPath(outputDirPath);
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
	
	private void setRequestAttribute(HashMap<String, List<Device>> deviceNumber, HttpServletRequest req){
		List<Device> lstPhone = deviceNumber.get(TAG_MOBILE);
		List<Device> lstWear = deviceNumber.get(TAG_WEAR);
		List<String> lstPairedDeviceFolder = new ArrayList<String>();
		
		for (Device phone : lstPhone) {
			for (Device wear : lstWear) {
				lstPairedDeviceFolder.add(phone.getSerialNum() + "_" + wear.getSerialNum());
			}
		}
		req.setAttribute("folder", lstPairedDeviceFolder);
	}
}
