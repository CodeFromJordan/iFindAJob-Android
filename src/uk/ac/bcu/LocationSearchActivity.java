// Author: Jordan Hancock
// Name: LocationSearchActivity.java
// Last Modified: 12/02/2014
// Purpose: Activity which is used for location search activity page.

package uk.ac.bcu;

import android.app.ListActivity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author jordan
 */
public class LocationSearchActivity extends ListActivity {
    private ArrayList<JSONObject> locations;
    private BroadcastReceiver receiver;
    private static final String LOCATION_FILENAME = "saved_locations.json";
    public static final String LOCATION_SAVED_CLICKED = "location_saved_selected";
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        // Set up super
        super.onCreate(savedInstanceState);
        
        locations = loadLocations(); // Load locations and their queries
        
        // Set up interface
        setContentView(R.layout.search); // Layout
        this.setTitle("Saved Locations"); // Title
        setupControls(); // Controls
        
        // Receive intent broadcast from SearchableActivity
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(SearchableActivity.LOCATION_SEARCH_CLICKED);
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                // Read data from intent extras
                String jsonString = intent.getExtras().getString("location_result");
                String originalQuery = intent.getExtras().getString("location_query");
                try {
                    JSONObject object = new JSONObject(jsonString); // Turn extra intro JSON object
                    object.put("query", originalQuery); // Add query to the JSON object
                    addLocation(object); // Add the location to the list
                } catch (JSONException ex) { }
            }
        };
        registerReceiver(receiver, intentFilter); // Make the receiver usable
        
        updateListView();
    }
    
    // When saved location result is selected
    // Pass it to JobSearchActivity to do search
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        if(position < locations.size()) {
            Intent intent = new Intent(getBaseContext(), JobSearchActivity.class);
            intent.putExtra("location", locations.get(position).toString());
            startActivity(intent);
        }
    }
    
    // Add a location to the global arraylist
    private void addLocation(JSONObject location) {
        locations.add(location);
        saveLocations(locations);
        
        updateListView();
    }
    
    private void updateListView() {
        // Set up for cells
        String[] CELLS = new String[locations.size()];
        for(int i = 0; i < locations.size(); i++) {
            try {
                CELLS[i] = locations.get(i).getString("city") + " - " + locations.get(i).getString("query");
            } catch (JSONException ex) { }
        }
        
         // Set up list
        setListAdapter(new ArrayAdapter<String>(this, 
                R.layout.list_cell,
                R.id.text,
                CELLS));
    }
    
    // Save locations from global arraylist to JSON file
    public void saveLocations(ArrayList<JSONObject> locationsList) {
        try {
            JSONObject listWrapper = new JSONObject();
            JSONArray list = new JSONArray(locations);
            listWrapper.put("locations", list);
            
            String strList = listWrapper.toString();
            
            FileOutputStream outputStream;
            try {
                outputStream = openFileOutput(LOCATION_FILENAME, Context.MODE_PRIVATE);
                outputStream.write(strList.getBytes());
                outputStream.close();
            } catch (Exception e) { }
        } catch (JSONException ex) { }
    }
    
    // Load locations from JSON file to global arraylist
    public ArrayList<JSONObject> loadLocations() {
        ArrayList<JSONObject> locationList = new ArrayList<JSONObject>();
        try {
            StringBuilder strList = new StringBuilder();
            FileInputStream inputStream;
            
            try {
                inputStream = openFileInput(LOCATION_FILENAME);
                
                byte[] buffer = new byte[1024];
                
                while(inputStream.read(buffer) != -1) {
                    strList.append(new String(buffer));
                }
                
                inputStream.close();
            } catch (Exception e) { }
            
            JSONObject listWrapper = new JSONObject(strList.toString());
            JSONArray list = listWrapper.getJSONArray("locations");
            
            for(int i = 0; i < list.length(); i++) {
                locationList.add(list.getJSONObject(i));
            }
        } catch (JSONException ex) { }
        return locationList;
    }
    
    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }
    
    // When Menu button clicked
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }
    
    // When item in menu selected
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent activityToSwitchTo = new Intent();
        switch(item.getItemId())
        {
            case R.id.itemHomeActivity:
                // Set as Main activity Locations
                activityToSwitchTo = new Intent(getBaseContext(), MainActivity.class);
                startActivity(activityToSwitchTo);
                return true;
            case R.id.itemSearchActivity:
                // Set as Search activity
                onSearchRequested(); // Start search
                return true;         
            case R.id.itemSavedJobsActivity:
                // Set as Saved Jobs activity
                activityToSwitchTo = new Intent(getBaseContext(), SavedJobsActivity.class);
                startActivity(activityToSwitchTo);
                return true; 
        }
        return false;
    }
    
    // When hardware key released
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        switch(keyCode)
        {
            case KeyEvent.KEYCODE_SEARCH:
                onSearchRequested();
                return true; 
        }
        return false;
    }
    
    private void setupControls() {
        // NEED TO MAKE LIST ITEMS CLICKABLE
        // They then take pass location ID to JobSearchActivity
        // Which then uses them to perform search and populate its results
        // Which then when clicked take you to job detail page
        
        // Template code for clickable control
        /*
        clickableObject = (Button)findViewById(R.id.object_id);
        clickableObject.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Do task here
            }
        });
        */
    }
}
