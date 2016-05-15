package main.com.example;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import main.com.example.entity.Device;
import main.com.example.entity.TestData;
import main.com.example.utility.CoreOptions;
import main.com.example.utility.FileTypeFilter;
import main.com.example.utility.Utility;

public class AndroidRobotframeworkExecutor implements TestExecutor {
	private final String PYBOT = "pybot.bat";
	private final String TAG_APK_LAUNCHABLE_ACTIVITY = "launchable-activity";
	private HashMap<String, List<Device>> deviceNumber;
	private File mainTestRunner;
	private String outputDirPath;
	private DeviceController deviceController = null;

	public AndroidRobotframeworkExecutor(HashMap<String, List<Device>> deviceNumber) {
		this.deviceNumber = deviceNumber;
		this.mainTestRunner = null;
		this.deviceController = new DeviceController();
	}

	public AndroidRobotframeworkExecutor() {
	}

	@Override
	public void executeTest(TestData testData) throws IOException, InterruptedException {
		File[] apkFiles = this.getApkFileInDir(CoreOptions.UPLOAD_DIRECTORY);
		HashMap<String, HashMap<String, String>> apkInfo = this.deviceController.getApkInfo(apkFiles);

		// findTestRunner();
		for (Device phone : testData.getPhones()) {
			this.deviceController.turnOnBluetooth(phone);
			this.deviceController.installApk(phone, apkInfo.get(CoreOptions.TAG_MOBILE).get(CoreOptions.TAG_APK_PATH));
			this.deviceController.launchApp(phone, apkInfo);
			for (Device wear : testData.getWearable()) {
				this.deviceController.clearWearGms(wear);
				this.deviceController.installApk(phone, apkInfo.get(CoreOptions.TAG_WEAR).get(CoreOptions.TAG_APK_PATH));
				List<Device> devices = new ArrayList<Device>();
				devices.add(phone);
				devices.add(wear);
				preprocessBeforeExecuteTestScript(testData, devices);
				this.execute(phone, wear);
				this.deviceController.uninstallApk(phone, apkInfo.get(CoreOptions.TAG_WEAR).get(CoreOptions.TAG_APK_PACKAGE));
			}
			this.deviceController.uninstallApk(phone, apkInfo.get(CoreOptions.TAG_MOBILE).get(CoreOptions.TAG_APK_PACKAGE));
			this.deviceController.turnOffBluetooth(phone);
		}
	}

	private void preprocessBeforeExecuteTestScript(TestData testData, List<Device> devices) throws IOException {
		System.out.println("【preprocessBeforeExecuteTestScript】 ");
		int index = testData.getProjectFullPath().length() - 4;
		findTestRunner(testData.getProjectFullPath().substring(0, index));
		List<String> content = readFile(mainTestRunner);
		List<String> newContent = changeLibraryPath(content);
		newContent = changeSerialNumber(newContent, devices);
		writeFile(mainTestRunner, newContent);
	}

	private List<String> execute(Device phone, Device wear) throws IOException, InterruptedException {
		System.out.println("【execute】 ");
		List<String> output = Utility.cmd(PYBOT, "--outputdir",
				outputDirPath + "/" + phone.getSerialNum() + "_" + wear.getSerialNum(),
				mainTestRunner.getAbsolutePath());
		return output;
	}

	private void findTestRunner(String directory) throws IOException {
		System.out.println("【findTestRunner】");
		File folder = new File(directory);
		// System.out.println(folder.getPath());
		FileFilter filter = new FileTypeFilter("txt");
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
		final String MOBILE_PY_PATH = "D:/Thesis/robotframework-uiautomatorlibrary-0.1/uiautomatorlibrary/Mobile.py";
		final String IMPORT_INTERVAL_SPACE = "           ";
		final String GENERAL_INTERVAL_SPACE = "    ";
		StringBuffer sb = new StringBuffer();
		String[] tokens = line.split("\\s+");
		sb.append(tokens[0]);
		sb.append(IMPORT_INTERVAL_SPACE);
		sb.append(MOBILE_PY_PATH);
		sb.append(GENERAL_INTERVAL_SPACE);
		sb.append(tokens[2]);
		sb.append(" ");
		sb.append(tokens[3]);
		sb.append(GENERAL_INTERVAL_SPACE);
		sb.append(tokens[4]);
		return sb.toString();
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
