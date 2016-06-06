package test.main.com.example.utility;

import static org.junit.Assert.*;

import java.io.IOException;
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
	public void testCmd() throws IOException, InterruptedException {
		List<String> result = Utility.cmd("testCmd",CoreOptions.ADB, "devices");
		assertTrue(result.get(0).equals("List of devices attached"));
	}
	
//	@Test
//	public void testTest() {
//		Utility.cmd("python", "/Users/kevinho/project/TPUploadSpace/main/RunTest.py", "FA369W910377", "510KPNY0322975");
//		//		Utility.cmd("python", "/Users/kevinho/project/TPUploadSpace/main/RunTest.py", "FA369W910377", "510KPNY0322975");
//	}
}
