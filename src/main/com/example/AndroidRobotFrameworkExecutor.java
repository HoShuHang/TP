package main.com.example;

import java.io.File;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

public class AndroidRobotFrameworkExecutor {
	private String script = "D:\\Thesis\\RobotFrameworkWorkspace\\Evernote\\WearEvernoteTest.txt";
	private final String TEST_ARCHIVE_UNZIP = "testArchiveUnzip";
	/*public void showLog(){
		PropertyConfigurator.configure("../../Log4j.properties");
		Logger logger = Logger.getLogger(this.getClass());
		logger.info("Hi Log4j, this will appear in console and log file");
	}*/
	
	public void executeTest() {
		//launchApp("com.evernote", ".ui.HomeActivity");
		//checkTestScriptExtensionName();
		try {
			String executeFilePath = getRobotframeworkCommand();
			ProcessBuilder pb = new ProcessBuilder();
			Map<String, String> environment = pb.environment();
//			String entireCommand = executeFilePath + 
//			System.out.println(executeFilePath);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private String getRobotframeworkCommand() throws Exception {
		String PYTHON_HOME = System.getenv("PYTHON_HOME");
		String command = null;
		command = PYTHON_HOME + File.separatorChar + "Scripts"
				+ File.separatorChar + "pybot" + ".bat";
		return command;
	}
	
	
	
//	private void launchApp(String packageName, String activityName){
//		final String command = "am start -W -n " + packageName + "/" + activityName;
//		ADB adb = new ADB();
//		adb.shell(command);
//	}
	
	private void checkTestScriptExtensionName(){
		//
	}
}
