package com.vishwanathlokare.VendorHelper.models;

public class Line {
    String line;
    String worker;

    public Line(String line, String worker, Integer amount) {
        this.line = line;
        this.worker = worker;
        this.amount = amount;
    }

    Integer amount;
    public String getLine() {
        return line;
    }

    public void setLine(String line) {
        this.line = line;
    }

    public String getWorker() {
        return worker;
    }

    public void setWorker(String worker) {
        this.worker = worker;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }


}
