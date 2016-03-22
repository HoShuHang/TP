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

import main.com.example.AndroidPythonUiautomatorExecutor.FileFilterWithType;
import main.com.example.AndroidPythonUiautomatorExecutor.FileSizeComparator;
import main.com.example.entity.Device;
import main.com.example.utility.CoreOptions;

public class RobotframeworkExecutor {
	private final String PYBOT = "pybot.bat";
	private HashMap<String, List<Device>> deviceNumber;
	private File mainTestRunner;

	public RobotframeworkExecutor(HashMap<String, List<Device>> deviceNumber) {
		this.deviceNumber = deviceNumber;
		this.mainTestRunner = null;
	}

	public RobotframeworkExecutor() {
	}

	public List<String> executeTest() throws IOException, InterruptedException {
		String test = "dMobile.Set Serial    CB5A259ZSX";
		HashMap<String, String> devicesMap = new HashMap<String, String>();
		final String REPORT_PATH = "C:" + File.separator + "Users" + File.separator + System.getenv("USERNAME")
		+ File.separator + "report.html";
		List<String> output = new ArrayList<String>();

		findTestRunner();
		List<String> content = readFile(mainTestRunner);
		List<String> newContent = replace(content);
		writeFile(mainTestRunner, newContent);
		execute(null, null);
		output = readFile(new File(REPORT_PATH));
		return output;
	}

	private List<String> execute(Device phone, Device wear) throws IOException, InterruptedException {
		List<String> output = new ArrayList<String>();
		ProcessBuilder proc = new ProcessBuilder(
				CoreOptions.PYTHON_HOME + File.separator + "Scripts" + File.separator + PYBOT,
				mainTestRunner.getAbsolutePath());

		Process p = proc.start();
		StreamConsumer errorConsumer = new StreamConsumer(p.getInputStream(), "error");

		errorConsumer.start();

		p.waitFor();
		output = errorConsumer.getOutput();
		// System.out.println("ExitVal: " + exitVal);

		return output;
	}

	private void findTestRunner() throws IOException {
		File folder = new File(CoreOptions.UPLOAD_DIRECTORY);
		// System.out.println(folder.getPath());
		FileFilter filter = new FileFilterWithType("txt");
		File[] files = folder.listFiles(filter);
		Arrays.sort(files, new FileSizeComparator());
		mainTestRunner = files[0];
		List<String> content = readFile(mainTestRunner);
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

	private List<String> replace(List<String> content) {
		final String MOBILE_PY = "Mobile.py";
		final String WITH_NAME = "WITH NAME";
		List<String> devices = new ArrayList<String>();
		List<String> newContent = new ArrayList<String>();

		for (String line : content) {
			if (line.contains(MOBILE_PY)) {
				line = replaceMobilePyPath(line);
			}
			if (line.contains(WITH_NAME)) {
				devices.add(getDeviceToken(line));
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

	private String getDeviceToken(String line) {
		final String WITH_NAME = "WITH NAME";
		String token = "";
		int position = line.indexOf(WITH_NAME);
		token = line.substring(position + WITH_NAME.length() + 4, line.length());
		return token;
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
