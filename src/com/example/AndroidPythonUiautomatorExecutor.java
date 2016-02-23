package com.example;

import java.io.IOException;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteException;



//import org.apache.commons.exec.CommandLine;
//import org.apache.commons.exec.DefaultExecutor;
//import org.apache.commons.exec.ExecuteException;

public class AndroidPythonUiautomatorExecutor {
	final String PYTHON = "python";
	final String TEST_SCRIPT_LOCATION = "C:\\Users\\User\\PycharmProjects\\MobileEvernoteTest\\RunTest.py";
	public void executeTest(){
		String entireCommand = PYTHON + " " + TEST_SCRIPT_LOCATION;
		System.out.println(entireCommand);
		
		CommandLine commandLine = CommandLine.parse(entireCommand);
		DefaultExecutor executor = new DefaultExecutor();
		try {
			int exitValue = executor.execute(commandLine);
		} catch (ExecuteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
//		CommandLine cmdLine = CommandLine.parse(entireCommand);
//		CommandLine cmdLine = new CommandLine(PYTHON);
		//cmdLine.addArgument(TEST_SCRIPT_LOCATION);
		//System.out.println(cmdLine.getExecutable());
		
//		CommandLine cmdLine = CommandLine.parse(entireCommand);
//		DefaultExecutor executor = new DefaultExecutor();
//		int exitValue;
//		try {
//			exitValue = executor.execute(cmdLine);
//			System.out.println(exitValue);
//		} catch (ExecuteException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
//		ProcessBuilder proc = new ProcessBuilder(entireCommand);
//		try {
////			Process p = proc.start();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}
	
	private String executeFile(){
		return System.getenv("PYTHON_HOME") + "\\" + PYTHON;
	}
}
