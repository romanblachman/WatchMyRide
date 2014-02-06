package com.purplerain.watchmyride;

import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;

public class MonitorLocationService extends Service implements LocationListener {

    private static final long MIN_TIME_BETWEEN_UPDATES = 30 * 1000; // Milliseconds
    private static final float MIN_DISTANCE_CHANGE_FOR_UPDATES = 50; // Meters

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Location initialLocation = startGettingLocation();

        if (initialLocation != null) {
            reportLocationToServer(initialLocation);
        }

        return START_NOT_STICKY;
    }

    private Location startGettingLocation() {
        Location currentLocation = null;

        try {
            LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
            if (locationManager == null) {
                stopSelf();
                return null;
            }

            boolean isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            if (isGPSEnabled) {
                locationManager.requestLocationUpdates(
                        LocationManager.GPS_PROVIDER,
                        MIN_TIME_BETWEEN_UPDATES,
                        MIN_DISTANCE_CHANGE_FOR_UPDATES,
                        this);

                currentLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            }

            boolean isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            if (isNetworkEnabled) {
                locationManager.requestLocationUpdates(
                        LocationManager.NETWORK_PROVIDER,
                        MIN_TIME_BETWEEN_UPDATES,
                        MIN_DISTANCE_CHANGE_FOR_UPDATES,
                        this);

                if (currentLocation == null) {
                    currentLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return currentLocation;
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("The service should be started, not bound.");
    }

    @Override
    public void onLocationChanged(Location location) {
        reportLocationToServer(location);
    }

    private void reportLocationToServer(Location location) {
        // TODO: Send the location to the server.
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {
        // Don't care. Just use whatever you've got.
    }

    @Override
    public void onProviderEnabled(String s) {
        // Don't care. Just use whatever you've got.
    }

    @Override
    public void onProviderDisabled(String s) {
        // Don't care. Just use whatever you've got.
    }
}
