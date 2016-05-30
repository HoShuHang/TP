package main.com.example.servlet;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.servlet.AsyncContext;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import main.com.example.ADB;
import main.com.example.TestExecutor;
import main.com.example.TestPlatform;
import main.com.example.entity.Device;
import main.com.example.entity.ExecutorBuilder;
import main.com.example.entity.Report;
import main.com.example.entity.TestData;
import main.com.example.entity.Tool;
import main.com.example.utility.CoreOptions;
import net.lingala.zip4j.exception.ZipException;

@WebServlet(name = "PythonUiAutomatorServlet", urlPatterns = { "/execute" }, asyncSupported = true)
@MultipartConfig
public class PythonUiAutomatorServlet extends HttpServlet {
//	final String HTML_NAME_TESTSCRIPT = "testscript";
//	final String HTML_NAME_MOBILE_SERIAL_NUMBER = "mobile_serial_number";
//	final String HTML_NAME_WEAR_SERIAL_NUMBER = "wear_serial_number";
//	final String TAG_REPORT = "report";
//	final String TAG_REPORT_SIZE = TAG_REPORT + "_size";
//	final String TAG_MOBILE = "mobile";
//	final String TAG_WEAR = "wear";
//	final String SETTING_PY = "Setting.py";
//	final String[] POST_PARAMS = { HTML_NAME_TESTSCRIPT, "apk" };
	final String TAG_REPORT_LIST = "report_list";
	private Lock lock = new ReentrantLock();
	private LinkedList<AsyncContext> asyncContexts = new LinkedList<>();

	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// Runnable runnable = new ExecuteRunnable(req);
		// java.util.concurrent.Executor executor = new MyExecutor();
		// executor.execute(runnable);
		System.out.println("doPost");

		TestData testData = null;
		try {
			testData = this.parseTestData(req);
		} catch (ZipException | InterruptedException e) {
			e.printStackTrace();
		}
		TestPlatform testPlatform = (TestPlatform) this.getServletContext().getAttribute("testPlatform");
		List<Report> output = testPlatform.execute(testData);
		ServletContext application = this.getServletContext();
		application.setAttribute(TAG_REPORT_LIST, output);
		// go to "report.jsp"
		resp.sendRedirect("report_list.jsp");
	}

	private TestData parseTestData(HttpServletRequest req)
			throws ServletException, IOException, ZipException, InterruptedException {
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
}

class MyExecutor implements java.util.concurrent.Executor {

	@Override
	public void execute(Runnable command) {
		command.run();
	}

}
