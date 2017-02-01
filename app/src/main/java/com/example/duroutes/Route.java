package com.example.duroutes;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Нигина on 30.01.2017.
 */
public class Route {
    private String routeName;

    private List<LatLngPoint> points;


    public Route() {
    }



    public Route(String routeName, List<LatLng> latLngs){
        this.routeName = routeName;
        this.points = convertLatLngToPoints(latLngs);
    }

    public String getRouteName() {
        return routeName;
    }
    public void setRouteName(String routeName) {
        this.routeName = routeName;
    }

    public List<LatLngPoint> getPoints() {
        return points;
    }

    public void setPoints(List<LatLngPoint> points) {
        this.points = points;
    }

    public List<LatLng> latLngList(){
        return convertPointsToLatLng(points);
    }

    public void changeLatLngList(List<LatLng> latLngs){
        points = convertLatLngToPoints(latLngs);
    }

    private List<LatLngPoint> convertLatLngToPoints(List<LatLng> latLngList){
        List<LatLngPoint> result = new ArrayList<>();
        for (LatLng latLng : latLngList){
            result.add(new LatLngPoint(latLng.latitude, latLng.longitude));
        }
        return result;
    }

    private List<LatLng> convertPointsToLatLng(List<LatLngPoint> points) {
        List<LatLng> result = new ArrayList<>();
        for (LatLngPoint latLng : points){
            result.add(new LatLng(latLng.getLat(), latLng.getLng()));
        }
        return result;
    }

}
