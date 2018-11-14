package com.example.id_myani.curehomesolution;

public class History {
    String status;
    String time_stamp;

    public History(){

    }

    public History(String status, String time_stamp) {
        this.status = status;
        this.time_stamp = time_stamp;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTime_stamp() {
        return time_stamp;
    }

    public void setTime_stamp(String time_stamp) {
        this.time_stamp = time_stamp;
    }
}
