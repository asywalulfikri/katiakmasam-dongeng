package ceritadongeng.dongenganakindonesia.dongeng.ui;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.squareup.picasso.Picasso;

import ceritadongeng.dongenganakindonesia.dongeng.R;
import ceritadongeng.dongenganakindonesia.dongeng.base.BaseActivity;
import ceritadongeng.dongenganakindonesia.dongeng.database.CeritaDb;
import ceritadongeng.dongenganakindonesia.dongeng.model.Cerita;
import ceritadongeng.dongenganakindonesia.dongeng.ui.adapter.CustomArrayAdapter;
import ceritadongeng.dongenganakindonesia.dongeng.util.Util;

/**
 * Created by asywalulfikri on 12/12/16.
 */

public class Detail_Cerita_Activity extends BaseActivity {

    private Cerita mCerita;
    private int mPosition = 0;
    private TextView title,content;
    private ImageView imageView;
    private Toolbar toolbar;
    private FloatingActionButton floatingActionButton;
    private int mCurrentGrouping = 0;
    private CeritaDb ceritaDb;
    private Button sumber;
    private AdView mAdView,mAdView2;
    private String languange;
    private Intent intent;
    CollapsingToolbarLayout mCTollbar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activit_cerita_detail);

        title     = (TextView)findViewById(R.id.tv_title);
        content   = (TextView)findViewById(R.id.tv_content);
        imageView = (ImageView)findViewById(R.id.iv_cerita);



        floatingActionButton = (FloatingActionButton)findViewById(R.id.fab);

        mCTollbar   = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);

        sumber    = (Button)findViewById(R.id.btn_sumber);
        intent = getIntent();
        enableDatabase();
        initDatabase();
        ceritaDb = new CeritaDb(getDatabase());
        Bundle bundle = getIntent().getExtras();
        mCerita = bundle.getParcelable(Util.getIntentName("cerita"));
        mPosition = bundle.getInt(Util.getIntentName("position"));

        mAdView = (AdView)findViewById(R.id.adView);
        mAdView2 = (AdView)findViewById(R.id.adView2);
        AdRequest adRequest = new AdRequest.Builder()
                .build();
        mAdView.loadAd(adRequest);
        mAdView2.loadAd(adRequest);


        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showdialog();
            }
        });

        sumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(Intent.ACTION_VIEW).setData(Uri.parse(mCerita.sumber));
                startActivity(intent);
            }
        });

        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        updateview();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public  void updateview(){

        toolbar.setTitle(mCerita.title);
        mCTollbar.setTitle(mCerita.title);
        title.setText(mCerita.title);
        content.setText(mCerita.content);

        Picasso.with(this) //
                .load((mCerita.image1.equals("")) ? null :mCerita.image1) //
                .placeholder(R.drawable.no_image) //
                .error(R.drawable.no_image)
                .into(imageView);


    }


    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        ceritaDb.reload(getDatabase());
    }


    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra(Util.getIntentName("cerita"), mCerita);
        intent.putExtra(Util.getIntentName("position"), mPosition);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }


    public void showdialog(){

        String [] more = {getString(R.string.change_font_size),getString(R.string.save_to_bookmark)};

        CustomArrayAdapter adapter = new CustomArrayAdapter(getActivity(), R.layout.list_item_adapter, more);

        final android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getActivity());
        builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                if (arg1 == 0) {
                    showSize();
                    builder.create().dismiss();
                } else if (arg1 == 1) {
                    bookmark();
                }
            }
        });

        builder.create().show();
    }


    public void showSize(){

        String[]size = {getString(R.string.size12),getString(R.string.size14),getString(R.string.size16),getString(R.string.size18)};
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setSingleChoiceItems(size, mCurrentGrouping, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                String optionMenu = String.valueOf(which);
                if (optionMenu.equals("0")) {
                    content.setTextSize(12);
                    mCurrentGrouping = 0;
                    dialog.dismiss();


                } else if (optionMenu.equals("1")) {
                    content.setTextSize(14);
                    mCurrentGrouping = 1;
                    dialog.dismiss();

                } else if (optionMenu.equals("2")) {
                    content.setTextSize(16);
                    mCurrentGrouping = 2;
                    dialog.dismiss();

                } else {
                    content.setTextSize(18);
                    mCurrentGrouping = 3;
                    dialog.dismiss();

                }
                dialog.dismiss();

            }
        });
        builder.create().show();
    }


    public void bookmark(){


        if (ceritaDb.exist(mCerita.id)) {
            ceritaDb.delete(mCerita);
            Toast.makeText(Detail_Cerita_Activity.this,getString(R.string.text_deleted),Toast.LENGTH_SHORT).show();
        } else {
            ceritaDb.update(mCerita,"bookmark-"+mCerita.type+"-"+mCerita.languange);
            Toast.makeText(Detail_Cerita_Activity.this,getString(R.string.text_save),Toast.LENGTH_SHORT).show();
        }

    }

}
