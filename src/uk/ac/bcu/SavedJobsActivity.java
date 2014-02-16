// Author: Jordan Hancock
// Name: SavedJobsActivity.java
// Last Modified: 11/02/2014
// Purpose: Activity which is used for saved jobs activity page.
package uk.ac.bcu;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import java.io.FileInputStream;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SavedJobsActivity extends ListActivity {

    private ArrayList<JSONObject> jobs;
    private static final String JOBS_FILENAME = "saved_jobs.json";

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        // Set up super
        super.onCreate(savedInstanceState);

        jobs = loadJobs(); // Load jobs from file

        // Set up interface
        setContentView(R.layout.saved_jobs);
        this.setTitle("Saved Jobs");

        updateListView();
    }
    
    // When job result is selected
    // Pass it to JobDetailActivity to display results
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        if(position < jobs.size()) {
            Intent intent = new Intent(getBaseContext(), JobDetailActivity.class);
            intent.putExtra("job", jobs.get(position).toString());
            intent.putExtra("from_saved", true); // It's from saved job page
            startActivity(intent);
        }
    }

    // When Menu button clicked
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    // Used to load when saving
    private ArrayList<JSONObject> loadJobs() {
        ArrayList<JSONObject> jobList = new ArrayList<JSONObject>(); // ArrayList to store contents of file

        try {
            StringBuilder strList = new StringBuilder();
            FileInputStream inputStream;

            // Read in file
            try {
                inputStream = openFileInput(JOBS_FILENAME);

                byte[] buffer = new byte[1024];

                while (inputStream.read(buffer) != -1) {
                    strList.append(new String(buffer));
                }

                inputStream.close();
            } catch (Exception e) {
            }

            // Convert read in details to list
            JSONObject listWrapper = new JSONObject(strList.toString());
            JSONArray list = listWrapper.getJSONArray("jobs");

            for (int i = 0; i < list.length(); i++) {
                jobList.add(list.getJSONObject(i));
            }
        } catch (JSONException ex) {
        }

        return jobList; // Return current contents of file
    }

    private void updateListView() {
        // Set up for cells
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

    // When item in menu selected
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent activityToSwitchTo = new Intent();
        switch (item.getItemId()) {
            case R.id.itemHomeActivity:
                // Set as Main activity
                activityToSwitchTo = new Intent(getBaseContext(), MainActivity.class);
                startActivity(activityToSwitchTo);
                return true;
            case R.id.itemSearchActivity:
                // Set as Search activity
                activityToSwitchTo = new Intent(getBaseContext(), LocationSearchActivity.class);
                startActivity(activityToSwitchTo);
                return true;
            case R.id.itemSavedJobsActivity:
                // Set as Saved Jobs activity
                activityToSwitchTo = new Intent(getBaseContext(), SavedJobsActivity.class);
                startActivity(activityToSwitchTo);
                return true;
        }
        return false;
    }
}
