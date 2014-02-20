// Author: Jordan Hancock
// Name: JobSearchActivity.java
// Last Modified: 20/02/2014
// Purpose: Activity which is used for individual job search activity page (no search bar therefore no searchable.)
package uk.ac.bcu;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import java.util.ArrayList;
import org.json.JSONException;
import org.json.JSONObject;
import uk.ac.availability.InternetConnection;
import uk.ac.bcu.services.AbstractService;
import uk.ac.bcu.services.IServiceListener;
import uk.ac.bcu.services.JobSearchService;

public class JobSearchActivity extends ListActivity implements IServiceListener {

    private Thread thread;
    private ArrayList<JSONObject> jobs;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        // Set up super
        super.onCreate(savedInstanceState);

        // Set up interface
        setContentView(R.layout.search); // Layout
        this.setTitle("Job Results"); // Title
        jobs = new ArrayList<JSONObject>();

        Intent intent = getIntent();
        String jsonString = intent.getExtras().getString("location");
        doSearch(intent.getExtras().getString("location_query"),
                intent.getExtras().getString("location_id")); // Start search with id and query
    }

    private void updateListView() {
        // Update list
        String[] CELLS = new String[jobs.size()];
        for (int i = 0; i < jobs.size(); i++) {
            try {
                CELLS[i] = jobs.get(i).getString("title");
            } catch (JSONException ex) {
            }
        }

        // Set up list
        setListAdapter(new ArrayAdapter<String>(this,
                R.layout.list_cell,
                R.id.text,
                CELLS));
    }

    // When job result is selected
    // Pass it to JobDetailActivity to display results
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        if (position < jobs.size()) {
            Intent intent = new Intent(getBaseContext(), JobDetailActivity.class);
            intent.putExtra("job", jobs.get(position).toString());
            intent.putExtra("from_saved", false); // It's not from saved job page
            startActivity(intent);
        }
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

        if (item.getItemId() == R.id.itemHomeActivity) {
            // Set as Main activity
            activityToSwitchTo = new Intent(getBaseContext(), MainActivity.class);
            startActivity(activityToSwitchTo);
            return true;
        }

        if (item.getItemId() == R.id.itemSearchActivity && InternetConnection.hasInternetConnection(this)) {
            // Set as Search activity
            activityToSwitchTo = new Intent(getBaseContext(), LocationSearchActivity.class);
            startActivity(activityToSwitchTo);
            return true;
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
            String shareBody = "I've found " + jobs.size() + " jobs in a search using "
                    + "iFindAJob Android";
            sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Share Jobs");
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

    public void doSearch(String query, String location_id) {
        String[] result = new String[]{"Searching for jobs.."};

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
        if (!service.hasError()) {
            JobSearchService jobService = (JobSearchService) service;
            String[] result = new String[jobService.getResults().length()];
            jobs.clear();

            for (int i = 0; i < jobService.getResults().length(); i++) {
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
            String[] result = new String[]{"No results.."};

            setListAdapter(new ArrayAdapter<String>(this,
                    R.layout.list_cell,
                    R.id.text,
                    result));
        }
    }
}
