package com.appsplor.cryptomogul.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.appsplor.cryptomogul.R;
import com.appsplor.cryptomogul.adapter.AltfolioAdapter;
import com.appsplor.cryptomogul.adapter.MyRecyclerAdapter;
import com.appsplor.cryptomogul.dao.DatabaseHandler;
import com.appsplor.cryptomogul.models.Altfolio;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class AltfolioFragment extends Fragment {

    @BindView(R.id.alt_1)
    Button btn1;

    @BindView(R.id.alt_recycler)
    RecyclerView recyclerView;

    List<Altfolio> list;
    AltfolioAdapter adapter;
    @BindView(R.id.alt_2)
    Button btn2;

    @BindView(R.id.alt_3)
    Button btn3;

    public AltfolioFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_altfolio, container, false);
        ButterKnife.bind(this, v);
        btn1.setPressed(true);
        btn1.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.setPressed(true);
                v.performClick();
                btn2.setPressed(false);
                btn3.setPressed(false);
                Log.e("kist", list.size() + "");
                return true;
            }
        });

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("here", "here at 1");

                list.clear();
                DatabaseHandler databaseHandler = new DatabaseHandler(getContext());
                list = databaseHandler.getAltfolio(0);
                adapter.notifyDataSetChanged();
                adapter.updateList(list);
            }
        });

        btn2.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.setPressed(true);
                btn1.setPressed(false);
                btn3.setPressed(false);
                v.performClick();
                return true;
            }
        });

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("here", "here at 2");
                list.clear();
                DatabaseHandler databaseHandler = new DatabaseHandler(getContext());
                list = databaseHandler.getAltfolio(1);
                adapter.notifyDataSetChanged();
                adapter.updateList(list);
            }
        });

        btn3.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.setPressed(true);
                btn2.setPressed(false);
                btn1.setPressed(false);
                v.performClick();

                return true;
            }
        });

        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            list.clear();
            DatabaseHandler databaseHandler = new DatabaseHandler(getContext());
            list = databaseHandler.getAltfolio(2);
            adapter.notifyDataSetChanged();
            adapter.updateList(list);
            Log.e("at_3", list.size() + "");
            }
        });

        DatabaseHandler databaseHandler = new DatabaseHandler(getContext());
        list = databaseHandler.getAltfolio(0);
        adapter = new AltfolioAdapter(list, getContext());
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);


        Log.e("got", list.size() + "");
        adapter.notifyDataSetChanged();
        return v;
    }

}
