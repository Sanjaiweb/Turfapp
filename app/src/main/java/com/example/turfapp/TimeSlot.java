package com.example.turfapp;

import java.io.Serializable;

public class TimeSlot implements Serializable {
    private String time; // Assuming time is stored as a String

    public TimeSlot() {
        // Default constructor for Firebase
    }

    public TimeSlot(String time) {
        this.time = time;
    }

    public String getTime() {
        return time;
    }
}
