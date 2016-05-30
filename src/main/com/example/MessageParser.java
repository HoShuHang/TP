package main.com.example;

import java.util.List;

public interface MessageParser {
	public boolean isPassTesting(List<String> message);
	public List<String> getTestingMessage(List<String> message);
	public int getTotalTestCase(List<String> message);
	public int getPassTestCaseNumber(List<String> message);
	public int getFailTestCaseNumber(List<String> message);
}
