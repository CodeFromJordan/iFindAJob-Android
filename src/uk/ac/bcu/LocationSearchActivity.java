// Author: Jordan Hancock
// Name: LocationSearchActivity.java
// Last Modified: 20/02/2014
// Purpose: Activity which is used for location search activity page.
package uk.ac.bcu;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONException;
import org.json.JSONObject;
import uk.ac.db.DatabaseManager;
import uk.ac.model.Location;

public class LocationSearchActivity extends ListActivity {

    List<Location> locations;
    List<String> locationsText;
    private BroadcastReceiver receiver;
    public static final String LOCATION_SAVED_CLICKED = "location_saved_selected";

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        // Set up super
        super.onCreate(savedInstanceState);

        setupListView(); // Get data for cells

        // Set up interface
        setContentView(R.layout.search); // Layout
        this.setTitle("Saved Locations"); // Title

        // Receive intent broadcast from SearchableActivity
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(SearchableActivity.LOCATION_SEARCH_CLICKED);
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                // Read data from intent extras
                JSONObject clickedResult; // Store JSONObject of clicked result
                String result = intent.getExtras().getString("location_result"); // Stores string of clicked result
                String query = intent.getExtras().getString("location_query"); // Get string from intent
                try {
                    clickedResult = new JSONObject(result); // Converts string to JSONObject for clicked result

                    // Store into a new location object
                    Location newLocation = new Location(clickedResult, query);
                    // Add and save location
                    addLocation(newLocation);
                } catch (JSONException ex) {
                }
            }
        };
        registerReceiver(receiver, intentFilter); // Make the receiver usable

        // Set-up long click listener for ListView activity
        ListView lv = getListView();
        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1, final int row, long arg3) {
                // Ask if user wants to delete with Dialog box
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(LocationSearchActivity.this);
                alertDialog.setTitle("Delete Location");
                alertDialog.setMessage("Do you want to delete the selected location?");

                // User clicks Yes
                alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        deleteLocation(locations.get(row), row);
                        Toast.makeText(getApplicationContext(), "Location deleted.", Toast.LENGTH_SHORT).show();
                    }
                });

                // User clicks No
                alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                alertDialog.show();
                return true;
            }
        });

        onSearchRequested(); // Open search bar as soon as activity displayed
        updateListView(); // Refresh list view after adding new item
    }

    // Reads Locations from database and populates cells
    private void setupListView() {
        // Wipe already existing locations list
        locations = new ArrayList<Location>();

        // Read location objects from database
        locations = DatabaseManager.getInstance().getAllLocations();
        locationsText = new ArrayList<String>();

        // Loop through all locations and get strings to write to cells
        for (Location loc : locations) {
            locationsText.add(loc.getCity() + " - " + loc.getQuery());
        }

        updateListView();
    }

    // When saved location result is selected
    // Pass it to JobSearchActivity to do search
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        if (position < locations.size()) {
            Intent intent = new Intent(getBaseContext(), JobSearchActivity.class);
            intent.putExtra("location_id", locations.get(position).getUsedID());
            intent.putExtra("location_query", locations.get(position).getQuery());
            startActivity(intent);
        }
    }

    // Add a location to the global arraylist
    private void addLocation(Location location) {
        locations.add(location); // Add new location object to list
        saveLocation(location); // Save location to database

        setupListView(); // Refresh list view
    }

    // Save locations from global arraylist to JSON file
    public void saveLocation(Location location) {
        // Add new Location to database
        DatabaseManager.getInstance().addNewLocation(location);
    }

    // Delete a location from the global arraylist
    private void deleteLocation(Location location, int row) {
        locations.remove(location); // Delete location from list
        locationsText.remove(row); // Delete location text from list

        // Delete location from database
        DatabaseManager.getInstance().deleteLocation(location);

        setupListView(); // Refresh list view
    }

    private void updateListView() {
        // Set up list
        if (locationsText.size() < 1) {
            locationsText.add(("There are no locations to show.."));
        }

        setListAdapter(new ArrayAdapter<String>(this,
                R.layout.list_cell,
                R.id.text,
                locationsText));
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

        if (item.getItemId() == R.id.itemHomeActivity) {
            // Set as Main activity
            activityToSwitchTo = new Intent(getBaseContext(), MainActivity.class);
            activityToSwitchTo.putExtra("mainActivityFirstOpen", false);
            startActivity(activityToSwitchTo);
            return true;
        }

        if (item.getItemId() == R.id.itemSearchActivity) {
            // Start search
            onSearchRequested();
        }

        if (item.getItemId() == R.id.itemSavedJobsActivity) {
            // Set as Saved Jobs activity
            activityToSwitchTo = new Intent(getBaseContext(), SavedJobsActivity.class);
            startActivity(activityToSwitchTo);
            return true;
        }

        if (item.getItemId() == R.id.itemSocialShare) {
            // Open share dialog
            // Create intent
            Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);

            // Set content type
            sharingIntent.setType("text/plain");

            // Set text and put to extras
            String shareBody = "Search for jobs in specific locations using iFindAJob Android!";
            sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Share Locations");
            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);

            startActivity(Intent.createChooser(sharingIntent, "Share Via..")); // Social type chooser
        }

        if (item.getItemId() == R.id.itemPreferences) {
            // Open preferences activity
            activityToSwitchTo = new Intent(getBaseContext(), PreferencesActivity.class);
            startActivity(activityToSwitchTo);
            return true;
        }

        return false;
    }

    // When hardware key released
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_SEARCH:
                onSearchRequested();
                return true;
        }
        return false;
    }
}
