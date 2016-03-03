package com.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class StreamConsumer extends Thread {
	InputStream is;
	OutputStream os;
	String type;
	List<String> output;

	StreamConsumer(InputStream is, String type) {
		this.is = is;
		this.type = type;
		this.output = new ArrayList<String>();
	}
	
	StreamConsumer(OutputStream os, String type){
		this.os = os;
		this.type = type;
		this.output = new ArrayList<String>();
	}

	public void run() {
		try {
			InputStreamReader isr = new InputStreamReader(is);
			BufferedReader br = new BufferedReader(isr);
			String line = null;
			while ((line = br.readLine()) != null){
				output.add(line);
//				System.out.println(type + ">" + line);
			}
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}
	
	public List<String> getOutput(){
		return this.output;
	}
}
