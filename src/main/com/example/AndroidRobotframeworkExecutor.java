package main.com.example;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import main.com.example.AndroidPythonUiautomatorExecutor.FileFilterWithType;
import main.com.example.entity.Device;
import main.com.example.utility.CoreOptions;
import main.com.example.utility.Utility;

public class AndroidRobotframeworkExecutor {
	private final String PYBOT = "pybot.bat";
	private final String TAG_APK_PATH = "apk_path";
	private final String TAG_APK_PACKAGE = "package";
	private final String TAG_APK_LAUNCHABLE_ACTIVITY = "launchable-activity";
	private HashMap<String, List<Device>> deviceNumber;
	private File mainTestRunner;
	private String outputDirPath;

	public AndroidRobotframeworkExecutor(HashMap<String, List<Device>> deviceNumber) {
		this.deviceNumber = deviceNumber;
		this.mainTestRunner = null;
	}

	public AndroidRobotframeworkExecutor() {
	}

	public void setOutputDirPath(String path) {
		this.outputDirPath = path;
	}

	public List<String> executeTest() throws IOException, InterruptedException {
		List<String> output = new ArrayList<String>();
		List<Device> lstPhone = deviceNumber.get(CoreOptions.TAG_MOBILE);
		List<Device> lstWear = deviceNumber.get(CoreOptions.TAG_WEAR);
		List<HashMap<String, String>> apkInfo = getApkInfo();

		findTestRunner();
		for (Device phone : lstPhone) {
			turnOnBluetooth(phone);
			for(Device wear : lstWear){
				clearWearGms(wear);
			}
			installPhoneApk(phone, apkInfo);
			launchApp(phone, apkInfo);
			for (Device wear : lstWear) {
				installWearApk(phone, apkInfo);
				List<Device> devices = new ArrayList<Device>();
				devices.add(phone);
				devices.add(wear);
				preprocessBeforeExecuteTestScript(devices);
				execute(phone, wear);
				uninstallWearApk(phone, apkInfo);
			}
			uninstallPhoneApk(phone, apkInfo);
			turnOffBluetooth(phone);
		}
		return output;
	}

	/* get file path, package and launchable-activity from apk */
	private List<HashMap<String, String>> getApkInfo() {
		List<HashMap<String, String>> apkInfo = new ArrayList<HashMap<String, String>>();
		File folder = new File(CoreOptions.UPLOAD_DIRECTORY);
		FileFilter filter = new FileFilterWithType("apk");
		File[] files = folder.listFiles(filter);

		for (File file : files) {
			HashMap<String, String> info = new HashMap<String, String>();
			info.put(TAG_APK_PATH, file.getAbsolutePath());
			info.put(TAG_APK_PACKAGE, getSpecValue(file, TAG_APK_PACKAGE));
			info.put(TAG_APK_LAUNCHABLE_ACTIVITY, getSpecValue(file, TAG_APK_LAUNCHABLE_ACTIVITY));
			apkInfo.add(info);
		}
		return apkInfo;

	}

	private String getSpecValue(File apk, String target) {
		List<String> result = Utility.cmd(CoreOptions.PYTHON, CoreOptions.SCRIPT_DIR + "\\ApkInfoGetter.py",
				apk.getAbsolutePath(), target);
		return result.get(0);
	}

	private void installPhoneApk(Device phone, List<HashMap<String, String>> apkInfo) {
		System.out.println("【installPhoneApk】");
		for (HashMap<String, String> info : apkInfo) {
			String packageName = info.get(TAG_APK_PACKAGE);
			String launchableActivity = info.get(TAG_APK_LAUNCHABLE_ACTIVITY);
			if (launchableActivity != null && launchableActivity != "") {
				Utility.cmd(CoreOptions.PYTHON, CoreOptions.SCRIPT_DIR + "\\installApk.py", phone.getSerialNum(),
						info.get(TAG_APK_PATH));
			}
		}
	}

