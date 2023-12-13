package com.testcase.rendering.model;

import java.util.Objects;

public class MapData {
    private String mapUrl;
    private double centerLat;
    private double centerLon;
    private int zoomLevel;


    // Геттеры и сеттеры для всех свойств

    public String getMapUrl() {
        return mapUrl;
    }

    public void setMapUrl(String mapUrl) {
        this.mapUrl = mapUrl;
    }

    public double getCenterLat() {
        return centerLat;
    }

    public void setCenterLat(double centerLat) {
        this.centerLat = centerLat;
    }

    public double getCenterLon() {
        return centerLon;
    }

    public void setCenterLon(double centerLon) {
        this.centerLon = centerLon;
    }

    public int getZoomLevel() {
        return zoomLevel;
    }

    public void setZoomLevel(int zoomLevel) {
        this.zoomLevel = zoomLevel;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MapData mapData = (MapData) o;
        return Double.compare(mapData.centerLat, centerLat) == 0 && Double.compare(mapData.centerLon, centerLon) == 0 && zoomLevel == mapData.zoomLevel && Objects.equals(mapUrl, mapData.mapUrl);
    }

    @Override
    public int hashCode() {
        return Objects.hash(mapUrl, centerLat, centerLon, zoomLevel);
    }

    @Override
    public String toString() {
        return "MapData{" +
                "mapUrl='" + mapUrl + '\'' +
                ", centerLat=" + centerLat +
                ", centerLon=" + centerLon +
                ", zoomLevel=" + zoomLevel +
                '}';
    }
}


