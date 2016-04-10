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
import java.util.Map;

import main.com.example.AndroidPythonUiautomatorExecutor.FileFilterWithType;
import main.com.example.AndroidPythonUiautomatorExecutor.FileSizeComparator;
import main.com.example.entity.Device;
import main.com.example.utility.CoreOptions;

public class RobotframeworkExecutor {
	private final String PYBOT = "pybot.bat";
	private final String TAG_MOBILE = "mobile";
	private final String TAG_WEAR = "wear";
	private HashMap<String, List<Device>> deviceNumber;
	private File mainTestRunner;
	private String outputDirPath;

	public RobotframeworkExecutor(HashMap<String, List<Device>> deviceNumber) {
		this.deviceNumber = deviceNumber;
		this.mainTestRunner = null;
	}

	public RobotframeworkExecutor() {
	}
	
	public void setOutputDirPath(String path){
		this.outputDirPath = path;
	}

	public List<String> executeTest() throws IOException, InterruptedException {
		List<String> output = new ArrayList<String>();
		
		findTestRunner();
		List<String> content = readFile(mainTestRunner);
		List<String> newContent = changeLibraryPath(content);
		List<Device> lstPhone = deviceNumber.get(TAG_MOBILE);
		List<Device> lstWear = deviceNumber.get(TAG_WEAR);
		for (Device phone : lstPhone) {
			for (Device wear : lstWear) {
				List<String> devices = new ArrayList<String>();
				devices.add(phone.getSerialNum());
				devices.add(wear.getSerialNum());
				newContent = changeSerialNumber(newContent, devices);
				writeFile(mainTestRunner, newContent);
				execute(phone, wear);
			}
		}
		return output;
	}
	
	private List<String> execute(Device phone, Device wear) throws IOException, InterruptedException {
		List<String> output = new ArrayList<String>();
		Process p = null;
		try {
			List<String> command = new ArrayList<String>();
			command.add(PYBOT);
			command.add("--outputdir");
			command.add(outputDirPath + "/" + phone.getSerialNum() + "_" + wear.getSerialNum());
			command.add(mainTestRunner.getAbsolutePath());

			ProcessBuilder proc = new ProcessBuilder(command);
			Map<String, String> env = proc.environment();
			proc.redirectErrorStream(true);
			proc.redirectOutput(new File("D:\\Thesis\\UploadSpace\\Log.txt"));

			p = proc.start();
			p.waitFor();
		} finally {
			p.destroy();
		}
		return output;
	}

	private void findTestRunner() throws IOException {
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
		List<String> content = readFile(mainTestRunner);
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
			if(line.contains(MOBILE_PY)) {
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
	
	private List<String> changeSerialNumber(List<String> content, List<String> devices){
		List<String> newContent = new ArrayList<String>();
		final String CONTAIN_KEYWORD = "Set Serial";
		String newLine = "";
		String space = "";
		int index=0;
		
		for(int i=0 ; i<CoreOptions.INTERVAL_SPACE_NUM ; i++)
			space += " ";
		
		for(String line : content){
			newLine = line;
			StringBuffer sb = new StringBuffer();
			if(line.contains(CONTAIN_KEYWORD)){
				String[] tokens = line.split(space);
				tokens[tokens.length-1] = devices.get(index++);
				for(String token : tokens){
					sb.append(space);
					sb.append(token);
				}
				newLine = sb.toString();
			}
			newContent.add(newLine);
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
