package main.com.example.entity;

import java.util.List;

public class Report {
	private Device phone;
	private Device watch;
	private boolean isPassTesting;
	private List<String> testingMessage;
	private int totalTestCase;
	private int passTestCaseNumber;
	private int failTestCaseNumber;

	public Device getPhone() {
		return phone;
	}

	public void setPhone(Device phone) {
		this.phone = phone;
	}

	public Device getWatch() {
		return watch;
	}

	public void setWatch(Device watch) {
		this.watch = watch;
	}

	public void setPair(Pair pair) {
		this.phone = pair.getPhone();
		this.watch = pair.getWear();
	}

	public boolean isPassTesting() {
		return isPassTesting;
	}

	public void setPassTesting(boolean isPassTesting) {
		this.isPassTesting = isPassTesting;
	}

	public List<String> getTestingMessage() {
		return testingMessage;
	}

	public void setTestingMessage(List<String> testingMessage) {
		this.testingMessage = testingMessage;
	}

	public int getTotalTestCase() {
		return totalTestCase;
	}

	public void setTotalTestCase(int totalTestCase) {
		this.totalTestCase = totalTestCase;
	}

	public int getPassTestCaseNumber() {
		return passTestCaseNumber;
	}

	public void setPassTestCaseNumber(int passTestCaseNumber) {
		this.passTestCaseNumber = passTestCaseNumber;
	}

	public int getFailTestCaseNumber() {
		return failTestCaseNumber;
	}

	public void setFailTestCaseNumber(int failTestCaseNumber) {
		this.failTestCaseNumber = failTestCaseNumber;
	}
}
