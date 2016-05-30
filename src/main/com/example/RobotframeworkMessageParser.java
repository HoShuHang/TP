package main.com.example;

import java.util.ArrayList;
import java.util.List;

public class RobotframeworkMessageParser implements MessageParser{

	@Override
	public boolean isPassTesting(List<String> message) {
		String testResult = message.get(message.size()-7);
		if(testResult.contains("PASS"))
			return true;
		return false;
	}

	@Override
	public List<String> getTestingMessage(List<String> message) {
		List<String> testMessage = new ArrayList<String>();
		for(int i=3 ; i<message.size()-8 ; i++){
			String line = message.get(i);
			testMessage.add(line);
		}
		return testMessage;
	}

	@Override
	public int getTotalTestCase(List<String> message) {
		String line = message.get(message.size()-5);
		String[] tokens = line.split("\\s");
		return Integer.parseInt(tokens[0]);
	}

	@Override
	public int getPassTestCaseNumber(List<String> message) {
		String line = message.get(message.size()-5);
		String[] tokens = line.split("\\s");
		System.out.println(line);
		System.out.println(tokens[3]);
		return Integer.parseInt(tokens[3]);
	}

	@Override
	public int getFailTestCaseNumber(List<String> message) {
		String line = message.get(message.size()-5);
		String[] tokens = line.split("\\s");
		return Integer.parseInt(tokens[5]);
	}

}
