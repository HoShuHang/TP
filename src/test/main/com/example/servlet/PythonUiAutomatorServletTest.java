package test.main.com.example.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;

import org.easymock.EasyMockSupport;
import org.easymock.internal.matchers.NotNull;
import org.junit.Before;
import org.junit.Test;

import main.com.example.ADB;
import main.com.example.entity.Device;
import main.com.example.entity.TestData;
import main.com.example.entity.Tool;
import main.com.example.servlet.PythonUiAutomatorServlet;
import test.main.com.example.entity.MyPart;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
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
	public void testGetToolWhenRequestRobotFramework() throws NoSuchMethodException, SecurityException,
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
	public void testParseDevice() throws NoSuchFieldException, SecurityException, IllegalArgumentException,
			IllegalAccessException, NoSuchMethodException, InvocationTargetException {
		// set ADB data
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

	@Test
	public void testParseDeviceWhenGetModelAliasNull() throws NoSuchFieldException, SecurityException,
			IllegalArgumentException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
		// set ADB data
		List<Device> devices = new ArrayList<Device>();
		final String modelAlias = "HSS model alias";
		Device deviceA = new Device("123456789", modelAlias, "nosdcard");
		Device deviceB = new Device("987654321", "model alias", "nosdcard");
		devices.add(deviceA);
		devices.add(deviceB);
		Field devicesField;
		devicesField = ADB.class.getDeclaredField("devices");
		devicesField.setAccessible(true);
		devicesField.set(null, devices);
		// mock req
		HttpServletRequest req = createMock(HttpServletRequest.class);
		expect(req.getParameter("HSS_model_alias")).andReturn(modelAlias);
		expect(req.getParameter("model_alias")).andReturn(null);
		replay(req);
		// assert private method
		Class[] cArg = new Class[1];
		cArg[0] = HttpServletRequest.class;
		Method method = PythonUiAutomatorServlet.class.getDeclaredMethod("parseDevices", cArg);
		method.setAccessible(true);
		List<Device> actual = (List<Device>) method.invoke(this.servlet, req);
		assertTrue(actual.size() == 1);
		assertTrue(actual.get(0).equals(deviceA));
	}

	@Test
	public void testParseTestData() throws NoSuchFieldException, SecurityException, IllegalArgumentException,
			IllegalAccessException, IOException, ServletException, NoSuchMethodException, InvocationTargetException {
		final String HTML_NAME_TESTSCRIPT = "testscript";
		// set ADB data
		List<Device> devices = new ArrayList<Device>();
		final String modelAlias = "HSS model alias";
		Device deviceA = new Device("123456789", modelAlias, "nosdcard");
		devices.add(deviceA);
		Field devicesField = ADB.class.getDeclaredField("devices");
		devicesField.setAccessible(true);
		devicesField.set(null, devices);
		// mock part
		String expectFileName = "script.zip";
		InputStream expectInputStream = new ByteArrayInputStream(expectFileName.getBytes(StandardCharsets.UTF_8));;
		Part mockPart = new MyPart();
		Field submittedFileNameField = MyPart.class.getDeclaredField("submittedFileName");
		Field inputStreamField = MyPart.class.getDeclaredField("inputStream");
		submittedFileNameField.setAccessible(true);
		inputStreamField.setAccessible(true);
		submittedFileNameField.set(mockPart, expectFileName);
		inputStreamField.set(mockPart, expectInputStream);
		// mock req
		HttpServletRequest req = createMock(HttpServletRequest.class);
		expect(req.getParameter("HSS_model_alias")).andReturn(modelAlias);
		expect(req.getPart(HTML_NAME_TESTSCRIPT)).andReturn(mockPart);
		expect(req.getParameter("tool")).andReturn("uiautomator");
		replay(req);
		// assert private method
		Class[] cArg = new Class[1];
		cArg[0] = HttpServletRequest.class;
		Method method = PythonUiAutomatorServlet.class.getDeclaredMethod("parseTestData", cArg);
		method.setAccessible(true);
		TestData actualTestData = (TestData) method.invoke(this.servlet, req);
		// assert
		Field phonesField = TestData.class.getDeclaredField("phones");
		Field wearableField = TestData.class.getDeclaredField("wearable");
		Field fileNameField = TestData.class.getDeclaredField("fileName");
		phonesField.setAccessible(true);
		wearableField.setAccessible(true);
		fileNameField.setAccessible(true);
		List<Device> actualPhones = (List<Device>) phonesField.get(actualTestData);
		List<Device> actualWearable = (List<Device>) wearableField.get(actualTestData);
		String actualFileName = (String) fileNameField.get(actualTestData);
		assertEquals(1, actualPhones.size());
		assertTrue(actualWearable.isEmpty());
		assertEquals(expectFileName, actualFileName);
	}
	
	@Test
	public void TestExecuteTest() {
		
	}
}
