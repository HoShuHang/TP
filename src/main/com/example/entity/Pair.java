package main.com.example.entity;

public class Pair {
	private boolean testComplete = false;
	private Device phone, wear;
	
	public Pair(Device phone, Device wear){
		this.phone = phone;
		this.wear = wear;
	}

	public boolean isTestComplete() {
		return this.testComplete;
	}
	
	public Device getPhone() {
		return this.phone;
	}
	
	public Device getWear() {
		return this.wear;
	}
}
