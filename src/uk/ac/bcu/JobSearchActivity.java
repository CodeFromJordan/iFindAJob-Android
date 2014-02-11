// Author: Jordan Hancock
// Name: JobSearchActivity.java
// Last Modified: 11/02/2014
// Purpose: Activity which is used for individual job search activity page.

package uk.ac.bcu;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import org.json.JSONException;
import uk.ac.bcu.services.AbstractService;
import uk.ac.bcu.services.IServiceListener;
import uk.ac.bcu.services.JobSearchService;
import uk.ac.bcu.services.LocationSearchService;

/**
 *
 * @author jordan
 */
public class JobSearchActivity extends ListActivity implements IServiceListener {
    private Thread thread;
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        // Set up super
        super.onCreate(savedInstanceState);
        
        // Populate list
        setListAdapter(new ArrayAdapter<String>(this,
        R.layout.list_cell,
        R.id.text, CELLS));
        
        // Set up interface
        setContentView(R.layout.search); // Layout
        this.setTitle("Search"); // Title
        setupControls(); // Controls
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
                //activityToSwitchTo = new Intent(getBaseContext(), SearchActivity.class);
                //startActivity(activityToSwitchTo);
                onSearchRequested();
                return true;         
            case R.id.itemSavedJobsActivity:
                // Set as Saved Jobs activity
                activityToSwitchTo = new Intent(getBaseContext(), SavedJobsActivity.class);
                startActivity(activityToSwitchTo);
                return true; 
        }
        return false;
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
            JobSearchService jobService = (JobSearchService)service;
            
            String[] result = new String[jobService.getResults().length()];
            
            for(int i = 0; i < jobService.getResults().length(); i++) {
                try {
                    result[i] = jobService.getResults().getJSONObject(i).getString("city");
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
    
        static final String[] CELLS = 
            new String[] { "Performing job search.." };
}
