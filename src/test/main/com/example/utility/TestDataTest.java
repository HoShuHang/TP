package test.main.com.example.utility;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.lang.reflect.Field;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import main.com.example.entity.TestData;
import main.com.example.utility.CoreOptions;
import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.FileHeader;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.util.Zip4jConstants;

import main.com.example.entity.*;

public class TestDataTest {
	private final String expectFileName = "test";
	private InputStream inputStream = null;
	private File folder = null;
	private File testFile = null;
	private ZipFile zip = null;
	private TestData testData = null;

	@Before
	public void setUp() throws ZipException, FileNotFoundException {
		this.testData = new TestData();
		File f = new File(CoreOptions.UPLOAD_DIRECTORY + File.separator + this.expectFileName + ".zip");
		f.delete();
	}

	@Test
	public void testSetProect() throws ZipException, NoSuchFieldException, SecurityException, IllegalArgumentException,
			IllegalAccessException, FileNotFoundException {
		this.createMockZipFile();
		this.testData.setProject(this.expectFileName, this.inputStream);
		// assert
		Field fileNameField = TestData.class.getDeclaredField("fileName");
		Field fileContentField = TestData.class.getDeclaredField("fileContent");
		fileNameField.setAccessible(true);
		fileContentField.setAccessible(true);
		String actualFileName = (String) fileNameField.get(this.testData);
		InputStream actualInputStream = (InputStream) fileContentField.get(this.testData);
		assertEquals(this.expectFileName, actualFileName);
		assertEquals(this.inputStream, actualInputStream);
		this.folder.delete();
	}

	@Test
	public void testSetDevice()
			throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		List<Device> devices = new ArrayList<Device>();
		Device phone = new Device("123", "HTC", "nosdcard");
		Device wearable = new Device("456", "Asus", "nosdcard, watch");
		devices.add(phone);
		devices.add(wearable);
		this.testData.setDevices(devices);
		// assert
		Field phonesField = TestData.class.getDeclaredField("phones");
		Field wearableField = TestData.class.getDeclaredField("wearable");
		phonesField.setAccessible(true);
		wearableField.setAccessible(true);
		List<Device> actualPhones = (List<Device>) phonesField.get(this.testData);
		List<Device> actualWearable = (List<Device>) wearableField.get(this.testData);
		assertFalse(actualPhones.isEmpty());
		assertFalse(actualWearable.isEmpty());
		assertEquals(phone, actualPhones.get(0));
		assertEquals(wearable, actualWearable.get(0));
	}

	@Test
	public void testSetTool()
			throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException {
		Tool tool = Tool.UIAutomator;
		this.testData.setTool(tool);
		// assert
		Field toolField = TestData.class.getDeclaredField("tool");
		toolField.setAccessible(true);
		Tool actual = (Tool) toolField.get(this.testData);
		assertEquals(tool, actual);
	}

	@Test
	public void testGetTool()
			throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		Field toolField = TestData.class.getDeclaredField("tool");
		toolField.setAccessible(true);
		Tool expect = Tool.RobotFramework;
		toolField.set(this.testData, expect);
		assertEquals(expect, this.testData.getTool());
	}

	@Test
	public void testGetPhones()
			throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		List<Device> phones = new ArrayList<Device>();
		Device device = new Device("123", "Samsung", "nosdcard");
		phones.add(device);
		Field phonesField = TestData.class.getDeclaredField("phones");
		phonesField.setAccessible(true);
		phonesField.set(this.testData, phones);
		assertEquals(phones, this.testData.getPhones());
	}

	@Test
	public void testGetWearable()
			throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException {
		List<Device> wearable = new ArrayList<Device>();
		Device device = new Device("123", "Sony", "nosdcard, watch");
		wearable.add(device);
		Field phonesField = TestData.class.getDeclaredField("wearable");
		phonesField.setAccessible(true);
		phonesField.set(this.testData, wearable);
		assertEquals(wearable, this.testData.getWearable());
	}
	
	@Test
	public void testGetProjectFullPath()
			throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException{
		Field fileName = TestData.class.getDeclaredField("fileName");
		fileName.setAccessible(true);
		fileName.set(this.testData, this.expectFileName);
		String expect = CoreOptions.UPLOAD_DIRECTORY + File.separator + this.expectFileName;
		assertEquals(expect, this.testData.getProjectFullPath());
	}

	private void createMockZipFile() throws ZipException, FileNotFoundException {
		this.folder = new File(CoreOptions.UPLOAD_DIRECTORY + File.separator + this.expectFileName);
		this.folder.mkdir();
		this.testFile = new File(CoreOptions.UPLOAD_DIRECTORY + File.separator + this.expectFileName + ".zip");
		this.zip = new ZipFile(this.testFile);
		ZipParameters parameter = new ZipParameters();
		parameter.setCompressionMethod(Zip4jConstants.COMP_DEFLATE);
		parameter.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_NORMAL);
		this.zip.addFile(this.folder, parameter);
		this.inputStream = new FileInputStream(this.testFile);
	}
	
	@After
	public void tearDown() throws ZipException {
	}
}
