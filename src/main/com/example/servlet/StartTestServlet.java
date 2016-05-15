package main.com.example.servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import main.com.example.TestExecutor;
import main.com.example.TestPlatform;
import main.com.example.entity.Device;
import main.com.example.entity.ExecutorBuilder;
import main.com.example.entity.TestData;
import net.lingala.zip4j.exception.ZipException;

@WebServlet(name = "StartTestServlet", urlPatterns = { "/device" })
public class StartTestServlet extends HttpServlet{
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		TestPlatform testPlatform;
		try {
			testPlatform = new TestPlatform();
			List<Device> phones = testPlatform.getPhones();
			List<Device> wearable = testPlatform.getWearable();
			req.setAttribute("phones", phones);
			req.setAttribute("wearable", wearable);
			req.getRequestDispatcher("/execute.jsp").forward(req, resp);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
