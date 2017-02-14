package ceritadongeng.dongenganakindonesia.dongeng.base;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.android.volley.VolleyError;

import java.util.Timer;
import java.util.TimerTask;

public class BaseFragment extends Fragment {

	//private User mUser;
	public Timer mTimerPage;
	public int totalTimeSec = 0;
	protected boolean mIsRefresh = false;
	protected boolean mIsLoading = false;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	//	mUser = getUser();
	}

	public SQLiteDatabase getDatabase() {
		return ((BaseActivity) getActivity()).getDatabase();
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

	
	public void back() {
		((BaseActivity) getActivity()).back();
	}
	public void initDatabase(){
		((BaseActivity) getActivity()).initDatabase();
	}

	public void errorHandle(VolleyError error) {
		((BaseActivity) getActivity()).errorHandle(error);
	}



}
