package com.appsplor.cryptomogul.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.appsplor.cryptomogul.R;
import com.appsplor.cryptomogul.dao.DatabaseHandler;
import com.appsplor.cryptomogul.models.Altfolio;
import com.appsplor.cryptomogul.models.Item;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Shabab on 5/15/2017.
 */

public class MyRecyclerAdapter extends RecyclerView.Adapter<CoinViewHolder> {

    List<Item> list;
    Context context;


    public MyRecyclerAdapter(List< Item> list, Context context)
    {
        this.list = list;
        this.context = context;

    }

    public void updateList(List<Item> list){
        this.list = list;
        notifyDataSetChanged();
    }

    public List<Item> getList()
    {
        return this.list;
    }

    @Override
    public CoinViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.album_card, parent, false);
        return new CoinViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(CoinViewHolder holder, int position) {
        final Item map = list.get(position);
        if(map.getCapital() == null)
        {
            holder.capital.setText("Data Not Available");

        }
        else
        {
            holder.capital.setText("$" + map.getCapital().toString());

        }
        holder.name.setText(map.getName() + "");
        holder.favoriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new MaterialDialog.Builder(context)
                        .title(R.string.title)
                        .items(R.array.items)
                        .itemsCallbackSingleChoice(-1, new MaterialDialog.ListCallbackSingleChoice() {
                            @Override
                            public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                                /**
                                 * If you use alwaysCallSingleChoiceCallback(), which is discussed below,
                                 * returning false here won't allow the newly selected radio button to actually be selected.
                                 **/
                                if(which >= 0)
                                {
                                    Altfolio altfolio = new Altfolio(map.getName(), which);
                                    DatabaseHandler databaseHandler = new DatabaseHandler(context);
                                    databaseHandler.addAltfolio(altfolio);
                                    Toast.makeText(context, map.getName() + " saved in Altfolio " + (which + 1), Toast.LENGTH_LONG).show();

                                }
                                return true;
                            }
                        })
                        .positiveText(R.string.choose)
                        .negativeText(R.string.cancel)
                        .show();
            }
        });
    }

    @Override
    public int getItemCount() {

        return list.size();

    }
}
