package main.com.example.entity;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import main.com.example.utility.CoreOptions;
import main.com.example.utility.Utility;

public class Device {
	private String serialNum;
	private String modelAlias;
	private String characteristic;
	private String image;

	public Device(String serialNum, String modelAlias, String characteristic) {
		this.serialNum = serialNum;
		this.modelAlias = modelAlias;
		this.characteristic = characteristic;
	}

	public String getSerialNum() {
		return this.serialNum;
	}

	public String getModelAlias() {
		return this.modelAlias;
	}

	public String getModelAliasWithDash() {
		return this.modelAlias.replace(" ", "_");
	}

	public String getImage() {
		Map<String, String> imageMaps = new HashMap<String, String>();
		imageMaps.put("HT53VWZ01028", "img/htc_butterfly2_exec.png");
		imageMaps.put("510KPNY0322975", "img/lg_urbane2.png");
		imageMaps.put("CB5A259ZSX", "img/sony_z3_exec.png");
		imageMaps.put("F2NZCY01033809E", "img/zenwatch2.png");
		imageMaps.put("FA369W910377", "img/one_m8_exec.png");
		if(imageMaps.get(this.getSerialNum())!=null)
			return imageMaps.get(this.getSerialNum());
		return "http://placehold.it/200x200";
	}

	public boolean isWearable() {
		return this.characteristic.contains("watch");
	}

	public void turnOffBluetooth() throws IOException, InterruptedException {
		System.out.println("【turnOffBluetooth】 " + this.getSerialNum());
		Utility.cmd("turnOffBluetooth", CoreOptions.PYTHON, CoreOptions.TURN_OFF_BLUETOOTH_DIR, this.getSerialNum());
	}

	public void turnOnBluetooth() throws IOException, InterruptedException {
		System.out.println("【turnOnBluetooth】 " + this.getSerialNum());
		Utility.cmd("turnOnBluetooth", CoreOptions.PYTHON, CoreOptions.TURN_ON_BLUETOOTH_DIR, this.getSerialNum());
	}

	public void clearWearGms() throws IOException, InterruptedException {
		System.out.println("【clearWearGms】 " + this.getSerialNum());
		Utility.cmd("clearWearGms", CoreOptions.PYTHON, CoreOptions.CLEAR_GMS_DIR, this.getSerialNum());
	}

	public void installApk(String apkPath) throws IOException, InterruptedException {
		System.out.println("【installApk】" + this.getSerialNum());
		Utility.cmd("installApk", CoreOptions.PYTHON, CoreOptions.INSTALL_APK_DIR, this.getSerialNum(), apkPath);
	}

	public void uninstallApk(String packageName) throws IOException, InterruptedException {
		System.out.println("【uninstallWearApk】【" + this.getSerialNum() + "】【" + packageName + "】");
		Utility.cmd("uninstallApk", CoreOptions.PYTHON, CoreOptions.UNINSTALL_APK_DIR, this.getSerialNum(),
				packageName);
	}

	public void launchApp(String packageName, String mainActivity) throws IOException, InterruptedException {
		System.out.println("【launchApp】");
		Utility.cmd("launch", CoreOptions.PYTHON, CoreOptions.LAUNCH_APK_DIR, this.getSerialNum(),
				packageName + "/" + mainActivity);
	}

	public List<String> waitWearInstallApp(String packageName) throws IOException, InterruptedException {
		System.out.println("【waitWearInstallApp】" + packageName);
		return Utility.cmd("waitWearInstallApp", CoreOptions.PYTHON, CoreOptions.SCRIPT_DIR + "\\waitWearAppInstall.py",
				this.getSerialNum(), packageName);
	}

	public void makeWearVisible() throws IOException, InterruptedException {
		System.out.println("【makeWearVisible】");
		Utility.cmd("makeWearVisible", CoreOptions.PYTHON, CoreOptions.SCRIPT_DIR + "\\makeWearVisible.py",
				this.getSerialNum());
	}

	public void pair(Device wear) throws IOException, InterruptedException {
		System.out.println("【pair】");
		Utility.cmd("pair", CoreOptions.PYTHON, CoreOptions.SCRIPT_DIR + "\\pair.py", this.getSerialNum(),
				wear.getSerialNum());
	}

	public void forgetWatch() throws IOException, InterruptedException {
		System.out.println("【forgetWatch】");
		Utility.cmd("forgetWatch", CoreOptions.PYTHON, CoreOptions.SCRIPT_DIR + "\\forgetWatch.py",
				this.getSerialNum());
	}
}
