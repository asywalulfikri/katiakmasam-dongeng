package ceritadongeng.dongenganakindonesia.dongeng.util;

import android.annotation.SuppressLint;

@SuppressLint("SdCardPath")
public class Cons {

	public static final String TAG = "Cerita Anak Indonesia";

	public static final String DBNAME = "cerita.db";
	public static final String DBPATH = "/data/data/ceritadongeng.dongenganakindonesia.dongeng/";
	public static final String APP_DIR = "/.cerita";
	public static final String IMAGE_DIR = APP_DIR + "/thumbs";

	public static final String PACKAGE_PREFIX = "ceritadongeng.dongenganakindonesia.dongeng";

	public static final int DB_VERSION = 3;



	// enable debug
	public static final boolean ENABLE_DEBUG = true;
	public static final String DBVER_KEY = "dbversion";
	public static final String PRIVATE_PREF = "lisa_pref";
	public static final String LASTUPD_KEY = "lastupdate";

	// Http connection timeout
	public static final int HTTP_CONNECTION_TIMEOUT = 30000;

	// socket time out
	public static final int HTTP_SOCKET_TIMEOUT = 100000;

	public static final int GPS_ACCURACY = 500;
	public static final int NETWORK_ACCURACY = 3000;

	public static final int RESULT_CLOSE_ALL = 1001;
}
