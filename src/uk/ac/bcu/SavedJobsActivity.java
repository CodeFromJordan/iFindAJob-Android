// Author: Jordan Hancock
// Name: SavedJobsActivity.java
// Last Modified: 20/02/2014
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
import java.util.ArrayList;
import java.util.List;
import uk.ac.availability.InternetConnection;
import uk.ac.db.DatabaseManager;
import uk.ac.model.Job;

public class SavedJobsActivity extends ListActivity {

    private List<Job> jobs;
    private List<String> jobsText;
    private static final String JOBS_FILENAME = "saved_jobs.json";

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        // Set up super
        super.onCreate(savedInstanceState);

        setupListView();

        // Set up interface
        setContentView(R.layout.saved_jobs);
        this.setTitle("Saved Jobs");

        updateListView();
    }

    // Reads Locations from database and populates cells
    private void setupListView() {

        // Read location objects from database
        jobs = DatabaseManager.getInstance().getAllJobs();
        jobsText = new ArrayList<String>();

        // Loop through all locations and get strings to write to cells
        for (Job job : jobs) {
            jobsText.add(job.getTitle() + " - " + job.getCity());
        }

        updateListView();
    }

    // When job result is selected
    // Pass it to JobDetailActivity to display results
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        if (position < jobs.size()) {
            Intent intent = new Intent(getBaseContext(), JobDetailActivity.class);
            intent.putExtra("job_id", jobs.get(position).getID());
            intent.putExtra("job_title", jobs.get(position).getTitle());
            intent.putExtra("job_company_name", jobs.get(position).getCompanyName());
            intent.putExtra("job_city", jobs.get(position).getCity());
            intent.putExtra("job_date_posted", jobs.get(position).getDatePosted());
            intent.putExtra("job_relocation", jobs.get(position).getHasRelocationSupport());
            intent.putExtra("job_telecommunication", jobs.get(position).getRequiresTelecommunication());
            intent.putExtra("job_description", jobs.get(position).getDescription());
            intent.putExtra("job_url", jobs.get(position).getURL());
            intent.putExtra("job_lat", jobs.get(position).getLatitude());
            intent.putExtra("job_lon", jobs.get(position).getLongitude());
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

    private void updateListView() {
        // Set up list
        if (jobsText.size() > 0) {
            setListAdapter(new ArrayAdapter<String>(this,
                    R.layout.list_cell,
                    R.id.text,
                    jobsText));
        }
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
            String shareBody = "I've saved " + jobs.size() + " jobs using iFindAJob Android!";
            sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Share Saved Job");
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
}
