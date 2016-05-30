package test.main.com.example.entity;

import static org.junit.Assert.*;

import java.lang.reflect.Field;

import org.junit.Before;
import org.junit.Test;

import main.com.example.entity.Device;
import main.com.example.entity.Pair;

public class PairTest {
	private Pair pairTest;
	private Device phone, wear;
	
	@Before
	public void setUp() {
		this.phone = new Device("12345", "test", "nosdcard");
		this.wear = new Device("54321", "test", "nosdcard, wear");
		this.pairTest = new Pair(phone, wear);
	}

	@Test
	public void testIsCompleteFalse() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		Field isComplete = Pair.class.getDeclaredField("testComplete");
		isComplete.setAccessible(true);
		isComplete.set(this.pairTest, false);
		assertFalse(this.pairTest.isTestComplete());
	}
	
	@Test
	public void testIsCompleteTrue() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		Field isComplete = Pair.class.getDeclaredField("testComplete");
		isComplete.setAccessible(true);
		isComplete.set(this.pairTest, true);
		assertTrue(this.pairTest.isTestComplete());
	}
	
	@Test
	public void testGetPhone() {
		assertEquals(this.phone, this.pairTest.getPhone());
	}
	
	@Test
	public void testGetWear() {
		assertEquals(this.wear, this.pairTest.getWear());
	}
}
