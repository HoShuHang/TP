package test.main.com.example;

import static org.junit.Assert.*;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import main.com.example.ADB;
import main.com.example.entity.Device;

public class ADBTest {
	private Device device;
	private final String deviceSerialNum = "123456789";
	private final String deviceModelAlias = "HSS model alias";
	private final String deviceCharacteristic = "nosdcard";

	@Before
	public void setUp() {
		this.device = new Device(this.deviceSerialNum, this.deviceModelAlias, this.deviceCharacteristic);
	}

	@Test
	public void testGetDevicesWhenDevicesNotNull() throws NoSuchFieldException, SecurityException,
			IllegalArgumentException, IllegalAccessException, InterruptedException, IOException {
		List<Device> devices = new ArrayList<Device>();
		devices.add(this.device);
		Field devicesField;
		devicesField = ADB.class.getDeclaredField("devices");
		devicesField.setAccessible(true);
		devicesField.set(null, devices);
		assertTrue(ADB.getDevices().size() == 1);
	}

	@Test
	public void testGetDeviceProp() {
//		String deviceSerialNum = "123";
	}
}
