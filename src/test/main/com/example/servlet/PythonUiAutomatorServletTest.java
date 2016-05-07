package test.main.com.example.servlet;

import javax.servlet.http.HttpServletRequest;

import org.easymock.EasyMockSupport;
import org.easymock.internal.matchers.NotNull;
import org.junit.Before;
import org.junit.Test;

import main.com.example.ADB;
import main.com.example.entity.Device;
import main.com.example.entity.Tool;
import main.com.example.servlet.PythonUiAutomatorServlet;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class PythonUiAutomatorServletTest {
	private PythonUiAutomatorServlet servlet = null;
	private HttpServletRequest mockReq = null;

	@Before
	public void setUp() {
		this.servlet = new PythonUiAutomatorServlet();
		this.mockReq = createMock(HttpServletRequest.class);
	}

	@Test
	public void testGetToolWhenRequestUiAutomator() throws NoSuchMethodException, SecurityException,
			IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		expect(this.mockReq.getParameter("tool")).andReturn("uiautomator");
		replay(this.mockReq);
		Class[] cArg = new Class[1];
		cArg[0] = HttpServletRequest.class;
		Method method = PythonUiAutomatorServlet.class.getDeclaredMethod("parseTool", cArg);
		method.setAccessible(true);
		Tool tool = (Tool) method.invoke(this.servlet, this.mockReq);
		assertTrue(tool.equals(Tool.UIAutomator));
	}

	@Test
	public void testGetTOllWhenRequestRobotFramework() throws NoSuchMethodException, SecurityException,
			IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		expect(this.mockReq.getParameter("tool")).andReturn("robotframework");
		replay(this.mockReq);
		// assert private method
		Class[] cArg = new Class[1];
		cArg[0] = HttpServletRequest.class;
		Method method = PythonUiAutomatorServlet.class.getDeclaredMethod("parseTool", cArg);
		method.setAccessible(true);
		Tool tool = (Tool) method.invoke(this.servlet, this.mockReq);
		assertTrue(tool.equals(Tool.RobotFramework));
	}

	@Test
	public void testParseDevice() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
		//set ADB data
		List<Device> devices = new ArrayList<Device>();
		final String modelAlias = "HSS model alias";
		Device device = new Device("123456789", modelAlias, "nosdcard");
		devices.add(device);
		Field devicesField;
		devicesField = ADB.class.getDeclaredField("devices");
		devicesField.setAccessible(true);
		devicesField.set(null, devices);
		// mock req
		HttpServletRequest req = createMock(HttpServletRequest.class);
		expect(req.getParameter("HSS_model_alias")).andReturn(modelAlias);
		replay(req);
		// assert private method
		Class[] cArg = new Class[1];
		cArg[0] = HttpServletRequest.class;
		Method method = PythonUiAutomatorServlet.class.getDeclaredMethod("parseDevices", cArg);
		method.setAccessible(true);
		List<Device> actual = (List<Device>) method.invoke(this.servlet, req);
		assertTrue(actual.size() == 1);
		assertTrue(actual.get(0).equals(device));
	}
}
