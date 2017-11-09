package com.appsplor.cryptomogul.models;

import com.orm.SugarRecord;
import com.orm.dsl.Unique;

/**
 * Created by Shabab on 5/19/2017.
 */

public class Altfolio extends Parent {
    @Unique
    String coinName;
    int number;

    public Altfolio(String coinName, int number) {
        this.coinName = coinName;
        this.number = number;
    }

    public Altfolio()
    {

    }

    public String getCoinName() {
        return coinName;
    }

    public void setCoinName(String coinName) {
        this.coinName = coinName;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }
}
