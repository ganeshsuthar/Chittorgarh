package com.m.chittorgarh.View.Activities;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.m.chittorgarh.Adaptors.NavAdaptor;
import com.m.chittorgarh.Halper.AnimatedExpandableListView;
import com.m.chittorgarh.Halper.Sources;
import com.m.chittorgarh.Model.NavData;
import com.m.chittorgarh.Model.NevSubData;
import com.m.chittorgarh.R;
import com.m.chittorgarh.View.Fragments.HomeFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Home extends AppCompatActivity {
    Toolbar toolbar;
    DrawerLayout drawer;
    private AnimatedExpandableListView listView;
    ArrayList<NavData> Mainmenu = new ArrayList<>();
    ArrayList<NevSubData> Submenu;
    NavData gru;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        listView = (AnimatedExpandableListView) findViewById(R.id.listView);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.drawer_open, R.string.drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        setTitle("Home");
        makeRequst();
        changeFragment(new HomeFragment(), "home");


        listView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            int previousGroup = -1;

            @Override
            public void onGroupExpand(int groupPosition) {
                if ((previousGroup != -1) && (groupPosition != previousGroup)) {
                    listView.collapseGroup(previousGroup);
                }
                previousGroup = groupPosition;
            }
        });
    }
    public void changeFragment(Fragment targetFragment, String name) {
        if (name != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.drawer_content, targetFragment)
                    .addToBackStack(name)
                    .commit();
            drawer.closeDrawer(Gravity.LEFT);
        } else {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.drawer_content, targetFragment)
                    .commit();
            drawer.closeDrawer(Gravity.LEFT);
        }

    }
    private void makeRequst(){
        final ArrayList<NevSubData> sub = new ArrayList<>();
        OkHttpClient httpClient = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
                .build();
        RequestBody params = new FormBody.Builder().build();
        Request request = new Request.Builder().url(Sources.getNav).post(params).build();
        final Call call = httpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("error",e.getMessage());
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                JSONArray menu;
                JSONArray subMenu;
                try {
                    JSONObject jsonObject = new JSONObject(response.body().string());
                    menu = jsonObject.getJSONArray("menu");
                    subMenu = jsonObject.getJSONArray("sub_menu");
                    for (int i=0;i<menu.length();i++){
                        JSONObject menuObject = menu.getJSONObject(i);
                        String id = menuObject.getString("category_id");
                        gru = new NavData();
                        gru.setId(menuObject.getString("category_id"));
                        gru.setTitle(menuObject.getString("name"));
                        Submenu = new ArrayList<NevSubData>();
                        for (int j=0;j<subMenu.length();j++){
                            JSONObject subObject = subMenu.getJSONObject(j);
                            NevSubData subData = new NevSubData();
                            if (subObject.getString("parent_id").matches(id)){
                                subData.setId(subObject.getString("category_id"));
                                subData.setName(subObject.getString("name"));
                                Log.e("name",subObject.getString("name"));
                                Submenu.add(subData);
                            }
                        }
                        gru.setSubTitile(Submenu);
                        Mainmenu.add(gru);
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            listView.setAdapter(new NavAdaptor(Home.this, Mainmenu));
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    boolean doubleBackToExitPressedOnce = false;
    @Override
    public void onBackPressed() {
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.drawer_content);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (currentFragment instanceof HomeFragment) {
            if (doubleBackToExitPressedOnce) {
                getSupportFragmentManager().popBackStack("home", 0);
                System.exit(0);
                super.onBackPressed();
                return;
            }
            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "Please Click BACK Again To Exit", Toast.LENGTH_SHORT).show();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;
                }
            }, 2000);

           /* if (searchView.isSearchOpen()) {
                searchView.closeSearch();
            } else {}*/
        }
    }
}
