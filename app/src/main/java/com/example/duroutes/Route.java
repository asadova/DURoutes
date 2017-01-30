package com.example.duroutes;

import com.google.android.gms.maps.model.Polyline;

/**
 * Created by Нигина on 30.01.2017.
 */
public class Route {
    private String routeName;
    private Polyline newLine;


    public Route() {
    }



    public Route(String routeName, Polyline newLine){
        this.routeName = routeName;
        this.newLine = newLine;
    }

    public String getRouteName() {
        return routeName;
    }
    public void setRouteName(String routeName) {
        this.routeName = routeName;
    }

    public Polyline getNewLine() {  return newLine;  }
    public void setNewLine(Polyline newLine) {  this.newLine = newLine;   }
}
