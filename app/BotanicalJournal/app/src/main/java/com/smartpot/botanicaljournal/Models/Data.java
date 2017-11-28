package com.smartpot.botanicaljournal.Models;

import java.util.Date;

/**
 * Created by MG on 2017-11-20.
 */

public class Data {
    long date;
    int value;

    Data(){}

    Data(long date, int value){
        this.date =date;
        this.value = value;
    }

    public long getDate() {
        return date;
    }

    public int getValue() {
        return value;
    }
}
