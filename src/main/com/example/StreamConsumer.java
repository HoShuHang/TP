package main.com.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class StreamConsumer extends Thread {
	InputStream is;
//	OutputStream os;
	String action;
	List<String> output;

	public StreamConsumer(InputStream is, String action) {
		this.is = is;
		this.action = action;
		this.output = new ArrayList<String>();
	}
	
	public StreamConsumer(InputStream is){
		this.is = is;
		this.output = new ArrayList<String>();
	}

	public void run() {
		try {
			InputStreamReader isr = new InputStreamReader(is);
			BufferedReader br = new BufferedReader(isr);
			String line = null;
			while ((line = br.readLine()) != null){
				output.add(line);
				System.out.println("【" + action + "】"+line);
			}
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}
	
	public List<String> getOutput(){
		return this.output;
	}
}
