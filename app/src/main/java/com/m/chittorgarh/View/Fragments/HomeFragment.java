package com.m.chittorgarh.View.Fragments;


import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;

import com.m.chittorgarh.Adaptors.HomeAdapter;
import com.m.chittorgarh.Halper.EndlessScrollListener;
import com.m.chittorgarh.Halper.Sources;
import com.m.chittorgarh.Model.HomeSetter;
import com.m.chittorgarh.R;
import com.m.chittorgarh.View.Activities.Home;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import cn.pedant.SweetAlert.SweetAlertDialog;
import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter;
import jp.wasabeef.recyclerview.animators.FadeInAnimator;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HomeFragment extends Fragment {
    RecyclerView recycler;
    ArrayList<HomeSetter> products;
    View view;
    EndlessScrollListener scrollListener;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (view==null){
            view = inflater.inflate(R.layout.fragment_home, container, false);
            recycler = (RecyclerView) view.findViewById(R.id.recycler);
            GridLayoutManager manager = new GridLayoutManager(getActivity(), 2, GridLayoutManager.VERTICAL, false);
            manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    int mod = position % 6;
                    if (position == 0 || position == 1)
                        return 2;
                    else if (position < 6)
                        return 1;
                    else if (mod == 0 || mod == 1)
                        return 2;
                    else
                        return 1;
                }
            });
            recycler.setLayoutManager(manager);
            recycler.setItemAnimator(new FadeInAnimator());
            makeRequst();
        }
        return view;
    }




    private void makeRequst(){
        products = new ArrayList<>();
        for (int i =0;i<15;i++){
            products.add(new HomeSetter("http://lorempixel.com/200/200/","Product Name"));
        }
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                setupView();
            }
        });
    }

    private void setupView() {
        HomeAdapter adapter = new HomeAdapter(getActivity(),products);
        AlphaInAnimationAdapter alphaAdapter = new AlphaInAnimationAdapter(adapter);
        alphaAdapter.setFirstOnly(true);
        alphaAdapter.setDuration(500);
        alphaAdapter.setInterpolator(new OvershootInterpolator(.5f));
        recycler.setAdapter(adapter);

    }
    private void sendData(Context context, Fragment fragment, String data){
        Bundle bundle = new Bundle();
        bundle.putString("data",data);
        fragment.setArguments(bundle);
        ((Home)context).changeFragment(fragment,"product");
    }

}
