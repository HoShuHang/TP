package main.com.example;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
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
	// final String TAG_MOBILE = "mobile";
	// final String TAG_WEAR = "wear";
	final String UPLOAD_DIRECTORY = "D:\\Thesis\\UploadSpace";
	// final String SETTING_PY = "Setting.py";
	final String REPORT_PATH = "D:\\Thesis\\UploadSpace\\report.html";

	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		System.out.println("【RobotframeworkServlet】 receive request");
		
		System.out.println(REPORT_PATH);
		try {
			for (Part part : req.getParts()) {
				if (HTML_NAME_TESTSCRIPT.equals(part.getName())) {
					uploadToServer(part);

				}
			}
			List<String> output = executeTest(null);
			PrintWriter out = resp.getWriter();
			for(String line : output){
				out.println(line);
			}
			out.close();
//			req.getRequestDispatcher(REPORT_PATH).forward(req, resp);
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

	private List<String> executeTest(HashMap<String, List<Device>> deviceNumber)
			throws IOException, InterruptedException {
		List<String> output;
		RobotframeworkExecutor executor = new RobotframeworkExecutor(deviceNumber);
		output = executor.executeTest();

		return output;
	}
}
