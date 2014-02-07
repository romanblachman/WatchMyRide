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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.gson.Gson;
import com.purplerain.watchmyride.utils.AppPreferences;
import com.purplerain.watchmyride.utils.DataFetcher;
import com.purplerain.watchmyride.utils.User;

public class MainActivity extends Activity {

    private static final String TAG = "WatchMyRide";

    private static final String SENDER_ID = "";

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

        Log.d("GCM", "Registration start");
        // registration
        Intent registrationIntent = new Intent("com.google.android.c2dm.intent.REGISTER");
        // sets the app name in the intent
        registrationIntent.putExtra("app", PendingIntent.getBroadcast(this, 0, new Intent(), 0));
        registrationIntent.putExtra("sender", SENDER_ID); // replace with your project id
        startService(registrationIntent);

        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        AppPreferences prefs = new AppPreferences(this);
        prefs.putString("DEVICE_REG_ID", null);
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
    public static class PlaceholderFragment extends Fragment  implements View.OnClickListener {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            ((ImageView)rootView.findViewById(R.id.btnLogin)).setOnClickListener(this);
            return rootView;
        }

        @Override
        public void onClick(View view) {
            switch (view.getId())
            {
                case R.id.btnLogin:

                    final String name = ((EditText)getView().findViewById(R.id.txtPhone)).getText().toString();
                    final String password = ((EditText) getView().findViewById(R.id.txtPassword)).getText().toString();
                    final String email = ((EditText) getView().findViewById(R.id.txtEmail)).getText().toString();

                    Thread thread = new Thread(new Runnable(){
                        @Override
                        public void run() {
                            try {
                                DataFetcher dataFetcher = new DataFetcher();
                                final String url = getActivity().getString(R.string.update_user_url);

                                User user = new User();
                                user.setPhone(name);
                                user.setPassword(password);
                                user.setEmail(email);

                                Gson gson = new Gson();
                                String json = gson.toJson(user);

                                String result = dataFetcher.makeJsonServiceCall(url, DataFetcher.POST, json);

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });

                    thread.start();

                    Intent monitorUserIntent = new Intent(getActivity(), MonitorLocationService.class);
                    monitorUserIntent.putExtra(
                            MonitorLocationService.EXTRA_USER_ID,
                            email
                            );
                    getActivity().startService(monitorUserIntent);

                    break;


            }
        }
    }


}
