package com.bsp.bspregistration;

public class HistoryList {
    private String tempref;
    private String amount;

    public HistoryList(String tempref, String amount) {
        this.tempref = tempref;
        this.amount = amount;
    }

    public String getTempref() {
        return tempref;
    }

    public void setTempref(String tempref) {
        this.tempref = tempref;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
}
