package main.com.example.utility;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import main.com.example.StreamConsumer;

public class Utility {
	public static List<String> cmd(String... command) {
		List<String> lstResults = new ArrayList<String>();
		ProcessBuilder proc = new ProcessBuilder(command);
		StreamConsumer outputConsumer = null;
		StreamConsumer errorConsumer = null;
		try {
			Process p = proc.start();
			outputConsumer = new StreamConsumer(p.getInputStream());
			errorConsumer = new StreamConsumer(p.getErrorStream());
			outputConsumer.start();
			errorConsumer.start();
			p.waitFor();
//			BufferedReader results = new BufferedReader(new InputStreamReader(p.getInputStream()));
//
//			String line = "";
//			while ((line = results.readLine()) != null) {
//				lstResults.add(line);
//			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return outputConsumer.getOutput();
	}
}
