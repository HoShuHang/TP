package main.com.example.servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import main.com.example.TestPlatform;
import main.com.example.entity.Device;

@WebServlet(name = "StartTestServlet", urlPatterns = { "/device" })
public class StartTestServlet extends HttpServlet {
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		TestPlatform testPlatform;
		testPlatform = (TestPlatform) this.getServletContext().getAttribute("testPlatform");
		List<Device> phones = testPlatform.getPhones();
		List<Device> wearable = testPlatform.getWearable();
		req.setAttribute("phones", phones);
		req.setAttribute("wearable", wearable);
		req.getRequestDispatcher("/execute.jsp").forward(req, resp);
	}
}
