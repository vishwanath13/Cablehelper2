package com.vishwanathlokare.VendorHelper.models;

import java.util.Comparator;
import java.util.Date;

public class CustHistory {
    Date date;
    int amount;

    public CustHistory(Date date, int amount) {
        this.date = date;
        this.amount = amount;
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
    public static Comparator<CustHistory> CompareDate = new Comparator<CustHistory>() {
        @Override
        public int compare(CustHistory Content, CustHistory t1) {
            return Content.getDate().compareTo(t1.getDate());
        }
    };

}
