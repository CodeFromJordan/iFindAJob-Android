package uk.ac.bcu;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import org.json.JSONException;
import org.json.JSONObject;

public class JobDetailActivity extends Activity {
    private JSONObject job;
    
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
    
    /** Called when the interface is first created */
    @Override
    public void onCreate(Bundle savedStateInstance) {
        // Set up super
        super.onCreate(savedStateInstance);
        
        // Set up interface
        super.setTitle("Job Details");
        setContentView(R.layout.job_details);
        
        // Set up controls
        imgJobMap = (ImageView)findViewById(R.id.job_map);
        txtJobTitle = (TextView)findViewById(R.id.txtJobTitle);
        txtJobCompanyName = (TextView)findViewById(R.id.txtJobCompanyName);
        txtJobCity = (TextView)findViewById(R.id.txtJobCity);
        txtJobPostDate = (TextView)findViewById(R.id.txtJobPostDate);
        txtJobHasRelocationAssistance = (TextView)findViewById(R.id.txtJobHasRelocationAssistance);
        txtJobRequiresTelecommuting = (TextView)findViewById(R.id.txtJobRequiresTelecommuting);
        txtJobDescription = (TextView)findViewById(R.id.txtJobDescriptionDetails);
        btnViewInBrowser = (Button)findViewById(R.id.btnViewInBrowser);
        btnSaveJob = (Button)findViewById(R.id.btnSaveJob);
        
        // Button click code
        
        
        // Get data from intent extra
        Intent intent = this.getIntent();
        String jsonString = intent.getExtras().getString("job"); // Shove it into a string
        try {
        job = new JSONObject(jsonString); // Parse it into JSON Object
        } catch (JSONException ex) { }
        
        // Put data into text boxes
        try {
            // Direct reads
            txtJobTitle.setText(txtJobTitle.getText() + " " + job.getString("title"));
            txtJobCompanyName.setText(txtJobCompanyName.getText() + " " + job.getJSONObject("company").getString("id"));
            txtJobCity.setText(txtJobCity.getText() + " " + job.getJSONObject("company").getJSONObject("location").getString("city"));
            txtJobPostDate.setText(txtJobPostDate.getText() + " " + job.getString("post_date"));
            
            // Translate from 1/0 to Yes/No
            if(job.getString("relocation_assistance") == "1") {
                txtJobHasRelocationAssistance.setText(txtJobHasRelocationAssistance.getText() + " " + "Yes");
            } else {
                txtJobHasRelocationAssistance.setText(txtJobHasRelocationAssistance.getText() + " " + "No");
            }
            
            if(job.getString("telecommuting") == "1") {
                txtJobRequiresTelecommuting.setText(txtJobRequiresTelecommuting.getText() + " " + "Yes");
            } else {
                txtJobRequiresTelecommuting.setText(txtJobRequiresTelecommuting.getText() + " " + "No");
            }
        } catch (JSONException ex) { }
    }
}
