package ceritadongeng.dongenganakindonesia.dongeng.ui.fragment;

/**
 * Created by asywalulfikri on 10/5/16.
 */

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;

import ceritadongeng.dongenganakindonesia.dongeng.R;
import ceritadongeng.dongenganakindonesia.dongeng.base.BaseFragment;
import ceritadongeng.dongenganakindonesia.dongeng.database.CeritaDb;
import ceritadongeng.dongenganakindonesia.dongeng.model.Cerita;
import ceritadongeng.dongenganakindonesia.dongeng.model.CeritaWrapper;
import ceritadongeng.dongenganakindonesia.dongeng.ui.Detail_Cerita_Activity;
import ceritadongeng.dongenganakindonesia.dongeng.ui.adapter.CeritaAdapter2;
import ceritadongeng.dongenganakindonesia.dongeng.util.Util;
import ceritadongeng.dongenganakindonesia.dongeng.widget.ExpandableHeightListView;
import ceritadongeng.dongenganakindonesia.dongeng.widget.LoadingLayout;

/**
 * Created by Toshiba Asywalul Fikri on 3/12/2016.
 */
public class CeritaBokmarkFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {
    // inisial buat user interface
    private ExpandableHeightListView mCeritaListview;
    private LoadingLayout mLoadingLayout;
    private SwipeRefreshLayout swipContainer;
    private View viewFloatingBt;
    private boolean mIsLoading = false;

    // inisial database dan cache

    //inisial adapter
    private CeritaAdapter2 mAdapter;
    private CeritaWrapper wrapper;
    private ArrayList<Cerita> mCeritaList = new ArrayList<Cerita>();
    private int mPage = 1;

    //variabel digunakan
    private static final int REQ_CODE = 1000;
    protected int mListSize = 0;
    protected boolean mIsRefresh = false;

    //shared Preference
    private AdView mAdView;
    private Intent intent;
    private String type;
    private int page = 1;

    private CeritaDb ceritaDb;
    private boolean mGetCache = false;
    private String languange;


    public static CeritaBokmarkFragment newInstance() {
        return new CeritaBokmarkFragment();
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ceritaDb = new CeritaDb(getDatabase());

        View view = inflater.inflate(R.layout.activity_cerita, container, false);
        initDatabase();


        //<<<-------------------> INISIAL USERID <----------------------->>>>>>


        mAdView = (AdView) view.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder()
                .build();
        mAdView.loadAd(adRequest);
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        toolbar.setVisibility(View.GONE);
        toolbar.setTitle("Cerita");
        toolbar.setTitleTextColor(Color.WHITE);
        mLoadingLayout = (LoadingLayout) view.findViewById(R.id.layout_loading);
        swipContainer = (SwipeRefreshLayout) view.findViewById(R.id.ptr_layout);
        swipContainer.setColorSchemeColors(Color.RED, Color.GREEN, Color.BLUE, Color.CYAN);
        swipContainer.setOnRefreshListener(this);

        mLoadingLayout = (LoadingLayout) view.findViewById(R.id.layout_loading);

        mCeritaListview = (ExpandableHeightListView) view.findViewById(R.id.lv_data);

        mCeritaListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                onListItemClick(position);
            }
        });

return view;
    }


    @Override
    public void onPause() {

        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();

        ceritaDb.reload(getDatabase());
        if (!isListVisible()) {
          loadReadLater();
        }



    }




    public void loadReadLater() {
        swipContainer.setRefreshing(false);

        CeritaWrapper wrapper;

        mGetCache = true;
        wrapper = ceritaDb.getListBookmark();
        result(wrapper);
    }

    public void result(CeritaWrapper wrapper) {
        if (wrapper != null) {
            if (wrapper.list.size() == 0) {
                showEmpty(getString(R.string.text_bookmark_empty));
            } else {
                if (mIsRefresh) {
                    mCeritaList = new ArrayList<Cerita>();
                }

                updateList(wrapper);
            }
        } else {
            showEmpty(getString(R.string.text_bookmark_empty));
        }
    }


    protected boolean isListVisible() {
        return (mCeritaListview.getVisibility() == View.VISIBLE) ? true : false;
    }

    protected void showList() {
        if (mLoadingLayout.getVisibility() == View.VISIBLE) {
            mCeritaListview.setVisibility(View.VISIBLE);
            mLoadingLayout.setVisibility(View.GONE);
        }
    }

    protected void showEmpty(String message) {
        if (mLoadingLayout.getVisibility() == View.VISIBLE) {
            mLoadingLayout.hideAndEmpty(message, false);
        }
    }

    protected void scrollMyListViewToBottom(final int sel) {
        mCeritaListview.post(new Runnable() {
            @Override
            public void run() {
                // Select the last row so it will scroll into view...
                mCeritaListview.setSelection(sel);
            }
        });
    }




    private void updateList(CeritaWrapper wrapper) {
        showList();

        int i = wrapper.list.size();
        int j = 0;
        do {
            if (j >= i) {
                mPage = 1 + mPage;
                mListSize = i;
                mAdapter = new CeritaAdapter2(getActivity());
                mAdapter.notifyDataSetChanged();
                mAdapter.setData(mCeritaList);
                mCeritaListview.setAdapter(mAdapter);
                if (mListSize != 0) {
                    scrollMyListViewToBottom(-1 + (mCeritaList.size() - mListSize));
                }
                if (mIsRefresh)
                {
                    mCeritaListview.setAdapter(mAdapter);
                }
                mIsRefresh = false;
                return;
            }
            mCeritaList.add((Cerita) wrapper.list.get(j));
            j++;
        } while (true);
    }


    @Override
    public void onRefresh() {

        mIsRefresh = true;
        page = 1;
        loadReadLater();

    }

    public void onListItemClick(final int position) {
        final Cerita cerita = mCeritaList.get(position);
        Intent intent = new Intent(getActivity(), Detail_Cerita_Activity.class);
        intent.putExtra(Util.getIntentName("position"), position);
        intent.putExtra(Util.getIntentName("cerita"), cerita);
        startActivity(intent);
    }
}