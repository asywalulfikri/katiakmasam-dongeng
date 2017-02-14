package ceritadongeng.dongenganakindonesia.dongeng.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import ceritadongeng.dongenganakindonesia.dongeng.MainActivity;
import ceritadongeng.dongenganakindonesia.dongeng.R;
import ceritadongeng.dongenganakindonesia.dongeng.base.BaseActivity;

/**
 * Created by Asywlul on 09/2/2016.
 */
public class MenuActivity extends BaseActivity {
    private Button mMulaiBtn;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        mMulaiBtn = (Button) findViewById(R.id.btn_mulai);

        mMulaiBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
            }
        });
    }
}
