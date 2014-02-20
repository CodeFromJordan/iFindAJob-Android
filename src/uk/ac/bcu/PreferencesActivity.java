// Author: Jordan Hancock
// Name: PreferencesActivity.java
// Last Modified: 20/02/2014
// Purpose: Allows user to manage app preferences
package uk.ac.bcu;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ToggleButton;
import uk.ac.availability.InternetConnection;

public class PreferencesActivity extends Activity {

    private ToggleButton tgbSkipHome;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        // Set up super
        super.onCreate(savedInstanceState);

        // Set up interface
        setContentView(R.layout.preferences);
        this.setTitle("Preferences");

        // Get shared preferences
        final SharedPreferences sharedPreferences = this.getSharedPreferences(
                "uk.ac.bcu", Context.MODE_PRIVATE);
        
        // Set up controls
        tgbSkipHome = (ToggleButton) findViewById(R.id.tgbSkipHome);
        boolean skipHome = false;
        skipHome = sharedPreferences.getBoolean("skipHome", skipHome);
        tgbSkipHome.setChecked(skipHome);

        // Button click code
        // Set shared preferences when button clicked
        tgbSkipHome.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (tgbSkipHome.isChecked()) {
                    sharedPreferences.edit().putBoolean("skipHome", true).commit();
                } else {
                    sharedPreferences.edit().putBoolean("skipHome", false).commit();
                }
            }
        });
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
            String shareBody = "Live somewhere on Planet Earth? Looking for a job in I.T.? "
                    + "You should download iFindAJob from the Android PlayStore!";
            sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Share App");
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
