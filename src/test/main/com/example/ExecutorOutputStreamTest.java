package test.main.com.example;

import static org.junit.Assert.*;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.junit.Before;
import org.junit.Test;

import main.com.example.ExecutorOutputStream;

public class ExecutorOutputStreamTest {
	private ExecutorOutputStream executorOutputStream = null;
	private Field outputField = null;

	@Before
	public void setUP() throws NoSuchFieldException, SecurityException {
		this.executorOutputStream = new ExecutorOutputStream();
		this.outputField = ExecutorOutputStream.class.getDeclaredField("output");
		this.outputField.setAccessible(true);
	}

	@Test
	public void testConstructor()
			throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException {
		List<String> actual = (List<String>) outputField.get(this.executorOutputStream);
		assertNotNull(actual);
	}

	@Test
	public void testGetOutput()
			throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException {
		List<String> expect = new ArrayList<String>();
		expect.add("test");
		this.outputField.set(this.executorOutputStream, expect);
		assertEquals(expect, this.executorOutputStream.getOutput());
	}

	@Test
	public void testProcessLine() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		Class[] cArg = new Class[2];
		cArg[0] = String.class;
		cArg[1] = int.class;
		Method method = ExecutorOutputStream.class.getDeclaredMethod("processLine", cArg);
		method.setAccessible(true);
		String expectString = "test";
		int expectInt = 1;
		method.invoke(this.executorOutputStream, expectString, expectInt);
		List<String> actual = (List<String>) outputField.get(this.executorOutputStream);
		assertEquals(1, actual.size());
		assertEquals(expectString, actual.get(0));
	}
}
