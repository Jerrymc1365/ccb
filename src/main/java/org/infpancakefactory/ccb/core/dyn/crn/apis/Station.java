package org.infpancakefactory.ccb.core.dyn.crn.apis;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class Station {
    private String station;
    private String status;
    private int realIndex;
    private int renderIndex;
    private int color;

    public Station(String station, String status, int realIndex,  int renderIndex) {
        this.station = station;
        this.status = status;
        this.realIndex = realIndex;
        this.renderIndex = renderIndex;
    }
    public Station(String station, String status, int realIndex, int renderIndex, int color) {
        this.station = station;
        this.status = status;
        this.realIndex = realIndex;
        this.renderIndex = renderIndex;
        this.color = color;
    }
    public Station(String station, String status, int realIndex) {
        this.station = station;
        this.status = status;
        this.realIndex = realIndex;
    }
    public Station(String station, String status) {
        this.station = station;
        this.status = status;
    }
    public String getStatus() {
        return this.status;
    }
    @NotNull
    public Integer getColor() {
        return this.color;
    }
    @NotNull
    public Integer getRealIndex() {
        return this.realIndex;
    }
    @NotNull
    public Integer getRenderIndex() {
        return this.renderIndex;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public void setRealIndex(int index) {
        this.realIndex = index;
    }
    public void setRenderIndex(int index) {
        this.renderIndex = index;
    }
    public void setColor(int color) {
        this.color = color;
    }
    public void setStationName(String stationName) {
        this.station = stationName;
    }
    public ArrayList<String> getStationTexts() {
        return Apis.getStationTexts(this.station);
    }
    public ArrayList<String> getGoTos() {
        return Apis.getGoTos(this.station);
    }
}
