package ceritadongeng.dongenganakindonesia.dongeng.base;

import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.android.volley.NetworkResponse;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Timer;
import java.util.TimerTask;

import ceritadongeng.dongenganakindonesia.dongeng.service.ServiceBGM;
import ceritadongeng.dongenganakindonesia.dongeng.util.Cons;
import ceritadongeng.dongenganakindonesia.dongeng.util.Debug;
import ceritadongeng.dongenganakindonesia.dongeng.util.Util;

public class BaseActivity extends AppCompatActivity {
	protected SQLiteDatabase mSqLite;
	protected SharedPreferences mSharedPref;
	private boolean mIsDbOpen = false;
	private boolean mEnableDb = false;

	protected boolean mIsBound = false;
	protected ServiceBGM mServ;

	public Timer mTimerPage;
	public int totalTimeSec = 0;


	protected ServiceConnection Scon =new ServiceConnection(){

		public void onServiceConnected(ComponentName name, IBinder
				binder) {
			mServ = ((ServiceBGM.ServiceBinder)binder).getService();
			mServ.resumeMusic();
		}

		public void onServiceDisconnected(ComponentName name) {
			mServ = null;
		}
	};

	public void doBindService(){
		bindService(new Intent(this,ServiceBGM.class),
				Scon, Context.BIND_AUTO_CREATE);
		mIsBound = true;
	}

	public void doUnbindService()
	{
		if(mIsBound)
		{
			unbindService(Scon);
			mIsBound = false;
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mSharedPref = getSharedPreferences(Cons.PRIVATE_PREF,
				Context.MODE_PRIVATE);
	}

	@Override
	protected void onPause() {
		if (mEnableDb && mIsDbOpen) {
			closeDatabase();
		}

		super.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();

		mSharedPref = getSharedPreferences(Cons.PRIVATE_PREF,
				Context.MODE_PRIVATE);

		if (mEnableDb && !mIsDbOpen) {
			openDatabase();
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			onBackPressed();
			break;
		}

		return super.onOptionsItemSelected(item);
	}

	public void back() {
		setResult(RESULT_OK);
		finish();
	}

	public boolean buildVersion() {
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			return true;
		}else {
			return false;
		}
	}

	public static final int getColorModify(Context context, int id) {
		final int version = Build.VERSION.SDK_INT;
		if (version >= 21) {
			return ContextCompat.getColor(context, id);
		} else {
			return context.getResources().getColor(id);
		}
	}


	public boolean isOnline() {
		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		if (netInfo != null && netInfo.isConnectedOrConnecting()) {
			return true;
		} else {
			return false;
		}
	}

	/*******************************************************************
	 * Till here for about Oauth and SharedPreferences User
	 * 
	 * *****************************************************************
	 */
	
	public SQLiteDatabase getDatabase() {
		return mSqLite;
	}
	
	public SharedPreferences getSharedPreferences() {
		return mSharedPref;
	}
	
	public int getDeviceType() {
		return (Util.isHoneycombTablet(this)) ? 3 : 2;
	}
	
	public String getOS() {
		return Build.VERSION.RELEASE;
	}
	
	public String getLatestUpdate() {
		
		return mSharedPref.getString(Cons.LASTUPD_KEY, "2014-01-29 00:00:00");
	}
	
	public BaseActivity getActivity() {
		return this;
	}

	protected void enableDatabase() {
		mEnableDb = true;

		openDatabase();
	}
	
	public void logout() {
		Editor editor = mSharedPref.edit();
    	
		//editor.putBoolean(Cons.PM_KEEP_LOGIN, 	false);
    
    	editor.commit();    	
	}

	// This for open Koneksi to Database
	private void openDatabase() {
		if (mIsDbOpen) {
			Debug.i(Cons.TAG, "Database already open");
			return;
		}

		String db = Cons.DBPATH + Cons.DBNAME;

		try {
			mSqLite = SQLiteDatabase.openDatabase(db, null,
					SQLiteDatabase.NO_LOCALIZED_COLLATORS);
			mIsDbOpen = mSqLite.isOpen();

			Debug.i(Cons.TAG, "Database open");
		} catch (SQLiteException e) {
			Debug.e(Cons.TAG, "Can not open database " + db, e);
		}
	}


	// This for Close Koneksi to Database
	private void closeDatabase() {
		if (!mIsDbOpen)
			return;

		mSqLite.close();

		mIsDbOpen = false;

		Debug.i(Cons.TAG, "Database closed");
	}

	public void openGPS(Context context) {
		context.startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
	}

	public void errorHandle(VolleyError error) {
		if (error != null) {
			NetworkResponse response = error.networkResponse;

			if (response != null && response.data != null) {
				String json = null;
				json = new String(response.data);
				VolleyLog.e("Response Error", "Error" + json);
			}
		}
	}



	public void startCountTime() {
		totalTimeSec = 0;

		mTimerPage = new Timer();
		mTimerPage.schedule(new TimerTask() {
			public void run() {
				totalTimeSec++;
			}
		}, 1, 1); //execute every 1 seconds
	}


	public AlertDialog.Builder getBuilder(Context context) {
		AlertDialog.Builder builder;

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			builder = new AlertDialog.Builder(context, android.R.style.Theme_Material_Light_Dialog_Alert);
		} else {
			builder = new AlertDialog.Builder(context);
		}

		return builder;
	}

	public void initDatabase() {
		Util.createAppDir();

		String database = Cons.DBPATH + Cons.DBNAME;
		int currVersion	= mSharedPref.getInt(Cons.DBVER_KEY, 0);

		try {
			Debug.i(Cons.TAG, "Current database version is " + String.valueOf(currVersion));

			if (Cons.DB_VERSION > currVersion) {
				File databaseFile  = new File(database);

				if (databaseFile.exists()) {
					Debug.i(Cons.TAG, "Deleting current database " + Cons.DBNAME);

					databaseFile.delete();
				}

				InputStream is	= getResources().getAssets().open(Cons.DBNAME);
				OutputStream os = new FileOutputStream(database);

				byte[] buffer	= new byte[1024];
				int length;

				Debug.i(Cons.TAG, "Start copying new database " + database + " version " + String.valueOf(Cons.DB_VERSION));

				while ((length = is.read(buffer)) > 0) {
					os.write(buffer, 0, length);
				}

				os.flush();
				os.close();
				is.close();

				SharedPreferences.Editor editor = mSharedPref.edit();

				editor.putInt(Cons.DBVER_KEY, Cons.DB_VERSION);
				editor.commit();
			} else {
				if (Cons.ENABLE_DEBUG) {
					InputStream is	= new FileInputStream(database);
					OutputStream os = new FileOutputStream(Util.getAppDir() + "/cerita.db");

					byte[] buffer	= new byte[1024];
					int length;

					Debug.i(Cons.TAG, "[DEVONLY] Copying db " + database + " to " + Util.getAppDir() + "/mari_belajar.db");

					while ((length = is.read(buffer)) > 0) {
						os.write(buffer, 0, length);
					}

					os.flush();
					os.close();
					is.close();
				}
			}
		} catch (SecurityException ex) {
			Debug.e(Cons.TAG, "Failed to delete current database " + Cons.DBNAME, ex);
		} catch (IOException ex) {
			Debug.e(Cons.TAG, "Failed to copy new database " + Cons.DBNAME + " version " + String.valueOf(Cons.DB_VERSION), ex);
		}

		enableDatabase();
	}



}
