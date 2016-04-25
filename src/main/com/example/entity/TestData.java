package main.com.example.entity;

import java.io.File;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import test.com.example.entity.Tool;

public class TestData {
	private List<Device> devices = null;
	private String fileName = null;
	private InputStream fileContent = null;
	private Tool tool;

	public void setProject(String fileName, InputStream fileContent) {
		this.fileName = fileName;
		this.fileContent = fileContent;
	}

	public void setDevices(List<Device> devices) {
		this.devices = devices;
	}

	public void setTool(Tool tool) {
		this.tool = tool;
	}
	
	public Tool getTool(){
		return this.tool;
	}

	public Map<String, Object> toHashMap() {
		Map<String, Object> maps = new HashMap<String, Object>();
		// maps.put("mobile", value);
		// maps.put("wearable", value);
		// maps.put(key, value);
		return maps;
	}
}
