package main.com.example.utility;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class Utility {
	public static List<String> cmd(String... command) {
		List<String> lstResults = new ArrayList<String>();
		ProcessBuilder proc = new ProcessBuilder(command);
		try {
			Process p = proc.start();
			BufferedReader results = new BufferedReader(new InputStreamReader(p.getInputStream()));

			String line = "";
			while ((line = results.readLine()) != null) {
				lstResults.add(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return lstResults;
	}
}
