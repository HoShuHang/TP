package com.example;

import java.io.File;
import java.io.IOException;
import java.sql.Time;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.exec.DefaultExecutor;

import com.example.entity.Device;

@WebServlet(name = "PythonUiAutomatorServlet", urlPatterns = { "/PythonUiAutomatorServlet" })
public class PythonUiAutomatorServlet extends HttpServlet {
	final String HTML_NAME_TESTSCRIPT = "testscript";
	final String HTML_NAME_MOBILE_SERIAL_NUMBER = "mobile_serial_number";
	final String HTML_NAME_WEAR_SERIAL_NUMBER = "wear_serial_number";
	final String TAG_REPORT = "report";
	final String TAG_REPORT_SIZE = TAG_REPORT + "_size";
	final String TAG_MOBILE = "mobile";
	final String TAG_WEAR = "wear";

	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		System.out.println("receive request");
		System.out.println("req = " + resp);
		final String testScriptLocation = req.getParameter(HTML_NAME_TESTSCRIPT);
		String test = req.getParameter("Acer_V370");

		// 從index.jsp 取得mobile和wearable的serial number
		HashMap<String, String> deviceNumber = new HashMap<String, String>();
		for(Device device : ADB.getDevices()){
			if(req.getParameter(device.getModelAliasWithDash())!=null)
				deviceNumber.put(TAG_MOBILE, device.getSerialNum());
		}
//		deviceNumber.put(TAG_MOBILE, req.getParameter(HTML_NAME_MOBILE_SERIAL_NUMBER));
//		deviceNumber.put(TAG_WEAR, req.getParameter(HTML_NAME_WEAR_SERIAL_NUMBER));

		// 執行 command "python xxxxxxx.py" 並取得執行結果
		List<String> output;
		AndroidPythonUiautomatorExecutor builder = new AndroidPythonUiautomatorExecutor(testScriptLocation,
				deviceNumber);
		output = builder.executeTest();
		
		// 將結果傳去report.jsp
		req.setAttribute(TAG_REPORT_SIZE, output.size());
		int lineCnt = 1;
		for (String line : output) {
			req.setAttribute(TAG_REPORT + "_" + lineCnt++, line);
		}

		// go to "report.jsp"
		req.getRequestDispatcher("report.jsp").forward(req, resp);
		
		System.out.println("finish");
	}
}
