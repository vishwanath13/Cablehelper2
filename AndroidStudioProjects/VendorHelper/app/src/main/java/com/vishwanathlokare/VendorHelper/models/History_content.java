package com.vishwanathlokare.VendorHelper.models;

import java.util.Date;

public class History_content {
    Date date;
    int  amount;
    String name;

    public  History_content( String mname, int mamount ,Date mdate){
        date= mdate;
        amount = mamount;
        name = mname;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}