	private void installWearApk(Device phone, List<HashMap<String, String>> apkInfo) {
		System.out.println("【installWearApk】");
		for (HashMap<String, String> info : apkInfo) {
			String launchableActivity = info.get(TAG_APK_LAUNCHABLE_ACTIVITY);
			if (launchableActivity == null || launchableActivity == "") {
				Utility.cmd(CoreOptions.PYTHON, CoreOptions.SCRIPT_DIR + "\\installApk.py", phone.getSerialNum(),
						info.get(TAG_APK_PATH));
			}
		}
	}
	
	private void waitWearInstall(Device wear){
		System.out.println("【waitWearInstall】");
		Utility.cmd(CoreOptions.PYTHON, CoreOptions.SCRIPT_DIR + "\\waitWearInstallApp.py", wear.getSerialNum());
	}

	private void uninstallWearApk(Device phone, List<HashMap<String, String>> apkInfo) {
		System.out.println("【uninstallWearApk】");
		for (HashMap<String, String> info : apkInfo) {
			String packageName = info.get(TAG_APK_PACKAGE);
			String launchableActivity = info.get(TAG_APK_LAUNCHABLE_ACTIVITY);
			// wear app has no launchable-activity
			if (launchableActivity == null || launchableActivity == "") {
				Utility.cmd(CoreOptions.PYTHON, CoreOptions.SCRIPT_DIR + "\\uninstallApk.py", phone.getSerialNum(),
						packageName);
			}
		}
	}

	private void uninstallPhoneApk(Device phone, List<HashMap<String, String>> apkInfo) {
		System.out.println("【uninstallPhoneApk】");
		for (HashMap<String, String> info : apkInfo) {
			String packageName = info.get(TAG_APK_PACKAGE);
			String launchableActivity = info.get(TAG_APK_LAUNCHABLE_ACTIVITY);
			// wear app has no launchable-activity
			if (launchableActivity != null && launchableActivity != "") {
				Utility.cmd(CoreOptions.PYTHON, CoreOptions.SCRIPT_DIR + "\\uninstallApk.py", phone.getSerialNum(),
						packageName);
			}
		}
	}

	private void launchApp(Device phone, List<HashMap<String, String>> apkInfo)
			throws IOException, InterruptedException {
		System.out.println("【launchApp】 ");
		for (HashMap<String, String> info : apkInfo) {
			String packageName = info.get(TAG_APK_PACKAGE);
			String launchableActivity = info.get(TAG_APK_LAUNCHABLE_ACTIVITY);
			// wear app has no launchable-activity
			if (launchableActivity != null && launchableActivity != "") {
				launch(phone, packageName, launchableActivity);
			}
		}
	}

	private void launch(Device phone, String packageName, String mainActivity)
			throws IOException, InterruptedException {
		Utility.cmd(CoreOptions.PYTHON, CoreOptions.SCRIPT_DIR + "\\launchApk.py", phone.getSerialNum(), packageName,
				mainActivity);
	}

	private List<String> dumpApk(File apkFile) throws IOException, InterruptedException {
		List<String> command = new ArrayList<String>();
		command.add(CoreOptions.ANDROID_HOME + "\\build-tools\\22.0.1\\aapt.exe");
		command.add("dump");
		command.add("badging");
		command.add(apkFile.getAbsolutePath());

		ProcessBuilder proc = new ProcessBuilder(command);
		Process p = proc.start();
		StreamConsumer stdinConsumer = new StreamConsumer(p.getInputStream(), "【Input】");
		StreamConsumer stderrConsumer = new StreamConsumer(p.getErrorStream(), "【Error】");
		stdinConsumer.start();
		stderrConsumer.start();
		p.waitFor();
		return stdinConsumer.getOutput();
	}

	private HashMap<String, String> getPackageAndActivity(List<String> content) {
		HashMap<String, String> info = new HashMap<String, String>();
		final String KEY_PACKAGE = "package";
		final String KEY_LAUNCHABLE_ACTIVITY = "launchable-activity";
		for (String line : content) {
			if (line.contains(KEY_PACKAGE)) {
				info.put(KEY_PACKAGE, getValue(line));
			}
			if (line.contains(KEY_LAUNCHABLE_ACTIVITY)) {
				info.put(KEY_LAUNCHABLE_ACTIVITY, getValue(line));
			}
		}
		return info;
	}

