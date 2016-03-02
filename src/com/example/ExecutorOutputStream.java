package com.example;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.exec.LogOutputStream;

public class ExecutorOutputStream extends LogOutputStream {

	List<String> output;
	public ExecutorOutputStream() {
		// TODO Auto-generated constructor stub
		output = new ArrayList<String>();
	}
	@Override
	protected void processLine(String line, int logLevel) {
		// TODO Auto-generated method stub
		output.add(line);
		System.out.println(line);
	}
	
	public List<String> getOutput(){
		return this.output;
	}

}
