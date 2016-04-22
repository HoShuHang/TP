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

import main.com.example.entity.Device;
import main.com.example.utility.CoreOptions;

public class AndroidRobotframeworkExecutor {
	private final String PYBOT = "pybot.bat";
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

		findTestRunner();
		for (Device phone : lstPhone) {
			turnOnBluetooth(phone);
			for (Device wear : lstWear) {
				clearWearGms(wear);
			}

			installApk(phone);
			for (Device wear : lstWear) {
				List<Device> devices = new ArrayList<Device>();
				devices.add(phone);
				devices.add(wear);
				preprocessBeforeExecuteTestScript(devices);
				launchApp(phone);
				execute(phone, wear);
			}
			turnOffBluetooth(phone);
		}
		return output;
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

	private void installApk(Device phone) throws IOException, InterruptedException {
		System.out.println("【installApk】 " + phone.getSerialNum());
		File folder = new File(CoreOptions.UPLOAD_DIRECTORY);
		FileFilter filter = new FileFilterWithType("apk");
		File[] files = folder.listFiles(filter);
		// System.out.println("File size: " + files.length + " Device size: " +
		// lstPhone.size());

		for (File apk : files) {
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
		System.out.println("apk number" + files.length);
		for (File apkFile : files) {
			List<String> dumpContent = dumpApk(apkFile);
			HashMap<String, String> info = getPackageAndActivity(dumpContent);
			String packageName = info.get(KEY_PACKAGE);
			String mainActivity = info.get(KEY_LAUNCHABLE_ACTIVITY);
			if (packageName != null && mainActivity != null || packageName != "" && mainActivity != "") {
				launch(phone, packageName, mainActivity);
			}
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
			System.out.println(line);
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
				// System.out.println(newLine);
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
}
