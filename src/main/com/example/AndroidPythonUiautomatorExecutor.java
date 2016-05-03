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
import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;

public class AndroidPythonUiautomatorExecutor implements TestExecutor{
	private final String PYTHON = "python.exe";
	private final String TAG_MOBILE = "mobile";
	private final String TAG_WEAR = "wear";
	private HashMap<String, List<Device>> deviceNumber;
	private File mainTestRunner;

	// deprecated
	public AndroidPythonUiautomatorExecutor(HashMap<String, List<Device>> deviceNumber) {
		this.deviceNumber = deviceNumber;
		this.mainTestRunner = null;
	}

	public AndroidPythonUiautomatorExecutor() {
	}
	

	@Override
	public void executeTest(TestData testData) throws IOException, InterruptedException, ZipException {
		int index = testData.getProjectFullPath().length() - 4;
		findTestRunner(testData.getProjectFullPath().substring(0, index));
		for (Device phone : testData.getPhones()) {
			for (Device wear : testData.getWearable()) {
				report.add("-------------------Mobile: " + phone.getSerialNum() + ", Wearable: " + wear.getSerialNum() + "-------------------");
				report.addAll(this.execute(phone, wear));
			}
		}
	}

	private List<String> execute(Device phone, Device wear) throws IOException, InterruptedException {
		List<String> output = new ArrayList<String>();
		ProcessBuilder proc = new ProcessBuilder(CoreOptions.PYTHON_HOME + File.separator + PYTHON,
				mainTestRunner.getAbsolutePath(), phone.getSerialNum(), wear.getSerialNum());

		
		Process p = proc.start();
		StreamConsumer errorConsumer = new StreamConsumer(p.getErrorStream(), "error");

		errorConsumer.start();

		p.waitFor();
		output = errorConsumer.getOutput();
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
