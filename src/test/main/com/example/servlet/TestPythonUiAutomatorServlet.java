package test.main.com.example.servlet;

import javax.servlet.http.HttpServletRequest;

import org.easymock.EasyMockSupport;
import org.junit.Before;
import org.junit.Test;

import main.com.example.entity.Tool;
import main.com.example.servlet.PythonUiAutomatorServlet;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class TestPythonUiAutomatorServlet{
	private PythonUiAutomatorServlet servlet = null;
	private HttpServletRequest mockReq = null;
	
	@Before
	public void setUp() {
		this.servlet = new PythonUiAutomatorServlet();
		this.mockReq = createMock(HttpServletRequest.class);
	}

	@Test
	public void testGetToolWhenRequestUiAutomator() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
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
	public void testGetTOllWhenRequestRobotFramework() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException{
		expect(this.mockReq.getParameter("tool")).andReturn("robotframework");
		replay(this.mockReq);
		Class[] cArg = new Class[1];
        cArg[0] = HttpServletRequest.class;
		Method method = PythonUiAutomatorServlet.class.getDeclaredMethod("parseTool", cArg);
		method.setAccessible(true);
		Tool tool = (Tool) method.invoke(this.servlet, this.mockReq);
		assertTrue(tool.equals(Tool.RobotFramework));
	}
}
