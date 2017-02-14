package ceritadongeng.dongenganakindonesia.dongeng.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import com.squareup.picasso.Picasso;

import java.net.URL;
import java.util.ArrayList;

import ceritadongeng.dongenganakindonesia.dongeng.R;
import ceritadongeng.dongenganakindonesia.dongeng.base.BaseFragment;
import ceritadongeng.dongenganakindonesia.dongeng.database.CacheDb;
import ceritadongeng.dongenganakindonesia.dongeng.database.CeritaDb;
import ceritadongeng.dongenganakindonesia.dongeng.model.Cerita;
import ceritadongeng.dongenganakindonesia.dongeng.model.CeritaWrapper;
import ceritadongeng.dongenganakindonesia.dongeng.ui.CeritaActivity;
import ceritadongeng.dongenganakindonesia.dongeng.ui.Detail_Cerita_Activity;
import ceritadongeng.dongenganakindonesia.dongeng.ui.adapter.CeritaAdapter;
import ceritadongeng.dongenganakindonesia.dongeng.ui.adapter.CeritaAdapter3;
import ceritadongeng.dongenganakindonesia.dongeng.util.Debug;
import ceritadongeng.dongenganakindonesia.dongeng.util.Util;
import ceritadongeng.dongenganakindonesia.dongeng.volley.VolleyParsing;
import ceritadongeng.dongenganakindonesia.dongeng.widget.LoadingLayout;
import ceritadongeng.dongenganakindonesia.dongeng.widget.PageIndicator;
import it.sephiroth.android.library.widget.HListView;

/**
 * Created by Toshiba Asywalul Fikri on 3/12/2016.
 */
public class CeritaFragmentEnglish extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {

    private int[] mResource = {R.drawable.kancil, R.drawable.kurakura, R.drawable.malinkundang};
    private String[] mResourceTitle = {"SI KANCIL", "KURA KURA", "MALIN KUNDANG"};
    RelativeLayout mLayoutSlider;
    private PageIndicator mIndicator;
    private ViewPager mViewPager;
    private CustomPagerAdapter mCustomPagerAdapter;
    private Handler handler;
    private Runnable animateViewPager;
    private boolean stopSliding = false;
    private static final int ANIM_VIEWPAGER_DELAY = 4000;
    private SwipeRefreshLayout swipContainer;
    private LoadingLayout mLoadingLayout;
    private RecyclerView mCeritaListView;
    private HListView mCeritaListView2;
    private ArrayList<Cerita> mCeritaList = new ArrayList<Cerita>();

    private LoadCacheTask mCacheTask;
    private LoadCacheTask2 mCacheTask2;

    private CacheDb mCacheDb;
    private Button btnDongeng,btnLegenda,btnHikayat,btnCeritarakyat,btnMitos;
    private ImageView btnPopuler,btnTerbaru;
    private int page =1;
    private AdView mAdView;
    private CeritaDb ceritaDb;


