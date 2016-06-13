package test.main.com.example;

import static org.junit.Assert.*;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import main.com.example.ADB;
import main.com.example.TestPlatform;
import main.com.example.entity.Device;

public class TestPlatformTest {
	
	@Before
	public void setUp() throws InterruptedException {
	}

	@Test
	public void testGetDevices() throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException, InterruptedException, IOException {
		// set devices to ADB
		List<Device> devices = new ArrayList<Device>();
		Device expectDeviceA = new Device("123","HTC","nosdcard");
		Device expectDeviceB = new Device("456","SONY","nosdcard, watch");
		devices.add(expectDeviceA);
		devices.add(expectDeviceB);
		Field field = ADB.class.getDeclaredField("devices");
		field.setAccessible(true);
		field.set(null, devices);
		
		TestPlatform testPlatform = new TestPlatform();
		
		// assert
		Field devicesField = TestPlatform.class.getDeclaredField("devices");
		devicesField.setAccessible(true);
		List<Device> actualDevice = (List<Device>) devicesField.get(testPlatform);
		assertFalse(actualDevice.isEmpty());
		assertEquals(2, actualDevice.size());
		assertEquals(expectDeviceA, actualDevice.get(0));
		assertEquals(expectDeviceB, actualDevice.get(1));
	}
	
	@Test
	public void testGetPhones() throws NoSuchFieldException, SecurityException, InterruptedException, IllegalArgumentException, IllegalAccessException, IOException {
		List<Device> expectPhones = new ArrayList<Device>();
		Device expect = new Device("123", "Asus", "nosdcard");
		expectPhones.add(expect);
		Field wearableField = TestPlatform.class.getDeclaredField("phones");
		wearableField.setAccessible(true);
		TestPlatform testPlatform = new TestPlatform();
		wearableField.set(testPlatform, expectPhones);
		// assert
		assertEquals(expectPhones, testPlatform.getPhones());
	}
	
	@Test
	public void testGetWearable() throws NoSuchFieldException, SecurityException, InterruptedException, IllegalArgumentException, IllegalAccessException, IOException {
		List<Device> expectWearable = new ArrayList<Device>();
		Device expect = new Device("123", "Asus", "nosdcard, watch");
		expectWearable.add(expect);
		Field wearableField = TestPlatform.class.getDeclaredField("wearable");
		wearableField.setAccessible(true);
		TestPlatform testPlatform = new TestPlatform();
		wearableField.set(testPlatform, expectWearable);
		// assert
		assertEquals(expectWearable, testPlatform.getWearable());
	}
}
