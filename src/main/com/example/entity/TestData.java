package main.com.example.entity;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import main.com.example.utility.CoreOptions;
import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;

public class TestData {
	private List<Device> phones, wearable = null;
	private String fileName = null;
	private InputStream fileContent = null;
	private Tool tool;
	private ZipFile project;

	public TestData() {
		this.phones = new ArrayList<Device>();
		this.wearable = new ArrayList<Device>();
	}

	public void setProject(String fileName, InputStream fileContent) throws ZipException {
		this.fileName = fileName;
		this.fileContent = fileContent;
		this.project = new ZipFile(CoreOptions.UPLOAD_DIRECTORY + File.separator + this.fileName);
	}

	public void setDevices(List<Device> devices) {
		for (Device d : devices) {
			if (d.isWearable())
				this.wearable.add(d);
			else
				this.phones.add(d);
		}
	}

	public void setTool(Tool tool) {
		this.tool = tool;
	}

	public Tool getTool() {
		return this.tool;
	}

	public Map<String, Object> toHashMap() {
		Map<String, Object> maps = new HashMap<String, Object>();
		// maps.put("mobile", value);
		// maps.put("wearable", value);
		// maps.put(key, value);
		return maps;
	}

	public List<Pair> getPairs() {
		List<Pair> pairs = new ArrayList<Pair>();
		for (Device phone : this.getPhones()) {
			for(Device wear : this.getWearable()){
				Pair pair = new Pair(phone, wear);
				pairs.add(pair);
			}
		}
		return pairs;
	}

	public List<Device> getPhones() {
		return this.phones;
	}

	public List<Device> getWearable() {
		return this.wearable;
	}

	public ZipFile getProject() {
		return this.project;
	}

	public String getProjectFullPath() {
		return CoreOptions.UPLOAD_DIRECTORY + File.separator + this.fileName;
	}
}
