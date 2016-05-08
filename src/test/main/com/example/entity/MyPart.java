package test.main.com.example.entity;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;

import javax.servlet.http.Part;

public class MyPart implements Part{
	private String submittedFileName = null;
	private InputStream inputStream = null;

	@Override
	public void delete() throws IOException {
		
	}

	@Override
	public String getContentType() {
		return null;
	}

	@Override
	public String getHeader(String arg0) {
		return null;
	}

	@Override
	public Collection<String> getHeaderNames() {
		return null;
	}

	@Override
	public Collection<String> getHeaders(String arg0) {
		return null;
	}

	@Override
	public InputStream getInputStream() throws IOException {
		return this.inputStream;
	}

	@Override
	public String getName() {
		return null;
	}

	@Override
	public long getSize() {
		return 0;
	}

	@Override
	public String getSubmittedFileName() {
		return this.submittedFileName;
	}

	@Override
	public void write(String arg0) throws IOException {
		
	}

}