	private String getValue(String line) {
		final String NAME = "name";
		String packageName = "";
		String[] tokens = line.split(" ");
		for (String token : tokens) {
			if (token.contains(NAME)) {
				int index = token.indexOf(NAME);
				String sub = token.substring(index + NAME.length(), token.length());
				packageName = sub.split("[=']")[2];
			}
		}
		return packageName;
	}

	private void preprocessBeforeExecuteTestScript(List<Device> devices) throws IOException {
		System.out.println("【preprocessBeforeExecuteTestScript】 ");
		findTestRunner();
		List<String> content = readFile(mainTestRunner);
		List<String> newContent = changeLibraryPath(content);
		newContent = changeSerialNumber(newContent, devices);
		writeFile(mainTestRunner, newContent);
	}

	private List<String> execute(Device phone, Device wear) throws IOException, InterruptedException {
		System.out.println("【execute】 ");
		List<String> output = new ArrayList<String>();
		Process p = null;
		try {
			List<String> command = new ArrayList<String>();
			command.add(PYBOT);
			command.add("--outputdir");
			command.add(outputDirPath + "/" + phone.getSerialNum() + "_" + wear.getSerialNum());
			command.add(mainTestRunner.getAbsolutePath());

			ProcessBuilder proc = new ProcessBuilder(command);
			p = proc.start();
			StreamConsumer stdinConsumer = new StreamConsumer(p.getInputStream(), "【Input】");
			StreamConsumer stderrConsumer = new StreamConsumer(p.getErrorStream(), "【Error】");
			stdinConsumer.start();
			stderrConsumer.start();
			p.waitFor();
		} finally {
			p.destroy();
		}
		return output;
	}

	private void findTestRunner() throws IOException {
		System.out.println("【findTestRunner】");
		File folder = new File(CoreOptions.UPLOAD_DIRECTORY);
		// System.out.println(folder.getPath());
		FileFilter filter = new FileFilterWithType("txt");
		File[] files = folder.listFiles(filter);
		Arrays.sort(files, new FileSizeComparator());
		for (File file : files) {
			List<String> content = readFile(file);
			for (String line : content) {
				if (line.contains("Test Case")) {
					mainTestRunner = file;
					break;
				}
			}
		}
	}

	private List<String> readFile(File file) throws IOException {
		List<String> content = new ArrayList<String>();
		if (file.exists()) {
			FileReader fr = new FileReader(file);
			BufferedReader br = new BufferedReader(fr);
			String line = "";
			while ((line = br.readLine()) != null) {
				content.add(line);
			}
		} else {
			System.out.println("no file");
		}
		return content;
	}

	private List<String> changeLibraryPath(List<String> content) {
		final String MOBILE_PY = "Mobile.py";
		List<String> newContent = new ArrayList<String>();

		for (String line : content) {
			if (line.contains(MOBILE_PY)) {
				line = replaceMobilePyPath(line);
			}
			newContent.add(line);
		}

		return newContent;
	}

	private String replaceMobilePyPath(String line) {
		final String MOBILE_PY_PATH = "robotframework-uiautomatorlibrary-0.1/uiautomatorlibrary/Mobile.py";
		int position = line.indexOf(MOBILE_PY_PATH);
		String newLine = "";
		newLine += line.substring(0, 18) + MOBILE_PY_PATH;
		newLine += line.substring(position + MOBILE_PY_PATH.length(), line.length());
		return newLine;
	}

