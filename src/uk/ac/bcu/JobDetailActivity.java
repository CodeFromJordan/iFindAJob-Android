// Author: Jordan Hancock
// Name: JobDetailActivity.java
// Last Modified: 20/03/2014
// Purpose: Activity which is used for individual job detail display.
package uk.ac.bcu;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONException;
import org.json.JSONObject;
import uk.ac.availability.InternetConnection;
import uk.ac.bcu.services.AbstractService;
import uk.ac.bcu.services.IServiceListener;
import uk.ac.bcu.services.MapDownloadService;
import uk.ac.db.DatabaseManager;
import uk.ac.model.Job;

public class JobDetailActivity extends Activity implements IServiceListener {

    private Thread imageThread;
    private Job job;

    // Declare interface controls
    private ImageView imgJobMap;
    private Animation imgJobMapAnimation;
    private boolean imgJobMapFullscreen = false;
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

        // Set placeholder map
        imgJobMap.setImageDrawable((getResources().getDrawable(R.drawable.mapplaceholder)));

        // View click code
        // View job details on AuthenticJobs webpage
        btnViewInBrowser.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW).setData(Uri.parse(job.getURL())));
            }
        });

        // Save job to file
        btnSaveJob.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                saveJob();
                Toast.makeText(getApplicationContext(), "Job saved.", Toast.LENGTH_SHORT).show();
            }
        });

        // Stretch image
        imgJobMap.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {

                // Set map animation
                if (imgJobMapFullscreen == true) { // If already fullscreen
                    // Set to fill animation
                    imgJobMapAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.map_shrink);
                } else { // If not fullscreen
                    // Set to shrink animation
                    imgJobMapAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.map_fill);
                }

                imgJobMapFullscreen = !imgJobMapFullscreen; // Flip fullscreen state boolean
                imgJobMap.startAnimation(imgJobMapAnimation); // Play animation
            }
        });

        // Get data from intent extra
        Intent intent = this.getIntent();
        String jsonString = intent.getExtras().getString("job"); // Shove it into a string
        try {
            if (intent.getExtras().getBoolean("from_saved") == false) {
                job = new Job(new JSONObject(jsonString)); // Use JSONObject to create Job object
            } else {
                String id = intent.getExtras().getString("job_id");
                String title = intent.getExtras().getString("job_title");
                String company_name = intent.getExtras().getString("job_company_name");
                String city = intent.getExtras().getString("job_city");
                String date_posted = intent.getExtras().getString("job_date_posted");
                boolean relocation = intent.getExtras().getBoolean("job_relocation");
                boolean telecommunication = intent.getExtras().getBoolean("job_telecommunication");
                String description = intent.getExtras().getString("job_description");
                String url = intent.getExtras().getString("job_url");
                String lat = intent.getExtras().getString("job_lat");
                String lon = intent.getExtras().getString("job_lon");

                job = new Job(id, title, company_name, city,
                        date_posted, relocation, telecommunication, description, url, lat, lon);
            }
        } catch (JSONException ex) {
        }

        if (job.getID() == "bad_job") {
            Toast.makeText(getApplicationContext(), "There was an error with the data returned for the job you chose.\n\n"
                    + "Please choose another.", Toast.LENGTH_SHORT).show();
            this.finish();
        } else {
            // Button visibility code
            if (intent.getExtras().getBoolean("from_saved")) { // If from Saved Jobs page
                btnSaveJob.setVisibility(View.INVISIBLE); // Hide button as it wont be needed
            }

            // Put data into text boxes
            // Direct reads
            txtJobTitle.setText(txtJobTitle.getText() + " " + job.getTitle());
            txtJobCompanyName.setText(txtJobCompanyName.getText() + " " + job.getCompanyName());
            txtJobCity.setText(txtJobCity.getText() + " " + job.getCity());
            txtJobPostDate.setText(txtJobPostDate.getText() + " " + job.getDatePosted());
            txtJobDescription.setText(Html.fromHtml(job.getDescription()));

            // Translate from 1/0 to Yes/No
            if (job.getHasRelocationSupport()) {
                txtJobHasRelocationAssistance.setText(txtJobHasRelocationAssistance.getText() + " " + "Yes");
            } else {
                txtJobHasRelocationAssistance.setText(txtJobHasRelocationAssistance.getText() + " " + "No");
            }

            if (job.getRequiresTelecommunication()) {
                txtJobRequiresTelecommuting.setText(txtJobRequiresTelecommuting.getText() + " " + "Yes");
            } else {
                txtJobRequiresTelecommuting.setText(txtJobRequiresTelecommuting.getText() + " " + "No");
            }

            // Get image
            if (InternetConnection.hasInternetConnection(this)) {
                MapDownloadService mapDownloadService = new MapDownloadService(job.getLatitude(), job.getLongitude());
                mapDownloadService.addListener(this);
                imageThread = new Thread(mapDownloadService);
                imageThread.start();
            }
        }
    }

    // Saves a new job to the list of saved jobs
    private void saveJob() {
        // Add new Job to database
        DatabaseManager.getInstance().addNewJob(job);

        // Refresh widget on Saved Job addition
        Intent updateWidget = new Intent();
        updateWidget.setAction(WidgetProvider.DATA_CHANGED);
        sendBroadcast(updateWidget);
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
            String shareBody = "iFindAJob Android: Want to be an '" + job.getTitle() + "'?"
                    + " Check out: " + job.getURL();
            sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Share Job");
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

    public void ServiceComplete(AbstractService service) {
        if (!service.hasError()) {
            MapDownloadService mapDownloadService = (MapDownloadService) service;
            imgJobMap.setImageBitmap(mapDownloadService.getMap());
        }
    }
}
