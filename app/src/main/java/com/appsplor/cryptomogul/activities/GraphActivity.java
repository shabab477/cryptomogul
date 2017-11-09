package com.appsplor.cryptomogul.activities;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.appsplor.cryptomogul.R;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GraphActivity extends AppCompatActivity {

    @BindView(R.id.market_cap)
    TextView marketCap;

    @BindView(R.id.price)
    TextView price;

    @BindView(R.id.supply)
    TextView supply;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);
        ButterKnife.bind(this);
        final GraphView graph = (GraphView) findViewById(R.id.graph);

        RequestQueue queue = Volley.newRequestQueue(this);
        Intent intent = getIntent();
        final String name = intent.getStringExtra("coin_name");
        String url = "http://www.coincap.io/history/1day/" + name.toUpperCase();
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);
                    JSONArray priceArray = object.getJSONArray("price");
                    int len = priceArray.length();
                    DataPoint[] dataPoints = new DataPoint[(len <= 40)? len: 40];

                    for(int c = 0; c < 40 && c < len; c++)
                    {
                        JSONArray pr = priceArray.getJSONArray(c);
                        double y = pr.getDouble(1) / 100; //scaling down
                        dataPoints[c] = new DataPoint(c + 1, y);
                    }

                    final LineGraphSeries<DataPoint> series = new LineGraphSeries<>(dataPoints);
                    series.setDrawBackground(true);
                    series.setBackgroundColor(0x8099ccff);
                    graph.getGridLabelRenderer().setVerticalLabelsVisible(false);
                    graph.getGridLabelRenderer().setHorizontalLabelsVisible(false);
                    graph.setTitle(name);
                    graph.getViewport().setScrollable(true);
                    graph.getViewport().setScrollableY(true);
                    graph.addSeries(series);

                    Log.e("json", "completed");

                } catch (JSONException e) {
                    Log.e("error_json", e.toString());
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("error_net", error.toString());
                Toast.makeText(GraphActivity.this, "No internet connection", Toast.LENGTH_LONG).show();
            }
        });

        String btcUrl = "http://www.coincap.io/page/" + name.toUpperCase();
        StringRequest dataRequest = new StringRequest(Request.Method.GET, btcUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);
                    price.setText("PRICE: " + object.getString("btcPrice"));
                    marketCap.setText("MARKET CAP: " + object.getString("btcCap"));
                    supply.setText("SUPPLY: " + object.getString("altCap"));

                } catch (JSONException e) {
                    Log.e("error_parse", e.toString());
                    price.setText("PRICE: " + "Data Not Available");
                    marketCap.setText("MARKET CAP: " + "Data Not Available");
                    supply.setText("SUPPLY: " + "Data Not Available");

                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(GraphActivity.this, "No internet connection", Toast.LENGTH_LONG).show();

            }
        });
        queue.add(request);
        queue.add(dataRequest);
    }
}
