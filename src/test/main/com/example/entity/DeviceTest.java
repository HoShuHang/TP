package test.main.com.example.entity;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import main.com.example.entity.Device;
import main.com.example.utility.CoreOptions;

public class DeviceTest {
	private Device phone, wearable;
	private String phoneSerialNum, phoneModelAlias, phoneCharacteristic;
	private String wearableSerialNum, wearableModelAlias, wearableCharacteristic;

	@Before
	public void setUp() throws Exception {
		this.phoneSerialNum = "9LCAJNR8OFPBHQPF";
		this.phoneModelAlias = "Acer V370";
		this.phoneCharacteristic = "default";
		this.wearableSerialNum = "F2NZCY01033809E";
		this.wearableModelAlias = "ASUS ZenWatch";
		this.wearableCharacteristic = "nosdcard,watch";
		this.phone = new Device(this.phoneSerialNum, this.phoneModelAlias, this.phoneCharacteristic);
		this.wearable = new Device(this.wearableSerialNum, this.wearableModelAlias, this.wearableCharacteristic);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGetSerialNum() {
		assertEquals(this.phoneSerialNum, this.phone.getSerialNum());
		assertEquals(this.wearableSerialNum, this.wearable.getSerialNum());
	}
	
	@Test
	public void testGetModelAlias() {
		assertEquals(this.phoneModelAlias, this.phone.getModelAlias());
		assertEquals(this.wearableModelAlias, this.wearable.getModelAlias());
	}
	
	@Test
	public void testGetModelAliasWithDash() {
		assertEquals("Acer_V370", this.phone.getModelAliasWithDash());
		assertEquals("ASUS_ZenWatch", this.wearable.getModelAliasWithDash());
	}
	
	@Test
	public void testIsWearable() {
		assertFalse(this.phone.isWearable());
		assertTrue(this.wearable.isWearable());
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
