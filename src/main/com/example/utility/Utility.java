package main.com.example.utility;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import main.com.example.StreamConsumer;

public class Utility {
	public static List<String> cmd(String... command) {
		ProcessBuilder proc = new ProcessBuilder(command);
		StreamConsumer outputConsumer = null;
		StreamConsumer errorConsumer = null;
		Process p = null;
		try {
			p = proc.start();
			outputConsumer = new StreamConsumer(p.getInputStream());
			errorConsumer = new StreamConsumer(p.getErrorStream());
			outputConsumer.start();
			errorConsumer.start();
			p.waitFor();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			p.destroy();
		}
		return outputConsumer.getOutput();
	}
}
