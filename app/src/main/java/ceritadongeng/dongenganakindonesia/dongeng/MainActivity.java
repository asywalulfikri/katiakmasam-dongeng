package ceritadongeng.dongenganakindonesia.dongeng;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.lapism.searchview.SearchAdapter;
import com.lapism.searchview.SearchHistoryTable;
import com.lapism.searchview.SearchItem;
import com.lapism.searchview.SearchView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import ceritadongeng.dongenganakindonesia.dongeng.base.BaseActivity;
import ceritadongeng.dongenganakindonesia.dongeng.database.CacheDb;
import ceritadongeng.dongenganakindonesia.dongeng.ui.InterestialLogout;
import ceritadongeng.dongenganakindonesia.dongeng.ui.SearchListActivity;
import ceritadongeng.dongenganakindonesia.dongeng.ui.fragment.CeritaBokmarkFragment;
import ceritadongeng.dongenganakindonesia.dongeng.ui.fragment.CeritaFragment;
import ceritadongeng.dongenganakindonesia.dongeng.ui.fragment.CeritaFragmentEnglish;

public class MainActivity extends BaseActivity {


    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    private NavigationView mNavigationView;
    private View mDrawerHeaderView;
    private Menu mDrawerMenu;
    private CacheDb mCacheDb;

    private SearchHistoryTable mHistoryDatabase;
    private List<SearchItem> mSuggestionsList;
    private SearchView mSearchView;

    private int mVersion = SearchView.VERSION_MENU_ITEM;
    private int mTheme = SearchView.THEME_LIGHT;

    private ImageView cari;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_new);


        setFragment(0);
        updateDrawer();
        initDatabase();
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

    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            View view = inflater.inflate(R.layout.dialog_info, null);

            TextView messageTv = (TextView) view.findViewById(R.id.tv_message);

            messageTv.setText(getActivity()
                    .getString(R.string.text_confirm_signout));

            android.app.AlertDialog.Builder builder = getBuilder(getActivity());

            builder.setView(view)
                    .setNegativeButton(getString(R.string.text_no), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .setPositiveButton(getString(R.string.text_yes),
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(MainActivity.this, InterestialLogout.class);
                                    startActivity(intent);

                                }
                            });

            builder.create().show();
        }
    }


    private void updateDrawer() {
        toolbar           = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Dongeng Anak");

        drawerLayout      = (DrawerLayout)   findViewById(R.id.drawer_layout);
        mNavigationView   = (NavigationView) findViewById(R.id.nav_view);
        mNavigationView.setItemIconTintList(null);
        mDrawerHeaderView = LayoutInflater.from(this).inflate(R.layout.nav_header_main, null);
        mNavigationView.addHeaderView(mDrawerHeaderView);
        mNavigationView.inflateMenu(R.menu.activity_main_drawer);

        ImageView ivFotouser = (ImageView) mDrawerHeaderView.findViewById(R.id.imageView);

        Picasso.with(this)
                .load((R.drawable.ic_launcher))
                .placeholder(R.drawable.no_image)
                .error(R.drawable.no_image)
                .into(ivFotouser);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.setDrawerListener(toggle);
        toggle.syncState();

        toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.setDrawerListener(toggle);
        toggle.syncState();


        mDrawerMenu = mNavigationView.getMenu();
        setMenuItem();

        setupDrawerContent(mNavigationView);



    }

    private void setMenuItem() {
        MenuItem dongeng = mDrawerMenu.findItem(R.id.nav_camera);
    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        selectDrawerItem(menuItem);
                        return true;
                    }
                });
    }



    public void setFragment(int position) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();


        switch (position) {
            case 0:
                CeritaFragment ceritaFragment = new CeritaFragment();
                fragmentTransaction.replace(R.id.fragment, ceritaFragment);
                break;

            case 1:
                CeritaFragmentEnglish ceritaFragmentt = new CeritaFragmentEnglish();
                fragmentTransaction.replace(R.id.fragment, ceritaFragmentt);
                break;


            case 2:
                CeritaBokmarkFragment ceritaFragmenttt = new CeritaBokmarkFragment();
                fragmentTransaction.replace(R.id.fragment, ceritaFragmenttt);
                break;

        }
        fragmentTransaction.commit();
    }

    public void selectDrawerItem(MenuItem menuitem) {
        // Handle navigation view item clicks here.

        switch (menuitem.getItemId()) {

            case R.id.nav_camera:
                setFragment(0);
                break;
            case R.id.nav_gallery:
                setFragment(1);
                break;
            case R.id.nav_bookmark:
                setFragment(2);
                break;
        }
        drawerLayout.closeDrawers();
        menuitem.setChecked(true);
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
