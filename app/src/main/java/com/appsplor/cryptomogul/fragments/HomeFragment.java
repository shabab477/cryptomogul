package com.appsplor.cryptomogul.fragments;


import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.appsplor.cryptomogul.R;
import com.appsplor.cryptomogul.adapter.MyRecyclerAdapter;
import com.appsplor.cryptomogul.models.Item;
import com.appsplor.cryptomogul.utils.Websocket;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import com.github.nkzawa.socketio.client.Socket;

import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    private List<Item> list;
    @BindView(R.id.coin_recycler)
    RecyclerView recyclerView;
    private HashMap<String, Integer> positionMap;
    MyRecyclerAdapter adapter;
    private MaterialDialog dialog;
    @BindView(R.id.search_view)
    SearchView searchView;

    private Emitter.Listener onNewMessage = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            Activity activity = getActivity();
            if(activity != null) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        JSONObject data = (JSONObject) args[0];
                        try {
                            JSONObject message = data.getJSONObject("message");
                            String name = message.getString("coin");
                            int position = positionMap.get(name);
                            Log.e("changed", name + " at " + position);
                            JSONObject full = message.getJSONObject("msg");
                            String str = full.getString("mktcap");
                            if(!str.equals("NaN"))
                            {
                                List<Item> temp = adapter.getList();
                                if(temp == list) {
                                    if (position > 0 && position < list.size()) {
                                        Log.e("updating", name + " at " + position);
                                        list.get(position).setCapital(new BigDecimal(String.format("%.2f", Double.parseDouble(str))));
                                        adapter.notifyItemChanged(position);
                                    }
                                }
                                else
                                {
                                    int sz = temp.size();
                                    for(int c = 0; c < sz; c++)
                                    {
                                        Item item = temp.get(c);
                                        if(item.getName().toUpperCase().equals(name.toUpperCase()))
                                        {
                                            Log.e("updating", name + " at " + c);
                                            item.setCapital(new BigDecimal(String.format("%.2f", Double.parseDouble(str))));
                                            adapter.notifyItemChanged(c);
                                            break;
                                        }
                                    }
                                }
                            }

                        } catch (JSONException | NullPointerException e) {
                            e.printStackTrace();
                        }

                        // add the message to view
                        //Log.e("message_socket", data.toString());
                    }
                });
            }
        }
    };

    private Socket mSocket;
    {
        try {
            mSocket = IO.socket("http://socket.coincap.io");
        } catch (URISyntaxException e) {
            Log.e("error_connect", "socket not found");
        }
    }
    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.home_fragment, container, false);
        ButterKnife.bind(this, v);
        list = new ArrayList<>();
        positionMap = new HashMap<>();

        adapter = new MyRecyclerAdapter(list, getContext());
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        //adapter.notifyDataSetChanged();
        Log.e("here at ", "onCreateView: ");
        mSocket.on("trades", onNewMessage);
        mSocket.connect();

        MyAsyncTask asyncTask = new MyAsyncTask();
        asyncTask.execute();
        dialog = new MaterialDialog.Builder(getContext())
                .title("Loading..")
                .content("Please wait. Check internet connection if taking too long")
                .progress(true, 0)
                .show();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                if(newText.length() != 0)
                {
                    List<Item> modifiedList = new ArrayList<Item>();
                    int sz = list.size();
                    for(int c = 0; c < sz; c++)
                    {
                        Item item = list.get(c);

                        if(item.getName().toUpperCase().contains(newText.toUpperCase())) {
                            modifiedList.add(item);
                            Log.e("here", "here at item");
                        }
                    }
                    adapter.updateList(modifiedList);
                }
                else
                {
                    adapter.updateList(list);
                }

                return true;
            }
        });

        recyclerView.setViewCacheExtension(null);
        return v;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        list.clear();
        adapter.updateList(list);
        adapter.notifyDataSetChanged();
        recyclerView.setLayoutManager(null);
        recyclerView.setAdapter(null);
        Log.e("destroyed", "list destroyer");
    }

    @Override
    public void onStop() {
        super.onStop();
        mSocket.disconnect();
    }

    @Override
    public void onResume() {
        super.onResume();
        mSocket.connect();
    }

    private class MyAsyncTask extends AsyncTask<Void, Void, Void>
    {
        private boolean flag = false;

        @Override
        protected Void doInBackground(Void... params) {
            Log.e("starting_", "async");
            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .url("http://www.coincap.io/front")
                    .build();
            Response responses = null;
            try {
                responses = client.newCall(request).execute();
                String jsonData = responses.body().string();
                list.clear();
                JSONArray jArray = new JSONArray(jsonData);
                for(int c = 0; c < jArray.length(); c++)
                {
                    JSONObject obj = jArray.getJSONObject(c);
                    BigDecimal decimal = null;
                    if(!obj.getString("mktcap").equals("NaN"))
                    {
                        decimal = new BigDecimal(String.format("%.2f", obj.getDouble("mktcap")));
                    }
                    Item it = new Item(c, obj.getString("short"), obj.getString("long"), decimal);
                    list.add(it);
                    positionMap.put(it.getName(), c);

                }

            } catch (IOException | JSONException  | NumberFormatException e) {
                e.printStackTrace();
                Log.e("add", e.toString());
                flag = true;

            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if(!flag)
                dialog.dismiss();
            adapter.notifyDataSetChanged();

        }
    }

}
