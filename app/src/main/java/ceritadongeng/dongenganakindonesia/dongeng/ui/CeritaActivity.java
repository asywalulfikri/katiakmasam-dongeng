package ceritadongeng.dongenganakindonesia.dongeng.ui;

/**
 * Created by asywalulfikri on 10/5/16.
 */

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.lapism.searchview.SearchAdapter;
import com.lapism.searchview.SearchHistoryTable;
import com.lapism.searchview.SearchItem;
import com.lapism.searchview.SearchView;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import ceritadongeng.dongenganakindonesia.dongeng.R;
import ceritadongeng.dongenganakindonesia.dongeng.base.BaseActivity;
import ceritadongeng.dongenganakindonesia.dongeng.database.CacheDb;
import ceritadongeng.dongenganakindonesia.dongeng.database.CeritaDb;
import ceritadongeng.dongenganakindonesia.dongeng.model.Cerita;
import ceritadongeng.dongenganakindonesia.dongeng.model.CeritaWrapper;
import ceritadongeng.dongenganakindonesia.dongeng.ui.adapter.CeritaAdapter2;
import ceritadongeng.dongenganakindonesia.dongeng.util.Debug;
import ceritadongeng.dongenganakindonesia.dongeng.util.Util;
import ceritadongeng.dongenganakindonesia.dongeng.volley.VolleyParsing;
import ceritadongeng.dongenganakindonesia.dongeng.widget.ExpandableHeightListView;
import ceritadongeng.dongenganakindonesia.dongeng.widget.LoadingLayout;

/**
 * Created by Toshiba Asywalul Fikri on 3/12/2016.
 */
public class CeritaActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener  {
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
    private CacheDb mCacheDb;
    private LoadCacheTask mCacheTask;
    private String languange;
    private   String url;

    private SearchHistoryTable mHistoryDatabase;
    private List<SearchItem> mSuggestionsList;
    private SearchView mSearchView;

    private int mVersion = SearchView.VERSION_MENU_ITEM;
    private int mTheme = SearchView.THEME_LIGHT;

