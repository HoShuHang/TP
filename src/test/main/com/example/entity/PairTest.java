package test.main.com.example.entity;

import static org.junit.Assert.*;

import java.lang.reflect.Field;

import org.junit.Before;
import org.junit.Test;

import main.com.example.entity.Device;
import main.com.example.entity.Pair;
import main.com.example.entity.TestStatus;

public class PairTest {
	private Pair pair;
	private Device phone, wear;

	@Before
	public void setUp() {
		this.phone = new Device("12345", "test", "nosdcard");
		this.wear = new Device("54321", "test", "nosdcard, wear");
		this.pair = new Pair(phone, wear);
	}

	@Test
	public void testSetTestStatusWaiting() throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException {
		TestStatus expect = TestStatus.Waiting;
		this.pair.setTestStatus(expect);
		Field field = Pair.class.getDeclaredField("status");
		field.setAccessible(true);
		TestStatus actual = (TestStatus) field.get(this.pair);
		assertEquals(expect, actual);
	}

	@Test
	public void testSetTestStatusTesting() throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException {
		TestStatus expect = TestStatus.Testing;
		this.pair.setTestStatus(expect);
		Field field = Pair.class.getDeclaredField("status");
		field.setAccessible(true);
		TestStatus actual = (TestStatus) field.get(this.pair);
		assertEquals(expect, actual);
	}

	@Test
	public void testSetTestStatusComplete() throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException {
		TestStatus expect = TestStatus.Complete;
		this.pair.setTestStatus(expect);
		Field field = Pair.class.getDeclaredField("status");
		field.setAccessible(true);
		TestStatus actual = (TestStatus) field.get(this.pair);
		assertEquals(expect, actual);
	}

	@Test
	public void testGetPhone() {
		assertEquals(this.phone, this.pair.getPhone());
	}

	@Test
	public void testGetWear() {
		assertEquals(this.wear, this.pair.getWear());
	}
}
