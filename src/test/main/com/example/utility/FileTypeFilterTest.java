package test.main.com.example.utility;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileFilter;
import java.lang.reflect.Field;

import org.junit.Before;
import org.junit.Test;

import main.com.example.utility.CoreOptions;
import main.com.example.utility.FileTypeFilter;

public class FileTypeFilterTest {
	private FileFilter fileTypeFilter = null;
	private String expectType = "apk";
	
	@Before
	public void setUp() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		this.fileTypeFilter = new FileTypeFilter(this.expectType);
		Field typeField = FileTypeFilter.class.getDeclaredField("type");
		typeField.setAccessible(true);
		String actualType = (String) typeField.get(this.fileTypeFilter);
		assertEquals(this.expectType, actualType);
	}
	
	@Test
	public void testAcceptApk() {
		File file = new File(CoreOptions.SCRIPT_DIR + "test.apk");
		assertTrue(this.fileTypeFilter.accept(file));
	}
	
	@Test
	public void testAcceptNotApk() {
		File file = new File(CoreOptions.SCRIPT_DIR + "test.zip");
		assertFalse(this.fileTypeFilter.accept(file));
	}
}
