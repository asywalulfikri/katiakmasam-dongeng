package ceritadongeng.dongenganakindonesia.dongeng.ui;

import android.os.Bundle;

import java.util.ArrayList;

import ceritadongeng.dongenganakindonesia.dongeng.R;
import ceritadongeng.dongenganakindonesia.dongeng.base.BaseActivity;
import ceritadongeng.dongenganakindonesia.dongeng.database.CacheDb;
import ceritadongeng.dongenganakindonesia.dongeng.model.Cerita;
import ceritadongeng.dongenganakindonesia.dongeng.util.Util;

public class SplashScreen extends BaseActivity {

    private ArrayList<Cerita> mQuiz = new ArrayList<Cerita>();
    private CacheDb mCacheDb;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);

        mCacheDb = new CacheDb(getDatabase());

        Util.createAppDir();
        initDatabase();
        doBindService();


      /*  new CountDownTimer(1000,100) {
            public void onTick(long millisUntilFinished) {}

            public void onFinish() {
                getConstantConnection();
            }
        }.start();*/
    }

    @Override
    protected void onResume() {
        super.onResume();
       // mCacheDb.reload(getDatabase());
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        doUnbindService();
    }

}
