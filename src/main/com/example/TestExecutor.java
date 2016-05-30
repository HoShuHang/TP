package main.com.example;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import main.com.example.entity.Report;
import main.com.example.entity.TestData;
import main.com.example.utility.CoreOptions;
import main.com.example.utility.FileTypeFilter;
import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;

public interface TestExecutor {
//	public List<String> report = new ArrayList<String>();
	public List<Report> lstReport = new ArrayList<Report>();
	public default void unzipProject(TestData testData) throws IOException, ZipException{
		ZipFile zip = testData.getProject();
		zip.extractAll(CoreOptions.UPLOAD_DIRECTORY);
	}
	
	public default void execute(TestData testData) throws IOException, ZipException, InterruptedException{
		unzipProject(testData);
		executeTest(testData);
	}
	
	public void executeTest(TestData testData) throws IOException, InterruptedException, ZipException;
	
//	public default List<String> getTestReport() {
//		return report;
//	}
	public default List<Report> getTestReport() {
		return lstReport;
	}
	
	public default File[] getApkFileInDir(String dir){
		File folder = new File(dir);
		FileFilter filter = new FileTypeFilter("apk");
		return folder.listFiles(filter);
	}
}
