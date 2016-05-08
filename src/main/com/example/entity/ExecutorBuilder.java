package main.com.example.entity;

import main.com.example.AndroidPythonUiautomatorExecutor;
import main.com.example.AndroidRobotframeworkExecutor;
import main.com.example.TestExecutor;

public class ExecutorBuilder {
	public TestExecutor build(Tool tool){
		if(tool == Tool.UIAutomator)
			return new AndroidPythonUiautomatorExecutor();
		else
			return new AndroidRobotframeworkExecutor();
	}
}
