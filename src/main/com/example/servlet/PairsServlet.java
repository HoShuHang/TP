package main.com.example.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import main.com.example.TestPlatform;
import main.com.example.entity.Pair;
import main.com.example.entity.Report;
import main.com.example.entity.TestData;
import net.lingala.zip4j.exception.ZipException;

@WebServlet(name = "PairsServlet", urlPatterns = { "/pairs" })
public class PairsServlet extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
		TestPlatform testPlatform;
		testPlatform = (TestPlatform) this.getServletContext().getAttribute("testPlatform");
		// req.setAttribute("pairs", testPlatform.getPairs());
		// req.getRequestDispatcher("/report_list.jsp").forward(req, resp);
		PrintWriter out = resp.getWriter();
		try {
			out.println(this.responseJSON(testPlatform.getPairs()));
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	private JSONObject responseJSON(List<Pair> pairs) throws JSONException {
		JSONObject responseJSON = new JSONObject();
		JSONArray pairsJSONArray = new JSONArray();
		for (Pair pair : pairs) {
			Map<String, String> map = new HashMap();
			map.put("phone", pair.getPhone().getSerialNum());
			map.put("watch", pair.getWear().getSerialNum());
			if (pair.isTestComplete())
				map.put("status", "complete");
			else
				map.put("status", "waiting for test");
			if (pair.isTestComplete()){
				map.put("detail", String.valueOf(this.parseReport(pair.getReport())));
				System.out.println("detail = " + String.valueOf(this.parseReport(pair.getReport())));
			}else
				map.put("detail", "");
			JSONObject subJSON = new JSONObject(map);
			pairsJSONArray.put(subJSON);
		}
		responseJSON.put("pairs", pairsJSONArray);
		responseJSON.put("all_complete", this.isAllComplete(pairs));
		return responseJSON;
	}

	private Map parseReport(Report report) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("total", String.valueOf(report.getTotalTestCase()));
		map.put("fail", String.valueOf(report.getFailTestCaseNumber()));
		map.put("message", String.valueOf(report.getTestingMessage()));
		return map;
	}

	private boolean isAllComplete(List<Pair> pairs) {
		for (Pair pair : pairs) {
			if (!pair.isTestComplete())
				return false;
		}
		return true;
	}
}
