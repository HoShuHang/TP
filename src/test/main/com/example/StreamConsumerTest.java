package test.main.com.example;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import main.com.example.StreamConsumer;

public class StreamConsumerTest {
	private StreamConsumer streamConsumer = null;
	private InputStream inputStream = null;
	private String expectString = "Hello";

	@Before
	public void setUp() {
		this.inputStream = new ByteArrayInputStream(this.expectString.getBytes(StandardCharsets.UTF_8));
		this.streamConsumer = new StreamConsumer(this.inputStream);
	}

	@Test
	public void testGetOutput()
			throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException {
		String test = "test";
		List<String> expect = new ArrayList<String>();
		expect.add(test);
		Field outputField = StreamConsumer.class.getDeclaredField("output");
		outputField.setAccessible(true);
		outputField.set(this.streamConsumer, expect);
		assertEquals(expect, this.streamConsumer.getOutput());
	}

	@Test
	public void testRun()
			throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		List<String> expect = new ArrayList<String>();
		expect.add(this.expectString);
		this.streamConsumer.run();
		Field outputField = StreamConsumer.class.getDeclaredField("output");
		outputField.setAccessible(true);
		List<String> actual = (List<String>) outputField.get(this.streamConsumer);
		assertEquals(expect, actual);
	}

	@Test
	public void testConstructor()
			throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		String type = "type";
		StreamConsumer streamConsumer = new StreamConsumer(this.inputStream, type);
		Field isField = StreamConsumer.class.getDeclaredField("is");
		Field typeField = StreamConsumer.class.getDeclaredField("action");
		isField.setAccessible(true);
		typeField.setAccessible(true);
		InputStream actualInputStream = (InputStream) isField.get(streamConsumer);
		String actualType = (String) typeField.get(streamConsumer);
		assertEquals(this.inputStream, actualInputStream);
		assertEquals(type, actualType);
	}
}
