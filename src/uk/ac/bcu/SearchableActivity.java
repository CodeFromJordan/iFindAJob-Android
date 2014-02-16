// Author: Jordan Hancock
// Name: SearchableActivity.java
// Last Modified: 16/02/2014
// Purpose: Activity which is used for controlling search bar for Location search.
package uk.ac.bcu;

import android.app.ListActivity;
import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import java.util.ArrayList;
import org.json.JSONException;
import org.json.JSONObject;
import uk.ac.bcu.services.AbstractService;
import uk.ac.bcu.services.IServiceListener;
import uk.ac.bcu.services.LocationSearchService;

/**
 *
 * @author jordan
 */
public class SearchableActivity extends ListActivity implements IServiceListener {

    private Thread thread;
    private String originalQuery;
    private ArrayList<JSONObject> searchResults;
    public static final String LOCATION_SEARCH_CLICKED = "location_result_selected";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search);
        this.setTitle("Find jobs in..");
        searchResults = new ArrayList<JSONObject>();

        // Get the intent, verify the action and get the query
        Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            doSearch(query);
        }
    }

    // When search result location is selected
    // Pass it back to LocationSearchActivity, along with the query, and close this activity.
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
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
            Location[] result = new Location[numberOfResults];
            originalQuery = locationService.getQuery();
            searchResults.clear();

            for (int i = 0; i < numberOfResults; i++) {
                try {
                    searchResults.add(locationService.getResults().getJSONObject(i));
                    result[i] = new Location(locationService.getResults().getJSONObject(i).getString("id"),
                            locationService.getResults().getJSONObject(i).getString("city"));
                } catch (JSONException ex) {
                    result[i] = new Location("Error", "There has been an error..");
                }
            }

            setListAdapter(new ArrayAdapter<Location>(this,
                    R.layout.list_cell,
                    R.id.text,
                    result));
        } else {
            String[] result = new String[]{"No results.."};

            setListAdapter(new ArrayAdapter<String>(this,
                    R.layout.list_cell,
                    R.id.text,
                    result));
        }
    }
}
