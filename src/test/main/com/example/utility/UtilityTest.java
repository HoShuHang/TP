package test.main.com.example.utility;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import main.com.example.entity.Device;
import main.com.example.utility.CoreOptions;
import main.com.example.utility.Utility;

public class UtilityTest {
	@Before
	public void setUp() throws Exception {
	}
	
	@Test
	public void testCmd() {
		List<String> result = Utility.cmd(CoreOptions.ADB, "devices");
		assertTrue(result.get(0).equals("List of devices attached"));
	}
}