    private ImageView cari;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cerita);
        ceritaDb = new CeritaDb(getDatabase());
        mCacheDb = new CacheDb(getDatabase());
        initDatabase();

        //<<<-------------------> INISIAL USERID <----------------------->>>>>>
        intent = getIntent();
        type = intent.getStringExtra("type");
        languange = intent.getStringExtra("languange");
        Log.d("bahasan",languange);


        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder()
                .build();
        mAdView.loadAd(adRequest);
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setTitle("Cerita");
        toolbar.setTitleTextColor(Color.WHITE);
        mLoadingLayout = (LoadingLayout) findViewById(R.id.layout_loading);
        swipContainer = (SwipeRefreshLayout) findViewById(R.id.ptr_layout);
        swipContainer.setColorSchemeColors(Color.RED, Color.GREEN, Color.BLUE, Color.CYAN);
        swipContainer.setOnRefreshListener(this);

        mLoadingLayout = (LoadingLayout) findViewById(R.id.layout_loading);

        mCeritaListview = (ExpandableHeightListView) findViewById(R.id.lv_data);

        mCeritaListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                onListItemClick(position);
            }
        });

        mCeritaListview.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE
                        && (mCeritaListview.getLastVisiblePosition() - mCeritaListview.getHeaderViewsCount() -
                        mCeritaListview.getFooterViewsCount()) >= (mAdapter.getCount() - 1)) {

                    loadTask3();
                }
            }


            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });

        setSearchView();

        cari   = (ImageView)findViewById(R.id.cari);
        cari.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSearchView();
            }
        });

    }

    @Override
    public void onPause() {


        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        mCacheDb.reload(getDatabase());
        ceritaDb.reload(getDatabase());

        if (!isListVisible()) {
            (mCacheTask = new LoadCacheTask()).execute();
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.action_settings:
                Intent intent = new Intent(CeritaActivity.this,CeritaBokmarkActivity.class);
                intent.putExtra("type",type);
                intent.putExtra("languange",languange);
                startActivity(intent);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


    public void loadReadLater() {
        swipContainer.setRefreshing(false);

        CeritaWrapper wrapper;

        mGetCache = true;
        wrapper = ceritaDb.getList(type);
        result(wrapper);
    }

    public void result(CeritaWrapper wrapper) {
        if (wrapper != null) {
            if (wrapper.list.size() == 0) {
                showEmpty(getString(R.string.text_no_data));
            } else {
                if (mIsRefresh) {
                    mCeritaList = new ArrayList<Cerita>();
                }

                updateList(wrapper);

            }
        } else {
            showEmpty(getString(R.string.text_no_data));
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


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Debug.i("On Result");

        if (requestCode == REQ_CODE && resultCode == Activity.RESULT_OK) {
            if (data == null || mCeritaList == null || mCeritaList.size() == 0) return;

            int position = data.getIntExtra(Util.getIntentName("position"), 0);
            Cerita wisataa = data.getParcelableExtra(Util.getIntentName("wisata"));
            Debug.i("On Result");

        }
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
        loadTask();

    }

    public void onListItemClick(final int position) {
        final Cerita cerita = mCeritaList.get(position);
        Intent intent = new Intent(getActivity(), Detail_Cerita_Activity.class);
        intent.putExtra(Util.getIntentName("position"), position);
        intent.putExtra(Util.getIntentName("cerita"), cerita);
        intent.putExtra("languange",languange);
        startActivity(intent);
    }


    protected void refresh() {
        mIsRefresh = true;
        mPage = 1;

        loadTask();
    }

    public void loadTask() {
        startCountTime();
        page = 1;
        if(type.equals("terbaru")){
            url = "http://wisatasumbar.esy.es/restful/cerita.php?&languange="+languange+"&&page="+page+"&&count=20";
        }else{
            url = "http://wisatasumbar.esy.es/restful/cerita.php?&languange="+languange+"&&type="+type+"&&page="+page+"&count=20";
        }
        Log.d("urlnya", url);
        StringRequest mRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //System.out.println("response of resend code is:" + response);
                        mTimerPage.cancel();
                        swipContainer.setRefreshing(false);
                        CeritaWrapper wrapper = null;
                        Debug.i("Response " + response);
                        try {
                            VolleyParsing parsing = new VolleyParsing();
                            wrapper = parsing.ceritaParsing(response);

                            if (wrapper != null) {
                                if (wrapper.list.size() == 0) {
                                    showEmpty(getString(R.string.text_no_data));
                                } else {
                                    if (mIsRefresh) {
                                        mCeritaList = new ArrayList<Cerita>();
                                    }

                                    updateList(wrapper);
                                    mCacheDb.update(wrapper.list,type+"-"+languange);
                                }
                            } else {
                                showEmpty(getString(R.string.text_download_failed));
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        mTimerPage.cancel();
                        try {
                            // actionTracking("getHomeList", "homeList", "failed", String.valueOf(totalTimeSec), user);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        swipContainer.setRefreshing(false);
                        VolleyLog.d("Response Error", "Error" + error.getMessage());
                        showEmpty(getString(R.string.text_download_failed));
                        errorHandle(error);
                    }
                });

        mRequest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(mRequest);
    }


    public class LoadCacheTask extends AsyncTask<URL, Integer, Long> {
        CeritaWrapper wrapper;

        protected void onCancelled() {
        }

        protected void onPreExecute() {
            // showLoading();
        }

        protected Long doInBackground(URL... urls) {
            long result = 0;

            try {
                //Debug.i("Checking cache " + "home-" + mConvObject);
                wrapper = mCacheDb.getListCerita(type+"-"+languange);
                Log.d("isinya",String.valueOf(wrapper));
                result 	= 1;
            } catch (Exception e) {
                e.printStackTrace();
            }

            return result;
        }

        protected void onProgressUpdate(Integer... progress) {
        }

        protected void onPostExecute(Long result) {
            if (wrapper != null) {
                if (wrapper.list.size() > 0) {
                    //doneLoading();

                    mCeritaList = new ArrayList<Cerita>();

                    updateList(wrapper);

                    //showToast("From db");
                } else {
                    Debug.i("Cache not found");
                }
            } else {
                Debug.i("Cache not found");
            }

            onRefresh();
        }
    }


    public void loadTask3() {
        startCountTime();
        page += 1;
        if(type.equals("terbaru")){
            url = "http://wisatasumbar.esy.es/restful/cerita.php?&languange="+languange+"&&page="+page+"&&count=20";
        }else{
            url = "http://wisatasumbar.esy.es/restful/cerita.php?&languange="+languange+"&&type="+type+"&&page="+page+"&count=20";
        }
        Log.d("urlnya", url);
        StringRequest mRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //System.out.println("response of resend code is:" + response);
                        mTimerPage.cancel();
                        swipContainer.setRefreshing(false);
                        CeritaWrapper wrapper = null;
                        Debug.i("Response " + response);
                        try {
                            VolleyParsing parsing = new VolleyParsing();
                            wrapper = parsing.ceritaParsing(response);

                            if (wrapper != null) {
                                if (wrapper.list.size() == 0) {
                                    showEmpty(getString(R.string.text_no_data));
                                } else {
                                    if (mIsRefresh) {
                                        mCeritaList = new ArrayList<Cerita>();
                                    }
                                    updateList(wrapper);
                                }
                            } else {
                                showEmpty(getString(R.string.text_download_failed));
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        mTimerPage.cancel();
                        try {
                            // actionTracking("getHomeList", "homeList", "failed", String.valueOf(totalTimeSec), user);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        swipContainer.setRefreshing(false);
                        VolleyLog.d("Response Error", "Error" + error.getMessage());
                        showEmpty(getString(R.string.text_download_failed));
                        errorHandle(error);
                    }
                });

        mRequest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(mRequest);
    }

    private void showSearchView() {
        mSuggestionsList.clear();
        mSuggestionsList.addAll(mHistoryDatabase.getAllItems(0));
        mSearchView.open(true);
    }

    public void setSearchView() {
        mHistoryDatabase = new SearchHistoryTable(this);
        mSuggestionsList = new ArrayList<>();

        mSearchView = (SearchView) findViewById(R.id.search_view);
        mSearchView.setVersion(mVersion);
        mSearchView.setTheme(mTheme, true);
        mSearchView.setDivider(false);
        mSearchView.setHint("Search");
        mSearchView.setHint("Cari Cerita");
        mSearchView.setTextSize(12);
        mSearchView.setVoice(true);
        mSearchView.setVoiceText("Voice");
        mSearchView.setAnimationDuration(360);
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                mSearchView.close(false);
                mHistoryDatabase.addItem(new SearchItem(query));
                Intent intent = new Intent(getActivity(), SearchListActivity.class);
                intent.putExtra("keyword", query);
                startActivity(intent);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        List<SearchItem> mResultsList = new ArrayList<>();
        SearchAdapter mSearchAdapter = new SearchAdapter(this, mResultsList);
        mSearchAdapter.setOnItemClickListener(new SearchAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                mSearchView.close(false);
                TextView textView = (TextView) view.findViewById(R.id.textView_item_text);
                String text = textView.getText().toString();
//                mHistoryDatabase.addItem(new SearchItem(text));
                Intent intent = new Intent(getActivity(), SearchListActivity.class);
                intent.putExtra("keyword", text);
                startActivity(intent);
            }
        });

        mSearchView.setAdapter(mSearchAdapter);
    }


}