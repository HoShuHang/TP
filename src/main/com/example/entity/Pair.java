package main.com.example.entity;

public class Pair {
	private boolean testComplete = false;
	private Device phone, wear;
	private Report report = null;

	public Pair(Device phone, Device wear) {
		this.phone = phone;
		this.wear = wear;
	}

	public void setTestComplete(boolean complete) {
		this.testComplete = complete;
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

	public void setReport(Report report) {
		this.report = report;
	}

	public Report getReport() {
		if(this.report == null)
			throw new NullPointerException("return value is null at method getReport");
		return this.report;
	}
}
