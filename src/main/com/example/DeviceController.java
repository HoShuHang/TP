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
	public HashMap<String, HashMap<String, String>> getApkInfo(File[] apkFiles) throws IOException, InterruptedException {
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

	private String getSpecValue(File apk, String target) throws IOException, InterruptedException {
		List<String> result = Utility.cmd("getSpecValue", CoreOptions.PYTHON, CoreOptions.APK_INFO_GETTER_DIR,
				apk.getAbsolutePath(), target);
		return result.get(0).replaceAll("\\r\\n", "");
	}
}
