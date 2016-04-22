package main.com.example.entity;

import java.io.File;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestData {
	private List<Device> devices = null;
	private String fileName = null;
	private InputStream fileContent = null;

	public void setProject(String fileName, InputStream fileContent) {
		this.fileName = fileName;
		this.fileContent = fileContent;
	}

	public void setDevices(List<Device> devices) {
		this.devices = devices;
	}

	public Map<String, Object> toHashMap() {
		Map<String, Object> maps = new HashMap<String, Object>();
//		maps.put("mobile", value);
//		maps.put("wearable", value);
//		maps.put(key, value);
		return maps;
	}
}
