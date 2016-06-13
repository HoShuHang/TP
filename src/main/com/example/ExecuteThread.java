package main.com.example;

import java.io.IOException;
import java.util.List;

import main.com.example.entity.ExecutorBuilder;
import main.com.example.entity.Report;
import main.com.example.entity.TestData;
import net.lingala.zip4j.exception.ZipException;

public class ExecuteThread implements Runnable {
	private TestData testData = null;
	
	public ExecuteThread(TestData testData) {
		this.testData = testData;
	}

	@Override
	public void run() {
		ExecutorBuilder builder = new ExecutorBuilder();
		TestExecutor executor = builder.build(this.testData.getTool());
		try {
			this.executeTest(executor, this.testData);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ZipException e) {
			e.printStackTrace();
		}
	}
	
	private void executeTest(TestExecutor executor, TestData testData)
			throws IOException, InterruptedException, ZipException {
		executor.execute(testData);
	}
}
