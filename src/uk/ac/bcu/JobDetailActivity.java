// Author: Jordan Hancock
// Name: JobDetailActivity.java
// Last Modified: 16/02/2014
// Purpose: Activity which is used for individual job detail display.
package uk.ac.bcu;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import uk.ac.availability.InternetConnection;
import uk.ac.bcu.services.AbstractService;
import uk.ac.bcu.services.IServiceListener;
import uk.ac.bcu.services.MapDownloadService;

public class JobDetailActivity extends Activity implements IServiceListener {

    private Thread imageThread;
    private JSONObject job;
    private static final String JOBS_FILENAME = "saved_jobs.json";

    // Declare interface controls
    private ImageView imgJobMap;
    private TextView txtJobTitle;
    private TextView txtJobCompanyName;
    private TextView txtJobCity;
    private TextView txtJobPostDate;
    private TextView txtJobHasRelocationAssistance;
    private TextView txtJobRequiresTelecommuting;
    private TextView txtJobDescription;
    private Button btnViewInBrowser;
    private Button btnSaveJob;

    /**
     * Called when the interface is first created
     */
    @Override
    public void onCreate(Bundle savedStateInstance) {
        // Set up super
        super.onCreate(savedStateInstance);

        // Set up interface
        super.setTitle("Job Details");
        setContentView(R.layout.job_details);

        // Set up controls
        imgJobMap = (ImageView) findViewById(R.id.job_map);
        txtJobTitle = (TextView) findViewById(R.id.txtJobTitle);
        txtJobCompanyName = (TextView) findViewById(R.id.txtJobCompanyName);
        txtJobCity = (TextView) findViewById(R.id.txtJobCity);
        txtJobPostDate = (TextView) findViewById(R.id.txtJobPostDate);
        txtJobHasRelocationAssistance = (TextView) findViewById(R.id.txtJobHasRelocationAssistance);
        txtJobRequiresTelecommuting = (TextView) findViewById(R.id.txtJobRequiresTelecommuting);
        txtJobDescription = (TextView) findViewById(R.id.txtJobDescriptionDetails);
        btnViewInBrowser = (Button) findViewById(R.id.btnViewInBrowser);
        btnSaveJob = (Button) findViewById(R.id.btnSaveJob);

        // Button click code
        // View job details on AuthenticJobs webpage
        btnViewInBrowser.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW).setData(Uri.parse(job.getString("url"))));
                } catch (JSONException ex) {
                }
            }
        });

        // Save job to file
        btnSaveJob.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                saveJob();
            }
        });

        // Get data from intent extra
        Intent intent = this.getIntent();
        String jsonString = intent.getExtras().getString("job"); // Shove it into a string
        try {
            job = new JSONObject(jsonString); // Parse it into JSON Object
        } catch (JSONException ex) {
        }

        // Button visibility code
        if (intent.getExtras().getBoolean("from_saved")) { // If from Saved Jobs page
            btnSaveJob.setVisibility(View.INVISIBLE); // Hide button as it wont be needed
        }

        // Put data into text boxes
        try {
            // Direct reads
            txtJobTitle.setText(txtJobTitle.getText() + " " + job.getString("title"));
            txtJobCompanyName.setText(txtJobCompanyName.getText() + " " + job.getJSONObject("company").getString("id"));
            txtJobCity.setText(txtJobCity.getText() + " " + job.getJSONObject("company").getJSONObject("location").getString("city"));
            txtJobPostDate.setText(txtJobPostDate.getText() + " " + job.getString("post_date"));
            txtJobDescription.setText(Html.fromHtml(job.getString("description")));

            // Translate from 1/0 to Yes/No
            if (job.getString("relocation_assistance") == "1") {
                txtJobHasRelocationAssistance.setText(txtJobHasRelocationAssistance.getText() + " " + "Yes");
            } else {
                txtJobHasRelocationAssistance.setText(txtJobHasRelocationAssistance.getText() + " " + "No");
            }

            if (job.getString("telecommuting") == "1") {
                txtJobRequiresTelecommuting.setText(txtJobRequiresTelecommuting.getText() + " " + "Yes");
            } else {
                txtJobRequiresTelecommuting.setText(txtJobRequiresTelecommuting.getText() + " " + "No");
            }

            // Get image
            if (InternetConnection.hasInternetConnection(this)) {
                String latitude = job.getJSONObject("company").getJSONObject("location").getString("lat");
                String longitude = job.getJSONObject("company").getJSONObject("location").getString("lng");
                MapDownloadService mapDownloadService = new MapDownloadService(latitude, longitude);
                mapDownloadService.addListener(this);
                imageThread = new Thread(mapDownloadService);
                imageThread.start();
            }
        } catch (JSONException ex) {
        }
    }

    // Saves a new job to the list of saved jobs
    private void saveJob() {
        try {
            // Creates a JSONArray from loading file
            JSONObject listWrapper = new JSONObject();
            JSONArray list = new JSONArray(loadJobs());
            list.put(job); // Add current job to array
            listWrapper.put("jobs", list);

            String strList = listWrapper.toString(); // Turns it into string

            // Write to file
            FileOutputStream outputStream;
            try {
                outputStream = openFileOutput(JOBS_FILENAME, Context.MODE_PRIVATE);
                outputStream.write(strList.getBytes());
                outputStream.close();
            } catch (Exception e) {
            }
        } catch (JSONException ex) {
        }
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

        if (item.getItemId() == R.id.itemSearchActivity) {
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

        return false;
    }

    public void ServiceComplete(AbstractService service) {
        if (!service.hasError()) {
            MapDownloadService mapDownloadService = (MapDownloadService) service;
            imgJobMap.setImageBitmap(mapDownloadService.getMap());
        }
    }
}
