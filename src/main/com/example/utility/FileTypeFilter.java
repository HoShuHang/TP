package main.com.example.utility;

import java.io.File;
import java.io.FileFilter;

public class FileTypeFilter implements FileFilter {
	String type;

	public FileTypeFilter(String type) {
		this.type = type;
	}

	@Override
	public boolean accept(File pathname) {
		String filename = pathname.getName();
		return filename.endsWith(type);
	}
}