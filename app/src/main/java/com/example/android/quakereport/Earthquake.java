package com.example.android.quakereport;

public class Earthquake {

    private String location;
    private long time;
    private double magnitude;
    private String URL;



    public Earthquake(double amplitude, String location, long time, String URL){
        this.location = location;
        this.time = time;
        this.magnitude =amplitude;
        this.URL = URL;
    }

    public String getLocation() {
        return location;
    }

    public long getTime() {
        return time;
    }

    public double getMagnitude() {
        return magnitude;
    }

    public String getURL() { return URL; }
}
