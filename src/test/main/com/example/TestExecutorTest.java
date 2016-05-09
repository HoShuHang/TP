package test.main.com.example;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import main.com.example.AndroidRobotframeworkExecutor;
import main.com.example.DeviceController;
import main.com.example.TestExecutor;
import main.com.example.entity.Device;
import main.com.example.utility.CoreOptions;

public class TestExecutorTest {
	private TestExecutor testExecutor;

	@Before
	public void setUp() {
		this.testExecutor = new AndroidRobotframeworkExecutor();
	}

	@Test
	public void testGetApkInDir() throws NoSuchMethodException, SecurityException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException {
		String dir = CoreOptions.UPLOAD_DIRECTORY;
		File[] actual = this.testExecutor.getApkFileInDir(dir);
		for (File f : actual) {
			assertTrue(f.getName().contains("apk"));
		}
	}
}
