package dev.afnan.builders_hub.Models;

import android.util.Log;

public class Order {

    private String orderId;
    private String workerType;
    private String totalWorkers;
    private String totalDays;
    private String address;
    private String date;
    private String status;


    public Order() {

    }

    public Order(String orderId, String workerType, String totalWorkers, String totalDays, String address, String date) {
        this.orderId = orderId;
        this.workerType = workerType;
        this.totalWorkers = totalWorkers;
        this.totalDays = totalDays;
        this.address = address;
        this.date = date;
        this.status = "0";          //default=0,  placed=1,  accepted=2,  cancelled=3
    }

    public String getWorkerType() {
        return workerType;
    }

    public void setWorkerType(String workerType) {
        this.workerType = workerType;
    }

    public String getTotalWorkers() {
        return totalWorkers;
    }

    public void setTotalWorkers(String totalWorkers) {
        this.totalWorkers = totalWorkers;
    }

    public String getTotalDays() {
        return totalDays;
    }

    public void setTotalDays(String totalDays) {
        this.totalDays = totalDays;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String checkStatus() {

        String st = "N/A";

        if (status.equals("0")) {
            st = "Order Placed";
        } else if (status.equals("1")) {
            st = "Pending";
        } else if (status.equals("2")) {
            st = "Accepted";
        } else if (status.equals("3")) {
            st = "cancelled";
        }

        Log.d("statusCheck", "conditions not met , status is: " + st);
        return st;
    }

}
