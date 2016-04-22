package main.com.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import main.com.example.entity.TestData;

public abstract class Executor {
	public abstract List<String> executeTest(TestData testData) throws IOException, InterruptedException;
}
