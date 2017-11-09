package com.appsplor.cryptomogul.models;

import com.orm.dsl.Ignore;

import java.math.BigDecimal;
import java.util.Locale;

/**
 * Created by Shabab on 5/15/2017.
 */

public class Item extends Parent{

    private int id;
    private String name;
    private String imageURL;
    private BigDecimal capital;

    public Item(int id, String name, String imageURL, BigDecimal capital) {
        this.id = id;
        this.name = name;
        this.imageURL = imageURL;
        this.capital = capital;
    }

    @Override
    public String toString() {
        String str = String.format(Locale.ENGLISH, "%s : %.3f", name, capital);
        return str;
    }

    public int getItemId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public BigDecimal getCapital() {
        return capital;
    }

    public void setCapital(BigDecimal capital) {
        this.capital = capital;
    }
}
