package com.appsplor.cryptomogul.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.appsplor.cryptomogul.R;
import com.appsplor.cryptomogul.activities.GraphActivity;
import com.appsplor.cryptomogul.models.Altfolio;
import com.appsplor.cryptomogul.models.Item;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Shabab on 5/15/2017.
 */

public class AltfolioAdapter extends RecyclerView.Adapter<AltfolioViewHolder> {

    List<Altfolio> list;
    Context context;


    public AltfolioAdapter(List<Altfolio> list, Context context)
    {
        this.list = list;
        this.context = context;

    }

    public void updateList(List<Altfolio> list){
        this.list = list;
        notifyDataSetChanged();
    }

    public List<Altfolio> getList()
    {
        return this.list;
    }

    @Override
    public AltfolioViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.altfolio_card, parent, false);
        Log.e("here_at_holder", "creating view");
        return new AltfolioViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(AltfolioViewHolder holder, int position) {
        final Altfolio map = list.get(position);

        holder.name.setText(map.getCoinName() + "");
        holder.favoriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, GraphActivity.class);
                intent.putExtra("coin_name", map.getCoinName());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {

        return list.size();

    }
}
