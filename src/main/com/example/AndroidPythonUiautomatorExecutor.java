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
import main.com.example.entity.Pair;
import main.com.example.entity.Report;
import main.com.example.entity.TestData;
import main.com.example.utility.CoreOptions;
import main.com.example.utility.Utility;
import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;

public class AndroidPythonUiautomatorExecutor implements TestExecutor {
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
		
		for (Pair pair : testData.getPairs()) {
			Device phone = pair.getPhone();
			Device wear = pair.getWear();
			Report report = new Report();
			report.setPair(pair);
			List<String> testingMessage = this.execute(phone, wear);
			report.setTestingMessage(testingMessage);
//			report.setPassTestCaseNumber(parser.getPassTestCaseNumber(testingMessage));
//			report.setPassTesting(parser.isPassTesting(testingMessage));
//			report.setFailTestCaseNumber(parser.getFailTestCaseNumber(testingMessage));
//			report.setTotalTestCase(parser.getTotalTestCase(testingMessage));
//			report.setTestingMessage(parser.getTestingMessage(testingMessage));
			pair.setReport(report);
			pair.setTestComplete(true);
			//HashMap<String, Object> report = new HashMap<String, Object>();
//			report.put(CoreOptions.TAG_MOBILE, phone.getSerialNum());
//			report.put(CoreOptions.TAG_WEAR, wear.getSerialNum());
//			report.put(CoreOptions.TAG_REPORT, this.execute(phone, wear));
		}

//		for (Device phone : testData.getPhones()) {
//			// phone.turnOnBluetooth();
//			// this.deviceController.installApk(phone,
//			// apkInfo.get(CoreOptions.TAG_MOBILE).get(CoreOptions.TAG_APK_PATH));
//			// this.deviceController.launchApp(phone, apkInfo);
//			for (Device wear : testData.getWearable()) {
//				// wear.clearWearGms();
//				// this.deviceController.installApk(phone,
//				// apkInfo.get(CoreOptions.TAG_WEAR).get(CoreOptions.TAG_APK_PATH));
//				HashMap<String, Object> report = new HashMap<String, Object>();
//				report.put(CoreOptions.TAG_MOBILE, phone.getSerialNum());
//				report.put(CoreOptions.TAG_WEAR, wear.getSerialNum());
//				report.put(CoreOptions.TAG_REPORT, this.execute(phone, wear));
//				// lstReport.add("-------------------Mobile: " +
//				// phone.getSerialNum() + ", Wearable: " + wear.getSerialNum() +
//				// "-------------------");
//				// lstReport.addAll(this.execute(phone, wear));
//				// this.deviceController.uninstallApk(phone,
//				// apkInfo.get(CoreOptions.TAG_WEAR).get(CoreOptions.TAG_APK_PACKAGE));
//			}
//			// phone.turnOffBluetooth();
//		}
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
//		Utility.cmd("chmod", "+x", this.mainTestRunner.getAbsolutePath());
		List<String> output = Utility.cmd("test", CoreOptions.PYTHON, this.mainTestRunner.getAbsolutePath(),
				phone.getSerialNum(), wear.getSerialNum());
		
//		List<String> output = Utility.cmd("ADB",CoreOptions.ADB, "devices");
		

		// List<String> output = new ArrayList<String>();
		// ProcessBuilder proc = new ProcessBuilder(CoreOptions.PYTHON,
		// mainTestRunner.getAbsolutePath(), phone.getSerialNum(),
		// wear.getSerialNum());
		//
		//
		// Process p = proc.start();
		//
		// StreamConsumer errorConsumer = new StreamConsumer(p.getErrorStream(),
		// "error");
		//
		// errorConsumer.start();
		//
		// p.waitFor();
		// output = errorConsumer.getOutput();
		// System.out.println("ExitVal: " + exitVal);

		return output;
	}

	private void findTestRunner(String directory) throws IOException {
		final String SETTING = "Setting";
		File folder = new File(directory);
		// System.out.println(folder.getPath());
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
