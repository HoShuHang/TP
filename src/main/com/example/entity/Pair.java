package main.com.example.entity;

public class Pair {
	private Device phone, wear;
	private Report report = null;
	private TestStatus status = TestStatus.Waiting;

	public Pair(Device phone, Device wear) {
		this.phone = phone;
		this.wear = wear;
	}

	public TestStatus getTestStatus() {
		return this.status;
	}

	public void setTestStatus(TestStatus status) {
		this.status = status;
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
		if (this.report == null)
			throw new NullPointerException("return value is null at method getReport");
		return this.report;
	}
}
