package com.example.duroutes;

import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.ToggleButton;
import android.widget.Toolbar;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ToggleButton drawRouteButton;
    private boolean drawRouteButtonState;  //Captures state of drawRouteButton
    private EditText routeName;
    private Button saveButton;
    private Polyline newLine;

    private FirebaseDatabase routesDB;
    private DatabaseReference routesReference;
    private Marker currentMarker;
    private LatLng cameraCenter;

    private Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a currentMarker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        toolbar = (Toolbar)findViewById(R.id.toolbar);

        String[] data = {"one", "two", "three", "four", "five"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, data);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner spinner = (Spinner)findViewById(R.id.spinner2);
        spinner.setAdapter(adapter);

        //Initialize interface
        drawRouteButton = (ToggleButton)findViewById(R.id.drawRouteButton);
        drawRouteButton.setChecked(false);
        drawRouteButtonState = false;
        routeName = (EditText)findViewById(R.id.routeName);
        saveButton = (Button)findViewById(R.id.saveButton);

        // CONNECT WITH FIREBASE
        routesDB = FirebaseDatabase.getInstance();
        routesReference = routesDB.getReference().child("routes");

        //Initialize currentMarker
        currentMarker = null;
        cameraCenter = mMap.getCameraPosition().target;


        //Interface listeners
        drawRouteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (drawRouteButtonState) {
                    drawRouteButtonState = false;
                } else {
                    newLine = mMap.addPolyline(new PolylineOptions()
                    .width(5)
                    .color(Color.RED));
                    drawRouteButtonState = true;
                }
            }
        });

        routesReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Route route = dataSnapshot.getValue(Route.class);
                mMap.addPolyline(new PolylineOptions().width(15).color(Color.BLUE))
                    .setPoints(route.latLngList());
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //создали роут как объект

                if (drawRouteButtonState) {
                    Route route = new Route(routeName.getText().toString(), newLine.getPoints());
                    //чтобы публиковать в базу
                    routesReference.push().setValue(route);  //push создаёт id


                }
            }
        });


        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                if (drawRouteButtonState) {
                    currentMarker = mMap.addMarker(new MarkerOptions().position(cameraCenter));
                    List<LatLng> pointsList = newLine.getPoints();
                    pointsList.add(currentMarker.getPosition());
                    newLine.setPoints(pointsList);
                }
            }
        });

        mMap.setOnCameraMoveListener(new GoogleMap.OnCameraMoveListener() {
            @Override
            public void onCameraMove() {
                cameraCenter = mMap.getCameraPosition().target;
                if (drawRouteButtonState && newLine.getPoints().size() != 0) changeLastPointInPolyline(newLine, cameraCenter);
            }
        });

    }

    private void changeLastPointInPolyline(Polyline polyLine, LatLng point) {
        List<LatLng> points = polyLine.getPoints();
        points.set(points.size()-1, point);
        polyLine.setPoints(points);
    }

}
