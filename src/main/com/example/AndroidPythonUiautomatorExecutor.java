package main.com.example;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import main.com.example.entity.Device;
import main.com.example.entity.TestData;
import main.com.example.utility.CoreOptions;
import main.com.example.utility.Utility;
import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;

public class AndroidPythonUiautomatorExecutor implements TestExecutor{
	private final String PYTHON = "python.exe";
	private File mainTestRunner;
	private DeviceController deviceController = null;

	public AndroidPythonUiautomatorExecutor() {
		this.deviceController = new DeviceController();
	}	

	@Override
	public void executeTest(TestData testData) throws IOException, InterruptedException, ZipException {
		int index = testData.getProjectFullPath().length() - 4;
		findTestRunner(testData.getProjectFullPath().substring(0, index));
		File[] apkFiles = this.getApkFileInDir(CoreOptions.UPLOAD_DIRECTORY);
		HashMap<String, HashMap<String, String>> apkInfo = this.deviceController.getApkInfo(apkFiles);
		for (Device phone : testData.getPhones()) {
			phone.turnOnBluetooth();
//			this.deviceController.installApk(phone, apkInfo.get(CoreOptions.TAG_MOBILE).get(CoreOptions.TAG_APK_PATH));
//			this.deviceController.launchApp(phone, apkInfo);
			for (Device wear : testData.getWearable()) {
				wear.clearWearGms();
//				this.deviceController.installApk(phone, apkInfo.get(CoreOptions.TAG_WEAR).get(CoreOptions.TAG_APK_PATH));
				report.add("-------------------Mobile: " + phone.getSerialNum() + ", Wearable: " + wear.getSerialNum() + "-------------------");
				report.addAll(this.execute(phone, wear));
//				this.deviceController.uninstallApk(phone, apkInfo.get(CoreOptions.TAG_WEAR).get(CoreOptions.TAG_APK_PACKAGE));
			}
			phone.turnOffBluetooth();
		}
	}

	private void installApk(Device phone) throws IOException, InterruptedException {
		System.out.println("【installApk】 " + phone.getSerialNum());
		File folder = new File(CoreOptions.UPLOAD_DIRECTORY);
		FileFilter filter = new FileFilterWithType("apk");
		File[] files = folder.listFiles(filter);
		

		for (File apk : files) {
			System.out.println("APK: " + apk.getName());
			List<String> command = new ArrayList<String>();
			command.add(CoreOptions.ADB);
			command.add("-s");
			command.add(phone.getSerialNum());
			command.add("install");
			command.add(apk.getAbsolutePath());

			ProcessBuilder proc = new ProcessBuilder(command);
			Process p = proc.start();
			StreamConsumer stdinConsumer = new StreamConsumer(p.getInputStream(), "【Input】");
			StreamConsumer stderrConsumer = new StreamConsumer(p.getErrorStream(), "【Error】");
			stdinConsumer.start();
			stderrConsumer.start();
			p.waitFor();
		}

	}

	private void launchApp(Device phone) throws IOException, InterruptedException {
		System.out.println("【launchApp】 ");
		final String KEY_PACKAGE = "package";
		final String KEY_LAUNCHABLE_ACTIVITY = "launchable-activity";
		File folder = new File(CoreOptions.UPLOAD_DIRECTORY);
		FileFilter filter = new FileFilterWithType("apk");
		File[] files = folder.listFiles(filter);

		for (File apkFile : files) {
			List<String> dumpContent = dumpApk(apkFile);
			HashMap<String, String> info = getPackageAndActivity(dumpContent);
			String packageName = info.get(KEY_PACKAGE);
			String mainActivity = info.get(KEY_LAUNCHABLE_ACTIVITY);
			if (packageName != null && mainActivity != null)
				launch(phone, packageName, mainActivity);
		}
	}

	private void launch(Device phone, String packageName, String mainActivity)
			throws IOException, InterruptedException {
		ProcessBuilder proc = new ProcessBuilder(CoreOptions.ADB, "-s", phone.getSerialNum(), "shell", "am", "start",
				"-W", "-n", packageName + "/" + mainActivity);
		Process p = proc.start();
		StreamConsumer stdinConsumer = new StreamConsumer(p.getInputStream(), "【Input】");
		StreamConsumer stderrConsumer = new StreamConsumer(p.getErrorStream(), "【Error】");
		stdinConsumer.start();
		stderrConsumer.start();
		p.waitFor();
	}

	private List<String> dumpApk(File apkFile) throws IOException, InterruptedException {
		List<String> command = new ArrayList<String>();
		command.add(CoreOptions.AAPT);
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
				System.out.println(info.get(KEY_PACKAGE));
			}
			if (line.contains(KEY_LAUNCHABLE_ACTIVITY)) {
				info.put(KEY_LAUNCHABLE_ACTIVITY, getValue(line));
				System.out.println(info.get(KEY_LAUNCHABLE_ACTIVITY));
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

	private List<String> execute(Device phone, Device wear) throws IOException, InterruptedException {

		List<String> output = Utility.cmd(CoreOptions.PYTHON, this.mainTestRunner.getAbsolutePath(), phone.getSerialNum(), wear.getSerialNum());
		
		//		List<String> output = new ArrayList<String>();
//		ProcessBuilder proc = new ProcessBuilder(CoreOptions.PYTHON,
//				mainTestRunner.getAbsolutePath(), phone.getSerialNum(), wear.getSerialNum());
//
//		
//		Process p = proc.start();
//		
//		StreamConsumer errorConsumer = new StreamConsumer(p.getErrorStream(), "error");
//
//		errorConsumer.start();
//
//		p.waitFor();
//		output = errorConsumer.getOutput();
//		System.out.println("ExitVal: " + exitVal);

		return output;
	}

	private void findTestRunner(String directory) throws IOException {
		final String SETTING = "Setting";
		File folder = new File(directory);
//		System.out.println(folder.getPath());
		FileFilter filter = new FileFilterWithType("py");
		File[] files = folder.listFiles(filter);
		Arrays.sort(files, new FileSizeComparator());
		List<String> content = null;
		for (File file : files) {
			if (file.getName().contains(SETTING))
				continue;
			content = readFile(file);
			if (isContainTestRunner(content)) {
				mainTestRunner = file;
			}
		}
	}

	private List<String> readFile(File file) throws IOException {
		List<String> content = new ArrayList<String>();
		FileReader fr = new FileReader(file);
		BufferedReader br = new BufferedReader(fr);
		String line = "";
		while ((line = br.readLine()) != null) {
			content.add(line);
		}
		return content;
	}

	private boolean isContainTestRunner(List<String> content) {
		final String TEXT_TEST_RUNNER = "TextTestRunner";
		for (String line : content) {
			if (line.contains(TEXT_TEST_RUNNER))
				return true;
		}
		return false;
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
}
