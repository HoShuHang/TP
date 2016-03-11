package test.com.example.entity;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import main.com.example.entity.Device;

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
}
