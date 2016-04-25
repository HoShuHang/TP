package main.com.example.entity;

import main.com.example.AndroidPythonUiautomatorExecutor;
import main.com.example.AndroidRobotframeworkExecutor;
import main.com.example.Executor;
import test.com.example.entity.Tool;

public class ExecutorBuilder {
	public Executor build(Tool tool){
		switch(tool){
		case UIAutomator:
			return new AndroidPythonUiautomatorExecutor();
		case RobotFramework:
			return new AndroidRobotframeworkExecutor();
		}
		return null;
	}
}
