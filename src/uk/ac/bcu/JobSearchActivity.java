// Author: Jordan Hancock
// Name: JobSearchActivity.java
// Last Modified: 11/02/2014
// Purpose: Activity which is used for individual job search activity page (no search bar therefore no searchable.)

package uk.ac.bcu;

import android.app.ListActivity;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import java.util.ArrayList;
import org.json.JSONException;
import org.json.JSONObject;
import uk.ac.bcu.services.AbstractService;
import uk.ac.bcu.services.IServiceListener;
import uk.ac.bcu.services.JobSearchService;

/**
 *
 * @author jordan
 */
public class JobSearchActivity extends ListActivity implements IServiceListener {
    private Thread thread;
    private ArrayList<JSONObject> jobs;
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        // Set up super
        super.onCreate(savedInstanceState);
        
        // Set up interface
        setContentView(R.layout.search); // Layout
        this.setTitle("Job Results"); // Title
        setupControls(); // Controls
        jobs = new ArrayList<JSONObject>();
        
        /*
        // Receive intent broadcast from LocationSearchActivity
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(LocationSearchActivity.LOCATION_SAVED_CLICKED);
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                // Read data from intent extras
                String jsonString = intent.getExtras().getString("location");
                try {
                    JSONObject object = new JSONObject(jsonString); // Turn extra intro JSON object
                    doSearch(object.getString("id"), object.getString("query")); // Start search with id and query
                    context.startActivity(intent);
                } catch (JSONException ex) { }
            }
        };
        registerReceiver(receiver, intentFilter); // Make the receiver usable
        */
        
        Intent intent = getIntent();
        String jsonString = intent.getExtras().getString("location");
        try {
            JSONObject object = new JSONObject(jsonString); // Turn extra intro JSON object
            doSearch(object.getString("query"), object.getString("id")); // Start search with id and query
        } catch (JSONException ex) { }
    }
    
    private void updateListView() {
        // Update list
        String[] CELLS = new String[jobs.size()];
        for(int i = 0; i < jobs.size(); i++) {
            try {
                CELLS[i] = jobs.get(i).getString("title");
            } catch (JSONException ex) { }
        }
        
        // Set up list
        setListAdapter(new ArrayAdapter<String>(this, 
                R.layout.list_cell,
                R.id.text,
                CELLS));
    }
    
    // Add a location to the global arraylist
    private void addJob(JSONObject job) {
        jobs.add(job);
        updateListView();
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
    
    public void doSearch(String query, String location_id) {
        String[] result = new String[] { "Searching for jobs.." };
        
        JobSearchService service = new JobSearchService(query, location_id);
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
            jobs.clear();
            
            for(int i = 0; i < jobService.getResults().length(); i++) {
                try {
                    jobs.add(jobService.getResults().getJSONObject(i));
                    result[i] = jobService.getResults().getJSONObject(i).getString("title");
                } catch (JSONException ex) {
                    result[i] = "There has been an error..";
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
}