	private List<String> changeSerialNumber(List<String> content, List<Device> devices) {
		List<String> newContent = new ArrayList<String>();
		final String KERWORD_SET_SERIAL = "Set Serial";
		final String KEYWORD_COMMENT = "comment";
		String space = "";
		int index = 0;

		for (int i = 0; i < CoreOptions.INTERVAL_SPACE_NUM; i++)
			space += " ";

		for (String line : content) {
			StringBuffer sb = new StringBuffer();
			if (line.contains(KERWORD_SET_SERIAL) && !line.contains(KEYWORD_COMMENT)) {
				String newLine = "";
				String[] tokens = line.split(space);
				tokens[tokens.length - 1] = devices.get(index++).getSerialNum(); // change
																					// serial
																					// number
				for (String token : tokens) {
					sb.append(token);
					sb.append(space);
				}
				newLine = sb.toString();
				newContent.add(newLine);
				continue;
			}
			newContent.add(line);
		}
		return newContent;
	}

	private void writeFile(File file, List<String> content) {
		FileWriter fw;
		try {
			fw = new FileWriter(file);
			for (String line : content) {
				fw.write(line);
				fw.write("\n");
			}
			fw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	class FileFilterWithType implements FileFilter {
		String type;

		public FileFilterWithType(String type) {
			this.type = type;
		}

		@Override
		public boolean accept(File pathname) {
			String filename = pathname.getName();
			return filename.endsWith(type);
		}
	}

	class FileSizeComparator implements Comparator<File> {
		@Override
		public int compare(File fa, File fb) {
			long aSize = fa.length();
			long bSize = fb.length();
			if (aSize == bSize) {
				return 0;
			} else {
				return Long.compare(aSize, bSize);
			}
		}
	}

	private void turnOffBluetooth(Device phone) throws IOException, InterruptedException {
		System.out.println("【turnOffBluetooth】 " + phone.getSerialNum());
		List<String> command = new ArrayList<String>();

		command.add(CoreOptions.PYTHON);
		command.add(CoreOptions.SCRIPT_DIR + "\\turnOffBluetooth.py");
		command.add(phone.getSerialNum());
		ProcessBuilder proc = new ProcessBuilder(command);
		Process p = proc.start();
		StreamConsumer stdinConsumer = new StreamConsumer(p.getInputStream(), "【Input】");
		StreamConsumer stderrConsumer = new StreamConsumer(p.getErrorStream(), "【Error】");
		stdinConsumer.start();
		stderrConsumer.start();
		p.waitFor();
	}

	private void turnOnBluetooth(Device phone) throws IOException, InterruptedException {
		System.out.println("【turnOnBluetooth】 " + phone.getSerialNum());
		List<String> command = new ArrayList<String>();

		command.add(CoreOptions.PYTHON);
		command.add(CoreOptions.SCRIPT_DIR + "\\turnOnBluetooth.py");
		command.add(phone.getSerialNum());
		ProcessBuilder proc = new ProcessBuilder(command);
		Process p = proc.start();
		StreamConsumer stdinConsumer = new StreamConsumer(p.getInputStream(), "【Input】");
		StreamConsumer stderrConsumer = new StreamConsumer(p.getErrorStream(), "【Error】");
		stdinConsumer.start();
		stderrConsumer.start();
		p.waitFor();
	}

	private void clearWearGms(Device wear) throws IOException, InterruptedException {
		System.out.println("【clearWearGms】 " + wear.getSerialNum());
		List<String> command = new ArrayList<String>();

		command.add(CoreOptions.PYTHON);
		command.add(CoreOptions.SCRIPT_DIR + "\\clearGms.py");
		command.add(wear.getSerialNum());
		ProcessBuilder proc = new ProcessBuilder(command);
		Process p = proc.start();
		StreamConsumer stdinConsumer = new StreamConsumer(p.getInputStream(), "【Input】");
		StreamConsumer stderrConsumer = new StreamConsumer(p.getErrorStream(), "【Error】");
		stdinConsumer.start();
		stderrConsumer.start();
		p.waitFor();
	}

	private void installApk(Device phone, List<HashMap<String, String>> apkInfo)
			throws IOException, InterruptedException {
		for (HashMap<String, String> info : apkInfo) {
			List<String> command = new ArrayList<String>();
			Utility.cmd(CoreOptions.PYTHON, CoreOptions.SCRIPT_DIR + "\\installApk.py", phone.getSerialNum(),
					info.get(TAG_APK_PATH));
		}

	}
}
