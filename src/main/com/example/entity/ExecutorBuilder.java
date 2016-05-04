package main.com.example.entity;

import main.com.example.AndroidPythonUiautomatorExecutor;
import main.com.example.AndroidRobotframeworkExecutor;
import main.com.example.TestExecutor;
import test.com.example.entity.Tool;

public class ExecutorBuilder {
	public TestExecutor build(Tool tool){
		switch(tool){
		case UIAutomator:
			return new AndroidPythonUiautomatorExecutor();
		case RobotFramework:
			return new AndroidRobotframeworkExecutor();
		}
		return null;
	}
}
