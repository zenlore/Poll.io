package cecs343.pollio;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.*;
import android.os.Bundle;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static android.content.Context.LOCATION_SERVICE;

public class GPSListener implements LocationListener {

    public static final double SEARCH_RADIUS_IN_MILES = 5.0;
    private static GPSListener instance;

    private Context mContext;
    private boolean isNetworkEnabled = false; // variable to check if user's network is enabled
    private boolean isGPSEnabled = false; // variable to check if user's GPS is enabled

    private Location location;// location variable

    private double latitude;
    private double longitude;
    protected LocationManager locationManager;

    private static final long MIN_DIST_FOR_UPDATES = 10; // min distance to update

    private static final long MIN_TIME_FOR_UPDATES = 5000; // min time between updates (in milliseconds)

    private GPSListener(Context mContext) {
        this.mContext = mContext;
        updateLocation();
    }

    public static GPSListener initGPS(Context mContext){
        if(instance == null){
            instance = new GPSListener(mContext);
        }
        return instance;
    }

    public static GPSListener getInstance(){
        return instance;
    }

    @SuppressLint("MissingPermission")
    private Location updateLocation(){
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

        // might need this later:
//        HashMap<String, String> args = new HashMap<>();
//        args.put("currentlat", Double.toString(latitude));
//        args.put("currentlong", Double.toString(longitude));
//        Requestor.postRequest(mContext.getApplicationContext(), "currentloc", FirebaseAuth.getInstance().getCurrentUser(), args);

        return location;
    }

    /**
     * Function to get latitude
     * @return a double representation of latitude
     */
    public double getLatitude(){
        updateLocation();
        return latitude;
    }

    /**
     * Function to get longitude
     * @return a double representation of longitude
     */
    public double getLongitude(){
        updateLocation();
        return longitude;
    }

    public String getCityName(){
        String cityName = null;
        Geocoder gcd = new Geocoder(mContext, Locale.getDefault());
        List<Address> addresses;

        try {
            addresses = gcd.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            if (addresses.size() > 0) {
                cityName = addresses.get(0).getLocality();
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        return cityName;
    }

    @Override
    public void onLocationChanged(Location location) {
        updateLocation();
        Log.i("LATITUDE ", Double.toString(latitude));
        Log.i("LONGITUDE ", Double.toString(longitude));
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

    public static void getPollsNearUser(ArrayList<PollItem> pollList){
        double userLat = instance.getLatitude();
        double userLong = instance.getLongitude();
        Log.i("getPollsNearUser", "==========================================================");
        Log.i("user.x", Double.toString(userLat));
        Log.i("user.y", Double.toString(userLong));
        Log.i("POLLLIST SIZE", Integer.toString(pollList.size()));
        Log.i("SEARCH_RADIUS_IN_MILES", Double.toString(SEARCH_RADIUS_IN_MILES));

        for (int i = 0; i < pollList.size(); i++) {
            double pollLat = pollList.get(i).getLatitude();
            double pollLong = pollList.get(i).getLongitude();
            double distanceBetweenPoints = Math.sqrt(Math.pow(userLat - pollLat, 2) + Math.pow(userLong - pollLong, 2));
            double distanceBetweenPointsInMiles = 69.0 * distanceBetweenPoints;

            Log.i("i", "---------------------------------------------");
            Log.i("Poll", pollList.get(i).getTitle());
            Log.i("p.x", Double.toString(pollLat));
            Log.i("p.y", Double.toString(pollLong));
            Log.i("      distance", Double.toString(distanceBetweenPoints));
            Log.i("distance in mi", Double.toString(distanceBetweenPointsInMiles) + " mi");

            // if the distance between the current location of the user and the poll coordinates is
            // greater than the search radius then remove the poll from the pollList
            if(distanceBetweenPointsInMiles > GPSListener.SEARCH_RADIUS_IN_MILES){
                pollList.remove(i);
                i--;
            }
        }
    }
}
