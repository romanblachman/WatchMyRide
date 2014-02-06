package com.purplerain.watchmyride;

import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class WatchRequestsActivity extends Activity {

    public static final String REQUESTS_KEY = "requests";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watch_requests);

        ArrayList<String> requestNumbers = new ArrayList<String>();

        try {
            // TODO: Get JSON with the list of pending requests for the user from the server.
            JSONObject myjson = new JSONObject("{'requests': ['Test Number 1', 'Test 2', 'Last test']}");
            JSONArray valArray = myjson.getJSONArray(REQUESTS_KEY);

            for(int i = 0; i < valArray.length(); i++) {
                requestNumbers.add(String.format(
                        "A user located... somewhere, asked that you watch over his bike. Ring them up at %s!",
                        valArray.getString(i)));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_list_item_1, requestNumbers);
        ListView requestsList = ((ListView)this.findViewById(R.id.watchRequestsList));
        requestsList.setAdapter(adapter);
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

}
