package main.com.example.utility;

public class CoreOptions {
	public final static String TEST_SCRIPT_DIR = "C:\\Users\\User\\PycharmProjects\\MobileEvernoteTest";
	public final static String ANDROID_HOME = System.getenv("ANDROID_HOME");
	public final static String ADB = ANDROID_HOME + "\\platform-tools\\adb.exe";
	public final static String PYTHON_HOME = System.getenv("PYTHON_HOME");
	public final static String PYTHON = PYTHON_HOME + "\\python.exe";
	public final static String UPLOAD_DIRECTORY = "D:\\Thesis\\UploadSpace";
	public final static int INTERVAL_SPACE_NUM = 4;
	public final static String TAG_MOBILE = "mobile";
	public final static String TAG_WEAR = "wear";
	public final static String TAG_PAIR_DEVICE = "pair_device";
	public final static String LOG_PATH = "D:\\Thesis\\UploadSpace\\Log.txt";
	public final static String SCRIPT_DIR = "D:\\Thesis\\Script";	
	public final static String APK_INFO_GETTER_DIR = SCRIPT_DIR + "\\ApkInfoGetter.py";
	public final static String TURN_OFF_BLUETOOTH_DIR = SCRIPT_DIR + "\\turnOffBluetooth.py";
	public final static String TURN_ON_BLUETOOTH_DIR = SCRIPT_DIR + "\\turnOnBluetooth.py";
	public final static String CLEAR_GMS_DIR = SCRIPT_DIR + "\\clearGms.py";
	public final static String INSTALL_APK_DIR = SCRIPT_DIR + "\\installApk.py";
	public final static String UNINSTALL_APK_DIR = SCRIPT_DIR + "\\uninstallApk.py";
	public final static String AAPT = ANDROID_HOME + "\\build-tools\\22.0.0\\aapt";
	public final static String TAG_APK_PATH = "apk_path";
	public final static String TAG_APK_PACKAGE = "package";
	public final static String LAUNCH_APK_DIR = SCRIPT_DIR + "\\launchApk.py";
	public final static String TAG_APK_LAUNCHABLE_ACTIVITY = "launchable-activity";
}
