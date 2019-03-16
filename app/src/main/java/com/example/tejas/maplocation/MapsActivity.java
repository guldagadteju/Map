package com.example.tejas.maplocation;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private static final int MY_LOCATION_REQUEST = 1001;/*create constant*/
    private GoogleMap mMap;
    private FusedLocationProviderClient mFusedLocationClient;
    private boolean isSelect= false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        // Add a marker in location and move the camera
        getMyLocation();



    }
        private void getMyLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},MY_LOCATION_REQUEST);
        }
        else{
            getLocation();

        }
    }

    @SuppressLint("MissingPermission")
    private void getLocation() {
        mFusedLocationClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location!=null){
                    LatLng latLng= new LatLng(location.getLatitude(),location.getLongitude());
                    addMarker(latLng);
                    addCircle(latLng);
//                    addPolyline();
                    addPolygoan();
                }
                }
        });
    }

    private void addPolygoan() {
        LatLng p1 = new LatLng(18.566588, 73.911359);
        LatLng p2 = new LatLng(18.5679951, 73.910683);
        LatLng p3 = new LatLng(18.568856, 73.910814);
        LatLng p4 = new LatLng(18.569025, 73.911530);

        List<LatLng> points = new ArrayList<>();
        points.add(p1);
        points.add(p2);
        points.add(p3);
        points.add(p4);

        PolygonOptions polygonOptions = new PolygonOptions();
        polygonOptions.addAll(points).strokeColor(Color.BLUE).strokeWidth(10).fillColor(Color.MAGENTA);
        //add on map
//        mMap.addPolygon(polygonOptions);
         Polygon polygon = mMap.addPolygon(polygonOptions);
         polygon.setClickable(true);
         mMap.setOnPolygonClickListener(new GoogleMap.OnPolygonClickListener() {
             @Override
             public void onPolygonClick(Polygon polygon) {
                 if(isSelect){
                     isSelect=false;
                     polygon.setFillColor(Color.RED);
                 }
                 else{
                     isSelect=true;
                     polygon.setFillColor(Color.GREEN);
                 }

             }
         });


        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (LatLng point : points) {
            builder.include(point);
        }
        LatLngBounds bounds = builder.build();
        int padding = 20;//offset from edges of the map in pixels
        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
        mMap.animateCamera(cu);
    }

  /*  private void addPolyline() {
//        for polylines
        LatLng p1= new LatLng(18.566588, 73.911359);
      LatLng p2= new LatLng(18.5679951, 73.910683);
      LatLng p3=new LatLng(18.568856, 73.910814);
      LatLng p4=new LatLng(18.569025, 73.911530);

      List<LatLng>points= new ArrayList<>();
      points.add(p1);
      points.add(p2);
      points.add(p3);
      points.add(p4);

      PolylineOptions polylineOptions= new PolylineOptions();
      polylineOptions.addAll(points).color(Color.BLUE).width(20);
     //add on map
      mMap.addPolyline(polylineOptions);

      LatLngBounds.Builder builder= new LatLngBounds.Builder();
      for(LatLng point:points){
          builder.include(point);
      }
      LatLngBounds bounds =builder.build();
      int padding = 20;//offset from edges of the map in pixels
      CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds,padding);
      mMap.animateCamera(cu);

  }*/
//      make bounds to zoom map



    private void addCircle(LatLng latLng) {
        CircleOptions circleOptions =new CircleOptions().center(latLng).fillColor(Color.GRAY).strokeColor(Color.BLUE).radius(50);
        mMap.addCircle(circleOptions);
    }

    private void addMarker(LatLng latLng) {
        mMap.addMarker(new MarkerOptions().position(latLng).title("current location"));
        CameraUpdate location =CameraUpdateFactory.newLatLngZoom(latLng,15);
        mMap.animateCamera(location);
    }
//   check permissions
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode==MY_LOCATION_REQUEST){
            if (grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                getLocation();
                }
                else {getMyLocation();}
        }
    }

}
