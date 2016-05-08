package test.main.com.example;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import main.com.example.AndroidRobotframeworkExecutor;
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
	public void testTurnOffBluetooth() throws IOException, InterruptedException {
		File file = new File(CoreOptions.SCRIPT_DIR + "\\turnOffBluetooth.py");
		assertTrue(file.exists());
		this.testExecutor.turnOffBluetooth(new Device("FA369W910377", "htc", "nosdcard"));
	}
//
//	@Test
//	public void testTurnOnBluetooth() {
//	}
//
//	@Test
//	public void testClearWearGms() {
//	}
}
