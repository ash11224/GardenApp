package com.letitgrow.gardenapp;

/**
 * Created by Ashley on 7/5/2015.
 */
public class ZoneDate {
    private long id;
    private String zone;
    private String ffd;
    private String lfd;

    public long getId() {
            return id;
        }

    public void setId(long id) {
            this.id = id;
        }

    public String getZone() {
            return zone;
        }

    public void setZone(String zone) {
            this.zone = zone;
        }

    public String getFFD() {
        return ffd;
    }

    public void setFFD(String zone) {
        this.ffd = ffd;
    }

    public String getLFD() {
        return lfd;
    }

    public void setLFD(String zone) {
        this.lfd = lfd;
    }

}