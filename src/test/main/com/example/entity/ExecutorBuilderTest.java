package test.main.com.example.entity;

import static org.junit.Assert.*;

import org.junit.Test;

import main.com.example.AndroidPythonUiautomatorExecutor;
import main.com.example.AndroidRobotframeworkExecutor;
import main.com.example.TestExecutor;
import main.com.example.entity.ExecutorBuilder;
import main.com.example.entity.Tool;

public class ExecutorBuilderTest {
	@Test
	public void testBuildUIAutomator() {
		ExecutorBuilder builder = new ExecutorBuilder();
		TestExecutor actual = builder.build(Tool.UIAutomator);
		assertEquals(AndroidPythonUiautomatorExecutor.class, actual.getClass());
	}
	
	@Test
	public void testBuildRobotFramework() {
		ExecutorBuilder builder = new ExecutorBuilder();
		TestExecutor actual = builder.build(Tool.RobotFramework);
		assertEquals(AndroidRobotframeworkExecutor.class, actual.getClass());
	}
	
	@Test
	public void testBuildBothNot() {
		ExecutorBuilder builder = new ExecutorBuilder();
		TestExecutor actual = builder.build(Tool.RobotFramework);
		assertEquals(AndroidRobotframeworkExecutor.class, actual.getClass());
	}
}
