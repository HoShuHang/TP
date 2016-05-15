package test.main.com.example;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.Before;
import org.junit.Test;

import main.com.example.DeviceController;
import main.com.example.entity.Device;
import main.com.example.utility.CoreOptions;
import main.com.example.utility.Utility;

public class DeviceControllerTest {
	private DeviceController deviceController = null;

	@Before
	public void setUp() {
		this.deviceController = new DeviceController();
	}

	@Test
	public void testGetSpecValue() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException,
			NoSuchMethodException, SecurityException {
		assertTrue(new File(CoreOptions.APK_INFO_GETTER_DIR).exists());
//		Class[] args = new Class[2];
//		args[0] = File.class;
//		args[1] = String.class;
//		Method method = DeviceController.class.getDeclaredMethod("getSpecValue", args);
//		method.setAccessible(true);
//		File file = new File(CoreOptions.UPLOAD_DIRECTORY + "test.apk");
//		final String TAG_APK_PACKAGE = "package";
//		String actual = (String) method.invoke(this.deviceController, file, TAG_APK_PACKAGE);
//		assertFalse(actual.contains("\\r\\n"));
	}

	@Test
	public void testTurnOffBluetooth() throws IOException, InterruptedException {
		File file = new File(CoreOptions.TURN_OFF_BLUETOOTH_DIR);
		assertTrue(file.exists());
//		this.deviceController.turnOffBluetooth(new Device("123456789", "htc", "nosdcard"));
	}

	@Test
	public void testTurnOnBluetooth() throws IOException, InterruptedException {
		File file = new File(CoreOptions.TURN_ON_BLUETOOTH_DIR);
		assertTrue(file.exists());
//		this.deviceController.turnOnBluetooth(new Device("HT53VWZ01028", "htc", "nosdcard"));
	}

	@Test
	public void testClearWearGms() throws IOException, InterruptedException {
		File file = new File(CoreOptions.CLEAR_GMS_DIR);
		assertTrue(file.exists());
		// this.deviceController.clearWearGms(new Device("123456789", "htc",
		// "nosdcard, watch"));
	}

	@Test
	public void testInstallApk() throws IOException, InterruptedException {
		File file = new File(CoreOptions.INSTALL_APK_DIR);
		assertTrue(file.exists());
		// this.deviceController.installApk(new Device("123456789", "htc",
		// "nosdcard, watch"), CoreOptions.UPLOAD_DIRECTORY + "/test.apk");
	}

	@Test
	public void testUninstallApk() {
		File file = new File(CoreOptions.UNINSTALL_APK_DIR);
		assertTrue(file.exists());
//		this.deviceController.uninstallApk(new Device("123456789", "htc", "nosdcard"), "test package");
	}
}
