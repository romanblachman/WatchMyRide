package com.purplerain.watchmyride;

import android.app.Activity;
import android.app.Fragment;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.purplerain.watchmyride.utils.AppPreferences;

public class MainActivity extends Activity {

    private static final String TAG = "WatchMyRide";

    private static final String SENDER_ID = "46009935272";

    private GoogleCloudMessaging gcm = null;
    private String registrationID = null;
    private Context context = null;
    private String deviceID = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = getApplicationContext();

        AppPreferences prefs = new AppPreferences(this);

        deviceID = Settings.Secure.getString(getContentResolver(),
                Settings.Secure.ANDROID_ID);

        prefs.putString("DEVICE_ID", deviceID);

        registrationID = prefs.getString("DEVICE_REG_ID");

        if (registrationID == null )
        {
            Log.d("GCM", "Registration start");
            // registration
            Intent registrationIntent = new Intent("com.google.android.c2dm.intent.REGISTER");
            // sets the app name in the intent
            registrationIntent.putExtra("app", PendingIntent.getBroadcast(this, 0, new Intent(), 0));
            registrationIntent.putExtra("sender", "46009935272"); // replace with your project id
            startService(registrationIntent);
        }


        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }
    }

}
