package com.example;

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

import com.example.utility.CoreOptions;

public class AndroidPythonUiautomatorExecutor {
	private final String PYTHON = "python.exe";
	private final String TAG_MOBILE = "mobile";
	private final String TAG_WEAR = "wear";
	private HashMap<String, List<String>> deviceNumber;
	private File mainTestRunner;

	public AndroidPythonUiautomatorExecutor(HashMap<String, List<String>> deviceNumber) {
		this.deviceNumber = deviceNumber;
		this.mainTestRunner = null;
	}

	public AndroidPythonUiautomatorExecutor() {
	}

	public List<String> executeTest() throws IOException, InterruptedException {
		List<String> output = new ArrayList<String>();
		findTestRunner();
		List<String> lstPhone = deviceNumber.get(TAG_MOBILE);
		List<String> lstWear = deviceNumber.get(TAG_WEAR);
		for (String phone : lstPhone) {
			for (String wear : lstWear) {
				output = execute(phone, wear);
			}
		}

		return output;
	}

	private List<String> execute(String snPhone, String snWear) throws IOException, InterruptedException {
		List<String> output = new ArrayList<String>();
		ProcessBuilder proc = new ProcessBuilder(CoreOptions.PYTHON_HOME + File.separator + PYTHON,
				mainTestRunner.getAbsolutePath());

		
		Process p = proc.start();
		StreamConsumer errorConsumer = new StreamConsumer(p.getErrorStream(), "error");

		errorConsumer.start();

		int exitVal = p.waitFor();
		output = errorConsumer.getOutput();
//		System.out.println("ExitVal: " + exitVal);

		return output;
	}

	private void findTestRunner() throws IOException {
		final String SETTING = "Setting";
		File folder = new File(CoreOptions.UPLOAD_DIRECTORY);
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
