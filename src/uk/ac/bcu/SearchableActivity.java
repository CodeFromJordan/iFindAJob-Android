// Author: Jordan Hancock
// Name: SearchableActivity.java
// Last Modified: 11/02/2014
// Purpose: Activity which is used for search bar.

package uk.ac.bcu;

import android.app.ListActivity;
import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import org.json.JSONException;
import uk.ac.bcu.services.AbstractService;
import uk.ac.bcu.services.IServiceListener;
import uk.ac.bcu.services.LocationSearchService;

/**
 *
 * @author jordan
 */
public class SearchableActivity extends ListActivity implements IServiceListener {
    private Thread thread;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search);
        
        // Get the intent, verify the action and get the query
        Intent intent = getIntent();
        if(Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            doSearch(query);
        }
        
       onSearchRequested();
    }
    
    public void doSearch(String query) {
        String[] result = new String[] { "Searching.." };
        
        LocationSearchService service = new LocationSearchService(query);
        service.addListener(this);
        thread = new Thread(service);
        thread.start();
        
        setListAdapter(new ArrayAdapter<String>(this,
        R.layout.list_cell,
        R.id.text,
        result));
    }
    
        public void ServiceComplete(AbstractService service) {
        if(!service.hasError()) {
            LocationSearchService locationService = (LocationSearchService)service;
            
            String[] result = new String[locationService.getResults().length()];
            
            for(int i = 0; i < locationService.getResults().length(); i++) {
                try {
                    result[i] = locationService.getResults().getJSONObject(i).getString("city");
                } catch (JSONException ex) {
                    result[i] = "Error";
                }
            }
            
            setListAdapter(new ArrayAdapter<String>(this, 
            R.layout.list_cell,
            R.id.text, 
            result));
        } else {
            String[] result = new String[] { "No results.." };
            
            setListAdapter(new ArrayAdapter<String>(this, 
            R.layout.list_cell,
            R.id.text, 
            result));
        }
    }
}
