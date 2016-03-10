package com.example.entity;

public class Device {
	private String serialNum;
	private String modelAlias;
	private String characteristic;
	
	public Device(String serialNum, String modelAlias, String characteristic){
		this.serialNum = serialNum;
		this.modelAlias = modelAlias;
		this.characteristic = characteristic;
	}
	
	public String getSerialNum(){
		return this.serialNum;
	}
	
	public String getModelAlias(){
		return this.modelAlias;
	}
	
	public String getModelAliasWithDash(){
		return this.modelAlias.replace(" ", "_");
	}
	
	public boolean isWearable(){
		return this.characteristic.contains("watch");
	}
}
