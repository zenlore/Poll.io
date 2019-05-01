package cecs343.pollio;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.TextView;


import static android.content.Context.LOCATION_SERVICE;
import static android.support.v4.content.ContextCompat.getSystemService;

public class getGPSCoord implements LocationListener {

    private Context mContext;
    //variable to check if user's netweork is enabled
    boolean isNetworkEnabled = false;
    //variable to check if user's GPS is enabled
    boolean isGPSEnabled = false;

    //Location variable
    Location location;
    double latitude;
    double longitude;
    protected LocationManager locationManager;
    TextView locationText;

    //min distance to update
    private static final long MIN_DIST_FOR_UPDATES = 10;

    //min time between updates (in milliseconds)
    private static final long MIN_TIME_FOR_UPDATES = 60000; //1 min


    /*
    This will be the function we use in other parts of the code
    * GETS the user's GPS location
     */
    public getGPSCoord(){
        getLocation();
    }
    @SuppressLint("MissingPermission")
    public Location getLocation(){
        try{
            locationManager = (LocationManager) mContext.getSystemService(LOCATION_SERVICE);
            //GPS Status
            isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            //Network Status
            isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            //if there is no gps and no network
            if(!isGPSEnabled && !isNetworkEnabled){
                //please turn allow us to use your location

            }
            else{
                //if NETWORK ....
                if(isNetworkEnabled){
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                            MIN_TIME_FOR_UPDATES, MIN_DIST_FOR_UPDATES, this);
                }
                if (locationManager != null){
                    location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    latitude = location.getLatitude();
                    longitude = location.getLongitude();
                }
            }
            //if GPS is enabled
            if (isGPSEnabled){
                if(location == null){
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_FOR_UPDATES,
                            MIN_DIST_FOR_UPDATES, this);
                }
                if(location != null){
                    location =locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    latitude = location.getLatitude();
                    longitude = location.getLongitude();
                }
            }


        }
        catch(Exception e){
            e.printStackTrace();

        }
        //finally get the user's location!
        return location;
    }
    /**
     * Function to get latitude
     * */
    public double getLatitude(){
        latitude = location.getLatitude();
        // return latitude
        return latitude;
    }

    /**
     * Function to get longitude
     * */
    public double getLongitude(){
        longitude = location.getLongitude();
        // return longitude
        return longitude;
    }
    @Override
    public void onLocationChanged(Location location) {
        locationText.setText("Current Location: " + location.getLatitude() + ", " + location.getLongitude());
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }


}
