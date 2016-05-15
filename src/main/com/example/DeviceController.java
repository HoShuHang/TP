package main.com.example;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import main.com.example.entity.Device;
import main.com.example.utility.CoreOptions;
import main.com.example.utility.FileTypeFilter;
import main.com.example.utility.Utility;

public class DeviceController {
	private final String TAG_APK_PATH = "apk_path";
	private final String TAG_APK_PACKAGE = "package";
	private final String TAG_APK_LAUNCHABLE_ACTIVITY = "launchable-activity";
	
	/* get file path, package and launchable-activity from apk */
	public HashMap<String, HashMap<String, String>> getApkInfo(File[] apkFiles) {
		HashMap<String, HashMap<String, String>> apkInfo = new HashMap<String, HashMap<String, String>>();
		for (File file : apkFiles) {
			String tagDevice = CoreOptions.TAG_WEAR;
			HashMap<String, String> info = new HashMap<String, String>();
			String packageName = this.getSpecValue(file, CoreOptions.TAG_APK_PACKAGE);
			String launchableActivity = this.getSpecValue(file, TAG_APK_LAUNCHABLE_ACTIVITY);
			if (launchableActivity != null && !launchableActivity.isEmpty())
				tagDevice = CoreOptions.TAG_MOBILE;
			System.out.println("tag: " + tagDevice);
			info.put(TAG_APK_PATH, file.getAbsolutePath());
			info.put(TAG_APK_PACKAGE, packageName);
			info.put(TAG_APK_LAUNCHABLE_ACTIVITY, launchableActivity);
			apkInfo.put(tagDevice, info);
		}
		return apkInfo;

	}

	private String getSpecValue(File apk, String target) {
		List<String> result = Utility.cmd(CoreOptions.PYTHON, CoreOptions.APK_INFO_GETTER_DIR,
				apk.getAbsolutePath(), target);
		return result.get(0).replaceAll("\\r\\n", "");
	}

	public void turnOffBluetooth(Device phone) throws IOException, InterruptedException {
		System.out.println("【turnOffBluetooth】 " + phone.getSerialNum());
		Utility.cmd(CoreOptions.PYTHON, CoreOptions.TURN_OFF_BLUETOOTH_DIR, phone.getSerialNum());
	}

	public void turnOnBluetooth(Device phone) throws IOException, InterruptedException {
		System.out.println("【turnOnBluetooth】 " + phone.getSerialNum());
		Utility.cmd(CoreOptions.PYTHON, CoreOptions.TURN_ON_BLUETOOTH_DIR, phone.getSerialNum());
	}

	public void clearWearGms(Device wear) throws IOException, InterruptedException {
		System.out.println("【clearWearGms】 " + wear.getSerialNum());
		Utility.cmd(CoreOptions.PYTHON, CoreOptions.CLEAR_GMS_DIR, wear.getSerialNum());
	}

	public void installApk(Device phone, String apkPath) {
		System.out.println("【installPhoneApk】");
		Utility.cmd(CoreOptions.PYTHON, CoreOptions.INSTALL_APK_DIR, phone.getSerialNum(),
				apkPath);
	}

	public void uninstallApk(Device phone, String packageName) {
		System.out.println("【uninstallWearApk】");
		Utility.cmd(CoreOptions.PYTHON, CoreOptions.UNINSTALL_APK_DIR, phone.getSerialNum(),
				packageName);
	}

	public void launchApp(Device phone, HashMap<String, HashMap<String, String>> apkInfo)
			throws IOException, InterruptedException {
		System.out.println("【launchApp】 ");
		String packageName = apkInfo.get(CoreOptions.TAG_MOBILE).get(TAG_APK_PACKAGE);
		String launchableActivity = apkInfo.get(CoreOptions.TAG_MOBILE).get(TAG_APK_LAUNCHABLE_ACTIVITY);
		launch(phone, packageName, launchableActivity);
	}

	private void launch(Device phone, String packageName, String mainActivity)
			throws IOException, InterruptedException {
		Utility.cmd(CoreOptions.PYTHON, CoreOptions.LAUNCH_APK_DIR, phone.getSerialNum(),
				packageName + "/" + mainActivity);
	}
}
