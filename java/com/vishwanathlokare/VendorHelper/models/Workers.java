package com.vishwanathlokare.VendorHelper.models;

import java.util.Date;

public class Workers {

    String name;
    Long phone;


    public boolean isPresent() {
        return present;
    }

    public void setPresent(boolean present) {
        this.present = present;
    }

    boolean present;
    Integer Count;

    public String getName() {
        return name;
    }

    public Long getPhone() {
        return phone;
    }

    public Integer getCount() {
        return Count;
    }

    public Workers(String name, Long phone, Integer count, boolean present) {
        this.name = name;
        this.phone = phone;
        this.present = present;
        Count = count;
    }


}
