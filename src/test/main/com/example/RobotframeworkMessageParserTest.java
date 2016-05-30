package test.main.com.example;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import main.com.example.MessageParser;
import main.com.example.RobotframeworkMessageParser;
import main.com.example.utility.Utility;

public class RobotframeworkMessageParserTest {
	List<String> passMessage;
	List<String> failMessage;
	MessageParser parser;
	@Before
	public void setUp() throws IOException {
		passMessage = Utility.readFile(new File("D:\\message.txt"));
		failMessage = Utility.readFile(new File("D:\\fail_message.txt"));
		parser = new RobotframeworkMessageParser();
	}
	
	@Test
	public void testIsPassTestingWithPassMessage(){
		assertTrue(parser.isPassTesting(passMessage));
	}
	
	@Test
	public void testIsPassTestingWithFailMessage(){
		assertTrue(parser.isPassTesting(failMessage)==false);
	}
	
	@Test
	public void testGetTestingMessageWithPassMessage(){
		List<String> message = parser.getTestingMessage(passMessage);
		for(int i=3, j=0 ; i<passMessage.size()-8 && j<message.size() ; i++, j++){
			if(!passMessage.get(i).equals(message.get(j)))
				assertTrue(false);
		}
		assertTrue(true);
	}
	
	@Test
	public void testGetTestingMessageWithFailMessage(){
		List<String> message = parser.getTestingMessage(failMessage);
		for(int i=3, j=0 ; i<failMessage.size()-8 && j<message.size() ; i++, j++){
			if(!failMessage.get(i).equals(message.get(j)))
				assertTrue(false);
		}
		assertTrue(true);
	}
	
	@Test
	public void testGetTotalMessageWithPassMessage(){
		int totalTestCase = parser.getTotalTestCase(passMessage);
		assertTrue(totalTestCase == 1);
	}
	
	@Test
	public void testGetPassTestCaseNumberWithPassMessage(){
		int passTestCaseNumber = parser.getPassTestCaseNumber(passMessage);
		assertTrue(passTestCaseNumber == 1);
	}
	
	@Test
	public void testGetPassTestCaseNumberWithFailMessage(){
		int passTestCaseNumber = parser.getPassTestCaseNumber(failMessage);
		assertTrue(passTestCaseNumber == 0);
	}
	
	@Test
	public void testGetFailTestCaseNumberWithPassMessage(){
		int failTestCaseNumber = parser.getFailTestCaseNumber(passMessage);
		assertTrue(failTestCaseNumber == 0);
	}
	
	@Test
	public void testGetFailTestCaseNumberWithFailMessage(){
		int failTestCaseNumber = parser.getFailTestCaseNumber(failMessage);
		assertTrue(failTestCaseNumber == 1);
	}
}
