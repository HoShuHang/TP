package com.example.entity;

public class Device {
	private String serialNum;
	private String modelAlias;
	
	public Device(String serialNum, String modelAlias){
		this.serialNum = serialNum;
		this.modelAlias = modelAlias;
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
}
