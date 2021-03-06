// Author: Jordan Hancock
// Name: SearchableActivity.java
// Last Modified: 20/03/2014
// Purpose: Activity which is used for controlling search bar for Location search.
package uk.ac.bcu;

import uk.ac.model.Location;
import android.app.ListActivity;
import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONException;
import org.json.JSONObject;
import uk.ac.bcu.services.AbstractService;
import uk.ac.bcu.services.IServiceListener;
import uk.ac.bcu.services.LocationSearchService;

public class SearchableActivity extends ListActivity implements IServiceListener {

    private Thread thread;
    private String originalQuery;
    private ArrayList<JSONObject> searchResults;
    public static final String LOCATION_SEARCH_CLICKED = "location_result_selected";

    private ProgressBar prgSearchSpinner;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // Set up super
        super.onCreate(savedInstanceState);

        // Set up interface
        setContentView(R.layout.search);
        this.setTitle("Find jobs in..");

        // Set up controls
        prgSearchSpinner = (ProgressBar) findViewById(R.id.prgSearchSpinner);

        // Initialize list
        searchResults = new ArrayList<JSONObject>();

        // Get the intent, verify the action and get the query
        Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            prgSearchSpinner.setVisibility(View.VISIBLE); // Visible spinner
            String query = intent.getStringExtra(SearchManager.QUERY);
            doSearch(query);
        }
    }

    // When search result location is selected
    // Pass it back to LocationSearchActivity, along with the query, and close this activity.
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        // Return string of JSONObject to LocationSearchActivity to be added to database and shown
        if (position < searchResults.size()) {
            Intent clickedSearchLocation = new Intent();
            clickedSearchLocation.setAction(LOCATION_SEARCH_CLICKED);
            clickedSearchLocation.putExtra("location_result", searchResults.get(position).toString());
            clickedSearchLocation.putExtra("location_query", originalQuery);
            this.sendBroadcast(clickedSearchLocation);

            this.finish();
        }
    }

    public void doSearch(String query) {
        LocationSearchService service = new LocationSearchService(query);
        String[] result = new String[]{"Searching for locations.."};

        service.addListener(this);
        thread = new Thread(service);
        thread.start();

        setListAdapter(new ArrayAdapter<String>(this,
                R.layout.list_cell,
                R.id.text,
                result));
    }

    public void ServiceComplete(AbstractService service) {
        if (!service.hasError()) {

            LocationSearchService locationService = (LocationSearchService) service;
            final int numberOfResults = locationService.getResults().length(); // Get number of results from search

            ArrayList result = new ArrayList();
            //Location[] result = new Location[numberOfResults];

            originalQuery = locationService.getQuery();

            searchResults.clear();

            // For all results, create a Locaiton object using JSON constructor
            for (int i = 0; i < numberOfResults; i++) {
                try {
                    JSONObject locationToAdd = locationService.getResults().getJSONObject(i);
                    // Filter out empty results (which are bad)
                    if (locationToAdd.has("city")) {
                        searchResults.add(locationService.getResults().getJSONObject(i));
                        result.add(new Location(locationService.getResults().getJSONObject(i), originalQuery));
                    } else {
                        System.out.println("Bad result");
                    }
                } catch (JSONException ex) {
                    result.add(new Location("0", "Error", "There has been an error.."));
                }
            }

            // Update list on this page
            setListAdapter(new ArrayAdapter<Location>(this,
                    R.layout.list_cell,
                    R.id.text,
                    result));
        } else { // No results clause
            String[] result = new String[]{"No results.."};

            // Update list to say No Results..
            setListAdapter(new ArrayAdapter<String>(this,
                    R.layout.list_cell,
                    R.id.text,
                    result));
        }

        prgSearchSpinner.setVisibility(View.GONE); // Hide spinner
    }
}
