package test.main.com.example.entity;

import static org.junit.Assert.*;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import main.com.example.entity.Device;
import main.com.example.entity.Report;

public class ReportTest {
	private Report report;

	@Before
	public void setUp() {
		this.report = new Report();
	}

	@Test
	public void testSetPhone() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		Device phone = new Device("12345", "test", "nosdcard");
		this.report.setPhone(phone);
		Field phoneField = Report.class.getDeclaredField("phone");
		phoneField.setAccessible(true);
		Device actual = (Device) phoneField.get(this.report);
		assertEquals(phone, actual);
	}

	@Test
	public void testGetPhone()
			throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		Device phone = new Device("12345", "test", "nosdcard");
		Field phoneField = Report.class.getDeclaredField("phone");
		phoneField.setAccessible(true);
		phoneField.set(this.report, phone);
		assertEquals(phone, this.report.getPhone());
	}

	@Test
	public void testSetWatch()
			throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		Device wear = new Device("12345", "test", "nosdcard, wear");
		this.report.setWatch(wear);
		Field phoneField = Report.class.getDeclaredField("watch");
		phoneField.setAccessible(true);
		Device actual = (Device) phoneField.get(this.report);
		assertEquals(wear, actual);
	}

	@Test
	public void testGetWatch()
			throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		Device watch = new Device("12345", "test", "nosdcard");
		Field watchField = Report.class.getDeclaredField("watch");
		watchField.setAccessible(true);
		watchField.set(this.report, watch);
		assertEquals(watch, this.report.getWatch());
	}

	@Test
	public void testIsPassTestingFalse()
			throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		Field isPassTestingField = Report.class.getDeclaredField("isPassTesting");
		isPassTestingField.setAccessible(true);
		isPassTestingField.set(this.report, false);
		assertFalse(this.report.isPassTesting());
	}

	@Test
	public void testIsPassTestingTrue()
			throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		Field isPassTestingField = Report.class.getDeclaredField("isPassTesting");
		isPassTestingField.setAccessible(true);
		isPassTestingField.set(this.report, true);
		assertTrue(this.report.isPassTesting());
	}

	@Test
	public void testSetPassTestingFalse()
			throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		this.report.setPassTesting(false);
		Field isPassTestingField = Report.class.getDeclaredField("isPassTesting");
		isPassTestingField.setAccessible(true);
		boolean actual = (boolean) isPassTestingField.get(this.report);
		assertFalse(actual);
	}

	@Test
	public void testSetPassTestingTrue()
			throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		this.report.setPassTesting(true);
		Field isPassTestingField = Report.class.getDeclaredField("isPassTesting");
		isPassTestingField.setAccessible(true);
		boolean actual = (boolean) isPassTestingField.get(this.report);
		assertTrue(actual);
	}

	@Test
	public void testGetTestingMessage()
			throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		List<String> testingMessage = new ArrayList<String>();
		testingMessage.add("test");
		Field testingMessageField = Report.class.getDeclaredField("testingMessage");
		testingMessageField.setAccessible(true);
		testingMessageField.set(this.report, testingMessage);
		assertEquals(testingMessage, this.report.getTestingMessage());
	}

	@Test
	public void testSetTestingMessage()
			throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		List<String> testingMessage = new ArrayList<String>();
		testingMessage.add("test");
		this.report.setTestingMessage(testingMessage);
		Field testingMessageField = Report.class.getDeclaredField("testingMessage");
		testingMessageField.setAccessible(true);
		List<String> actual = (List<String>) testingMessageField.get(this.report);
		assertEquals(testingMessage, actual);
	}

	@Test
	public void testGetTotalTestCase() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		int totalTestCase = 5;
		Field totalTestCaseField = Report.class.getDeclaredField("totalTestCase");
		totalTestCaseField.setAccessible(true);
		totalTestCaseField.set(this.report, totalTestCase);
		assertEquals(totalTestCase, this.report.getTotalTestCase());
	}

	@Test
	public void testSetTotalTestCase() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		int totalTestCase = 5;
		this.report.setTotalTestCase(totalTestCase);
		Field totalTestCaseField = Report.class.getDeclaredField("totalTestCase");
		totalTestCaseField.setAccessible(true);
		int actual = (int) totalTestCaseField.get(this.report);
		assertEquals(totalTestCase, actual);
	}
	
	public void testGetPassTestCaseNumber() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		int passTestCaseNumber = 5;
		Field passTestCaseNumberField = Report.class.getDeclaredField("passTestCaseNumber");
		passTestCaseNumberField.setAccessible(true);
		passTestCaseNumberField.set(this.report, passTestCaseNumber);
		assertEquals(passTestCaseNumber, this.report.getPassTestCaseNumber());
	}

	public void testSetPassTestCaseNumber() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		int passTestCaseNumber = 5;
		this.report.setPassTestCaseNumber(passTestCaseNumber);
		Field passTestCaseNumberField = Report.class.getDeclaredField("passTestCaseNumber");
		passTestCaseNumberField.setAccessible(true);
		int actual = (int) passTestCaseNumberField.get(this.report);
		assertEquals(passTestCaseNumber, actual);
	}

	public void testGetFailTestCaseNumber() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		int failTestCaseNumber = 5;
		Field failTestCaseNumberField = Report.class.getDeclaredField("failTestCaseNumber");
		failTestCaseNumberField.setAccessible(true);
		failTestCaseNumberField.set(this.report, failTestCaseNumber);
		assertEquals(failTestCaseNumber, this.report.getPassTestCaseNumber());
	}

	public void testSetFailTestCaseNumber() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		int failTestCaseNumber = 5;
		this.report.setFailTestCaseNumber(failTestCaseNumber);
		Field failTestCaseNumberField = Report.class.getDeclaredField("failTestCaseNumber");
		failTestCaseNumberField.setAccessible(true);
		int actual = (int) failTestCaseNumberField.get(this.report);
		assertEquals(failTestCaseNumber, actual);
	}
}