    public static CeritaFragmentEnglish newInstance() {
        return new CeritaFragmentEnglish();
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mCacheDb = new CacheDb(getDatabase());
        ceritaDb = new CeritaDb(getDatabase());

        View view = inflater.inflate(R.layout.fragment_cerita, container, false);
        initDatabase();

        mIndicator    = (PageIndicator) view.findViewById(R.id.indicatorHome);
        mViewPager    = (ViewPager) view.findViewById(R.id.pagerBrowseSlider);
        mLayoutSlider = (RelativeLayout) view.findViewById(R.id.rl_home_slider);
        btnPopuler    = (ImageView)view.findViewById(R.id.btn_populer);
        btnTerbaru    = (ImageView) view.findViewById(R.id.btn_terbaru);

        swipContainer     = (SwipeRefreshLayout) view.findViewById(R.id.ptr_layout);
        swipContainer.setColorSchemeColors(Color.RED, Color.GREEN, Color.BLUE, Color.CYAN);
        swipContainer.setOnRefreshListener(this);
        mLoadingLayout    = (LoadingLayout) view.findViewById(R.id.layout_loading);

        mAdView = (AdView)view.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder()
                .build();
        mAdView.loadAd(adRequest);

        btnPopuler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), CeritaActivity.class);
                intent.putExtra("type","populer");
                intent.putExtra("languange","english");
                startActivity(intent);
            }
        });

        btnTerbaru.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), CeritaActivity.class);
                intent.putExtra("type","terbaru");
                intent.putExtra("languange","english");
                startActivity(intent);
            }
        });

        btnDongeng   = (Button)view.findViewById(R.id.btn_dongeng);
        btnDongeng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), CeritaActivity.class);
                intent.putExtra("type","dongeng");
                intent.putExtra("languange","english");
                startActivity(intent);
            }
        });

        btnLegenda   = (Button)view.findViewById(R.id.btn_legenda);
        btnLegenda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), CeritaActivity.class);
                intent.putExtra("type","legenda");
                intent.putExtra("languange","english");
                startActivity(intent);
            }
        });

        btnHikayat   = (Button)view.findViewById(R.id.btn_hikayat);
        btnHikayat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), CeritaActivity.class);
                intent.putExtra("type","hikayat");
                intent.putExtra("languange","english");
                startActivity(intent);
            }
        });

        btnCeritarakyat = (Button)view.findViewById(R.id.btn_ceritarakyat);
        btnCeritarakyat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), CeritaActivity.class);
                intent.putExtra("type","ceritarakyat");
                intent.putExtra("languange","english");
                startActivity(intent);
            }
        });

        btnMitos      = (Button)view.findViewById(R.id.btn_mitos);
        btnMitos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), CeritaActivity.class);
                intent.putExtra("type","ceritalucu");
                intent.putExtra("languange","english");
                startActivity(intent);
            }
        });
        recyclerView(view);

        mLayoutSlider.setFocusable(true);
        mLayoutSlider.setFocusableInTouchMode(true);
        setupSliderHome();
        return view;
    }

    public  void recyclerView(View view){

        mCeritaListView   = (RecyclerView)  view.findViewById(R.id.lv_data);
        mCeritaListView2  = (HListView)  view.findViewById(R.id.lv_data2);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        mCeritaListView.setLayoutManager(linearLayoutManager);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mCeritaListView.setHasFixedSize(true);


    }

    class CustomPagerAdapter extends PagerAdapter {

        Context mContext;
        LayoutInflater mLayoutInflater;

        public CustomPagerAdapter(Context context) {
            mContext = context;
            mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return mResource.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == ((RelativeLayout) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View itemView = mLayoutInflater.inflate(R.layout.home_slider, container, false);

            ImageView ivCoverSlide = (ImageView) itemView.findViewById(R.id.iv_home_slider);
            TextView tvTitle = (TextView) itemView.findViewById(R.id.tv_home_slider);

            Picasso.with(mContext)
                    .load((mResource[position]))
                    .placeholder(R.drawable.no_image)
                    .error(R.drawable.no_image)
                    .into(ivCoverSlide);
            tvTitle.setText(mResourceTitle[position]);

            container.addView(itemView);

            return itemView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((RelativeLayout) object);
        }
    }


    protected void showList() {
        if (mLoadingLayout.getVisibility() == View.VISIBLE) {
            mCeritaListView.setVisibility(View.VISIBLE);
            mCeritaListView2.setVisibility(View.VISIBLE);
            mLoadingLayout.setVisibility(View.GONE);
        }
    }

    protected void showEmpty(String message) {
        if (mLoadingLayout.getVisibility() == View.VISIBLE) {
            mLoadingLayout.hideAndEmpty(message, false);
        }
    }

    protected boolean isListVisible() {
        return (mCeritaListView.getVisibility() == View.VISIBLE) ? true : false;
    }


    @Override
    public void onResume() {
        super.onResume();
        mCacheDb.reload(getDatabase());
        ceritaDb.reload(getDatabase());

        if (!isListVisible()) {
            (mCacheTask = new LoadCacheTask()).execute();
            (mCacheTask2 = new LoadCacheTask2()).execute();
        }
    }

    @Override
    public void onRefresh() {
       loadTask();
        loadTask2();

    }

    @Override
    public void onPause() {

        if (mIsLoading && mCacheTask != null && mCacheTask2 != null) {
            mCacheTask.cancel(true);
            mCacheTask2.cancel(true);
        }

        super.onPause();
    }


    private void runnable(final int size) {
        handler = new Handler();
        animateViewPager = new Runnable() {
            public void run() {
                if (!stopSliding) {
                    if (mViewPager.getCurrentItem() == size - 1) {
                        mViewPager.setCurrentItem(0);
                    } else {
                        mViewPager.setCurrentItem(
                                mViewPager.getCurrentItem() + 1, true);
                    }
                    handler.postDelayed(animateViewPager, ANIM_VIEWPAGER_DELAY);
                }
            }
        };
    }

    private void setupSliderHome() {
        mCustomPagerAdapter = new CustomPagerAdapter(getActivity());

        mViewPager.setAdapter(mCustomPagerAdapter);
        mIndicator.setViewPager(mViewPager);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        runnable(mResource.length);
        handler.postDelayed(animateViewPager,
                ANIM_VIEWPAGER_DELAY);
    }


    private void updateList(final CeritaWrapper wrapper) {
        showList();

        CeritaAdapter mAdapter = new CeritaAdapter(wrapper.list,getActivity());
        mCeritaListView.setAdapter(mAdapter);
        mAdapter.setData(wrapper.list);


        mAdapter.likeQuestion(new CeritaAdapter.LikeQuestion() {

            @Override
            public void OnLikeClickQuestion(View view, int position) {

                // TODO Auto-generated method stub
                Cerita cerita = wrapper.list.get(position);
                Intent intent = new Intent(getActivity(), Detail_Cerita_Activity.class);
                intent.putExtra(Util.getIntentName("position"), position);
                intent.putExtra(Util.getIntentName("cerita"), cerita);
                startActivity(intent);
            }
        });

        mAdapter.moreCerita(new CeritaAdapter.MoreCerita() {
            @Override
            public void OnMoreClickCerita(final  View view, final  int position) {
                Cerita cerita = wrapper.list.get(position);

                if (ceritaDb.exist(cerita.id)) {
                    ceritaDb.delete(cerita);
                    Toast.makeText(getActivity(),getString(R.string.text_deleted),Toast.LENGTH_SHORT).show();
                } else {
                    ceritaDb.update(cerita,"bookmark-"+cerita.type+"-english");
                    Toast.makeText(getActivity(),getString(R.string.text_save),Toast.LENGTH_SHORT).show();
                }
            }
        });

        mAdapter.notifyDataSetChanged();

    }

    private void updateList2(final CeritaWrapper wrapper) {
        showList();

        int i = wrapper.list.size();
        int j = 0;
        do {
            if (j >= i) {
                CeritaAdapter3 mAdapter = new CeritaAdapter3(getActivity());
                mAdapter.notifyDataSetChanged();
                mAdapter.setData(wrapper.list);

                mAdapter.likeQuestion(new CeritaAdapter3.LikeQuestion() {

                    @Override
                    public void OnLikeClickQuestion(View view, final  int position) {

                        // TODO Auto-generated method stub
                        Cerita cerita = wrapper.list.get(position);
                        Intent intent = new Intent(getActivity(), Detail_Cerita_Activity.class);
                        intent.putExtra(Util.getIntentName("position"), position);
                        intent.putExtra(Util.getIntentName("cerita"), cerita);
                        startActivity(intent);
                    }
                });
                mCeritaListView2.setAdapter(mAdapter);
                mAdapter.moreCerita(new CeritaAdapter3.MoreCerita() {
                    @Override
                    public void OnMoreClickCerita(final  View view, final  int position) {
                        Cerita cerita = wrapper.list.get(position);

                        if (ceritaDb.exist(cerita.id)) {
                            ceritaDb.delete(cerita);
                            Toast.makeText(getActivity(),getString(R.string.text_deleted),Toast.LENGTH_SHORT).show();
                        } else {
                            ceritaDb.update(cerita,"bookmark-"+cerita.type+"-english");
                            Toast.makeText(getActivity(),getString(R.string.text_save),Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                mIsRefresh = false;
                return;
            }
            mCeritaList.add((Cerita) wrapper.list.get(j));
            j++;
        } while (true);
    }


    public void loadTask() {
        startCountTime();
        String url = "http://wisatasumbar.esy.es/restful/cerita.php?&type=populer&languange=english"+"&page="+page+"&count=15";


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
                                    mCacheDb.update(wrapper.list,"populer-english");
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




    public void loadTask2() {
        startCountTime();
        String url = "http://wisatasumbar.esy.es/restful/cerita.php?&languange=english&page="+page +"&count=15";
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

                                    updateList2(wrapper);
                                    mCacheDb.update(wrapper.list,"terbaru-english");
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
                wrapper = mCacheDb.getListCerita("populer-english");
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



    public class LoadCacheTask2 extends AsyncTask<URL, Integer, Long> {
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
                wrapper = mCacheDb.getListCerita("terbaru-english");
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

                    updateList2(wrapper);

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

}

