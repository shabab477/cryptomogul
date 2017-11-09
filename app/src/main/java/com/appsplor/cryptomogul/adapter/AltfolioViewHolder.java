package com.appsplor.cryptomogul.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.appsplor.cryptomogul.R;
import com.appsplor.cryptomogul.models.Altfolio;

import net.cachapa.expandablelayout.ExpandableLayout;

import org.w3c.dom.Text;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Shabab on 5/15/2017.
 */

public class AltfolioViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.favorite)
    public Button favoriteButton;

    @BindView(R.id.name)
    public TextView name;

    public AltfolioViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);

    }

}
