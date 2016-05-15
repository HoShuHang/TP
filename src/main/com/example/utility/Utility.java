package main.com.example.utility;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import main.com.example.StreamConsumer;

public class Utility {
	public static List<String> cmd(String action, String... command) {
		ProcessBuilder proc = new ProcessBuilder(command);
		Map<String, String> env = proc.environment();
		proc.environment().put("ANDROID_HOME", CoreOptions.ANDROID_HOME);
		StreamConsumer outputConsumer = null;
		StreamConsumer errorConsumer = null;
		Process p = null;
		try {
			p = proc.start();
			outputConsumer = new StreamConsumer(p.getInputStream(), action);
			errorConsumer = new StreamConsumer(p.getErrorStream(), action);
			outputConsumer.start();
			errorConsumer.start();
			p.waitFor();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			p.destroy();
		}
		return outputConsumer.getOutput();
	}
	

	public static List<String> readFile(File file) throws IOException {
		List<String> content = new ArrayList<String>();
		if (file.exists()) {
			FileReader fr = new FileReader(file);
			BufferedReader br = new BufferedReader(fr);
			String line = "";
			while ((line = br.readLine()) != null) {
				content.add(line);
			}
		} else {
			System.out.println("no file");
		}
		return content;
	}
	
	public static void writeFile(File file, List<String> content) {
		FileWriter fw;
		try {
			fw = new FileWriter(file);
			for (String line : content) {
				fw.write(line);
				fw.write("\n");
			}
			fw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static boolean isContain(List<String> text, String word){
		for(String line : text){
			if(text.contains(word)){
				return true;
			}
		}
		return false;
	}
}
