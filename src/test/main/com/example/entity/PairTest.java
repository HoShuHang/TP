package test.main.com.example.entity;

import static org.junit.Assert.*;

import java.lang.reflect.Field;

import org.junit.Before;
import org.junit.Test;

import main.com.example.entity.Device;
import main.com.example.entity.Pair;

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
	public void testSetTestCompleteFalse() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		boolean isComplete = false;
		this.pair.setTestComplete(isComplete);
		Field isCompleteField = Pair.class.getDeclaredField("testComplete");
		isCompleteField.setAccessible(true);
		boolean actual = (boolean) isCompleteField.get(this.pair);
		assertFalse(actual);
	}
	
	@Test
	public void testSetTestCompleteTrue() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		boolean isComplete = true;
		this.pair.setTestComplete(isComplete);
		Field isCompleteField = Pair.class.getDeclaredField("testComplete");
		isCompleteField.setAccessible(true);
		boolean actual = (boolean) isCompleteField.get(this.pair);
		assertTrue(actual);
	}

	@Test
	public void testIsCompleteFalse() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		Field isComplete = Pair.class.getDeclaredField("testComplete");
		isComplete.setAccessible(true);
		isComplete.set(this.pair, false);
		assertFalse(this.pair.isTestComplete());
	}
	
	@Test
	public void testIsCompleteTrue() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		Field isComplete = Pair.class.getDeclaredField("testComplete");
		isComplete.setAccessible(true);
		isComplete.set(this.pair, true);
		assertTrue(this.pair.isTestComplete());
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
