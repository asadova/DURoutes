package com.example.duroutes;

/**
 * Created by Нигина on 01.02.2017.
 */

public class LatLngPoint {

        public LatLngPoint(){}

        public LatLngPoint(double lat, double lng){
            this.lat = lat;
            this.lng = lng;
        }

        private double lat;
        private double lng;

        public double getLat() {
            return lat;
        }

        public void setLat(double lat) {
            this.lat = lat;
        }

        public double getLng() {
            return lng;
        }

        public void setLng(double lng) {
            this.lng = lng;
        }

}
