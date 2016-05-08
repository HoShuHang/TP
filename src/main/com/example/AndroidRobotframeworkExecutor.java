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
import main.com.example.entity.TestData;
import main.com.example.utility.CoreOptions;
import main.com.example.utility.Utility;

public class AndroidRobotframeworkExecutor implements TestExecutor {
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

	@Override
	public void executeTest(TestData testData) throws IOException, InterruptedException {
		HashMap<String, HashMap<String, String>> apkInfo = getApkInfo();

		// findTestRunner();
		for (Device phone : testData.getPhones()) {
			turnOnBluetooth(phone);
			installApk(phone, apkInfo.get(CoreOptions.TAG_MOBILE).get(TAG_APK_PATH));
			if(!isAppExists(phone, apkInfo.get(CoreOptions.TAG_MOBILE).get(TAG_APK_PACKAGE))){
				System.out.println("app not exists");
				continue;
			}
			
			launchApp(phone, apkInfo);
			for (Device wear : testData.getWearable()) {
				clearWearGms(wear);
				installApk(phone, apkInfo.get(CoreOptions.TAG_WEAR).get(TAG_APK_PATH));
				List<Device> devices = new ArrayList<Device>();
				devices.add(phone);
				devices.add(wear);
				preprocessBeforeExecuteTestScript(testData, devices);
				execute(phone, wear);
				uninstallApk(phone, apkInfo.get(CoreOptions.TAG_WEAR).get(TAG_APK_PACKAGE));
			}
			uninstallApk(phone, apkInfo.get(CoreOptions.TAG_MOBILE).get(TAG_APK_PACKAGE));
			turnOffBluetooth(phone);
		}
	}

	public void setOutputDirPath(String path) {
		this.outputDirPath = path;
	}

	/* get file path, package and launchable-activity from apk */
	private HashMap<String, HashMap<String, String>> getApkInfo() {
		File folder = new File(CoreOptions.UPLOAD_DIRECTORY);
		FileFilter filter = new FileFilterWithType("apk");
		File[] files = folder.listFiles(filter);
		HashMap<String, HashMap<String, String>> apkInfo = new HashMap<String, HashMap<String, String>>();
		for (File file : files) {
			String tagDevice = CoreOptions.TAG_WEAR;
			HashMap<String, String> info = new HashMap<String, String>();
			String packageName = getSpecValue(file, TAG_APK_PACKAGE);
			String launchableActivity = getSpecValue(file, TAG_APK_LAUNCHABLE_ACTIVITY);
			if (launchableActivity != null && !launchableActivity.isEmpty())
				tagDevice = CoreOptions.TAG_MOBILE;
			info.put(TAG_APK_PATH, file.getAbsolutePath());
			info.put(TAG_APK_PACKAGE, packageName);
			info.put(TAG_APK_LAUNCHABLE_ACTIVITY, launchableActivity);
			apkInfo.put(tagDevice, info);
		}
		return apkInfo;

	}

	private String getSpecValue(File apk, String target) {
		List<String> result = Utility.cmd(CoreOptions.PYTHON, CoreOptions.SCRIPT_DIR + "\\ApkInfoGetter.py",
				apk.getAbsolutePath(), target);
		return result.get(0).replaceAll("\\r\\n", "");
	}

	private void installApk(Device phone, String apkPath) {
		System.out.println("【installPhoneApk】");
		Utility.cmd(CoreOptions.PYTHON, CoreOptions.SCRIPT_DIR + "\\installApk.py", phone.getSerialNum(), apkPath);
	}

	private boolean isAppExists(Device device, String packageName) {
		String result = Utility.cmd(CoreOptions.PYTHON, CoreOptions.SCRIPT_DIR + "\\checkPackageExsits.py",
				device.getSerialNum(), packageName).get(0).replaceAll("\\r\\n", "");
		return result.equals("True");
	}

	private void uninstallApk(Device phone, String packageName) {
		System.out.println("【uninstallWearApk】");
		Utility.cmd(CoreOptions.PYTHON, CoreOptions.SCRIPT_DIR + "\\uninstallApk.py", phone.getSerialNum(),
				packageName);
	}

	private void launchApp(Device phone, HashMap<String, HashMap<String, String>> apkInfo)
			throws IOException, InterruptedException {
		System.out.println("【launchApp】 ");
		String packageName = apkInfo.get(CoreOptions.TAG_MOBILE).get(TAG_APK_PACKAGE);
		String launchableActivity = apkInfo.get(CoreOptions.TAG_MOBILE).get(TAG_APK_LAUNCHABLE_ACTIVITY);
		launch(phone, packageName, launchableActivity);
	}

	private void launch(Device phone, String packageName, String mainActivity)
			throws IOException, InterruptedException {
		Utility.cmd(CoreOptions.PYTHON, CoreOptions.SCRIPT_DIR + "\\launchApk.py", phone.getSerialNum(),
				packageName + "/" + mainActivity);
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
		Utility.cmd(CoreOptions.PYTHON, CoreOptions.SCRIPT_DIR + "\\turnOffBluetooth.py", phone.getSerialNum());
	}

	private void turnOnBluetooth(Device phone) throws IOException, InterruptedException {
		System.out.println("【turnOnBluetooth】 " + phone.getSerialNum());
		Utility.cmd(CoreOptions.PYTHON, CoreOptions.SCRIPT_DIR + "\\turnOnBluetooth.py", phone.getSerialNum());
	}

	private void clearWearGms(Device wear) throws IOException, InterruptedException {
		System.out.println("【clearWearGms】 " + wear.getSerialNum());
		Utility.cmd(CoreOptions.PYTHON, CoreOptions.SCRIPT_DIR + "\\clearGms.py", wear.getSerialNum());
	}

}
