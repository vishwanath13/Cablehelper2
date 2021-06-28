package com.vishwanathlokare.VendorHelper.models;

public class complaint_model {
    String name;
    String complaint;
    public  complaint_model(String nm,String com){
        name = nm;
        complaint = com;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getComplaint() {
        return complaint;
    }

    public void setComplaint(String complaint) {
        this.complaint = complaint;
    }
}
