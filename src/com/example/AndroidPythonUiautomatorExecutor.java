package com.example;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecuteResultHandler;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteWatchdog;
import org.apache.commons.exec.PumpStreamHandler;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class AndroidPythonUiautomatorExecutor {
	private final String PYTHON = "python.exe";
	private final String FILE_PROPERTIES_TXT = "properties.txt";
	private final String TAG_MOBILE = "mobile";
	private final String TAG_WEAR = "wear";
	private String testScriptLocation/* = "D:\\Thesis\\PythonWorkplace\\MobileEvernoteTest\\RunTest.py" */;
	private File script;
	private String parentPath;
	private HashMap<String, String> deviceNumber;
	private Log mLog;

	public AndroidPythonUiautomatorExecutor(String testScriptLocation, HashMap<String, String> deviceNumber) {
		this.deviceNumber = deviceNumber;
		this.testScriptLocation = testScriptLocation;
		script = new File(testScriptLocation);
		parentPath = script.getParent();
	}

	public AndroidPythonUiautomatorExecutor() {
		mLog = LogFactory.getLog(this.getClass());
	}

	public List<String> executeTest() {
		List<String> output = new ArrayList<String>();
		final String PYTHON_HOME = System.getenv("PYTHON_HOME");
		String entireCommand = PYTHON_HOME + File.separator + PYTHON + " " + testScriptLocation;
		ProcessBuilder proc = new ProcessBuilder(PYTHON_HOME + File.separator + PYTHON, testScriptLocation);
		try {
			Process p = proc.start();
//			Process p = rt.exec("ipconfig");
			System.out.println(entireCommand);
			StreamConsumer errorConsumer = new StreamConsumer(p.getErrorStream(), "error");

//			StreamConsumer inputConsumer = new StreamConsumer(p.getInputStream(), "input");
			
			errorConsumer.start();
//			inputConsumer.start();

			int exitVal = p.waitFor();
			output = errorConsumer.getOutput();
			System.out.println("ExitVal: " + exitVal);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return output;
	}

	//保留
//	public List<String> executeTest() {
//		final int DEFAULT_TIMEOUT = 120000;
//		String entireCommand = PYTHON + " " + testScriptLocation;
//		String cmd = "ipconfig";
//		List<String> output = null;
//		CommandLine cmdLine = CommandLine.parse(entireCommand);
//		ExecuteWatchdog watchDog = new ExecuteWatchdog(DEFAULT_TIMEOUT);
//		DefaultExecuteResultHandler resultHandler = new DefaultExecuteResultHandler();
//		ExecutorOutputStream outputStream = new ExecutorOutputStream();
//		PumpStreamHandler streamHandler = new PumpStreamHandler(outputStream);
//		try {
//
//			DefaultExecutor executor = new DefaultExecutor();
//			executor.setExitValue(1);
//			executor.setStreamHandler(streamHandler);
//			executor.setWatchdog(watchDog);
//
//			executor.setWorkingDirectory(new File(parentPath));
//
//			creatrePropertiesFile();
//			executor.execute(cmdLine, resultHandler);
//			resultHandler.waitFor();
//
//			output = outputStream.getOutput();
//		} catch (IOException e) {
//			e.printStackTrace();
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
//		return output;
//	}

	private void creatrePropertiesFile() throws IOException {
		File fProperties = new File(parentPath + File.separator + FILE_PROPERTIES_TXT);
		PrintWriter writer = new PrintWriter(fProperties, "UTF-8");
		writer.println(TAG_MOBILE + "," + deviceNumber.get(TAG_MOBILE));
		writer.print(TAG_WEAR + "," + deviceNumber.get(TAG_WEAR));
		writer.close();
	}
}
