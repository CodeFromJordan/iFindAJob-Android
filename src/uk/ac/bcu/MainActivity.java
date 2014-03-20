// Author: Jordan Hancock
// Name: MainActivity.java
// Last Modified: 20/03/2014
// Purpose: Activity which is used for main (home) activity page.
package uk.ac.bcu;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;
import uk.ac.availability.InternetConnection;
import uk.ac.db.DatabaseManager;

public class MainActivity extends Activity {

    private ImageView imgLogo;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // Get shared preferences
        firstRunSkipHomeActivity();
        actuallySkipHomeActivity();

        // Set up super
        super.onCreate(savedInstanceState);
        super.setTitle("iFindAJob");
        DatabaseManager.init(this);

        // Set up interface
        setContentView(R.layout.main);
        this.setTitle("Home");

        // Set up controls
        imgLogo = (ImageView) findViewById(R.id.main_logo);
        imgLogo.setImageDrawable((getResources().getDrawable(R.drawable.icon)));
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
            if (InternetConnection.hasInternetConnection(this)) {
                // Set as Search activity
                activityToSwitchTo = new Intent(getBaseContext(), LocationSearchActivity.class);
                startActivity(activityToSwitchTo);
                return true;
            } else {
                Toast.makeText(getApplicationContext(), "Can't go to Search activity as it requires an internet connection.", Toast.LENGTH_SHORT).show();
                return false;
            }
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

    private void firstRunSkipHomeActivity() {
        // Get shared preferences
        final SharedPreferences sharedPreferences = this.getSharedPreferences(
                "uk.ac.bcu", Context.MODE_PRIVATE);

        // Set first run to True
        boolean firstTimeAppRun = sharedPreferences.getBoolean("firstTime", true);

        if (firstTimeAppRun) { // Only do this is app running for first time

            // Set firstTime to false
            sharedPreferences.edit().putBoolean("firstTime", false).commit();

            // Ask if user wants to delete with Dialog box
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
            alertDialog.setTitle("Skip Home Activity?");
            alertDialog.setMessage("Do you want to skip the Homescreen and jump straight to the "
                    + "Search activity when the app is opened?\n\n"
                    + "You can change this setting in the Preferences activity at any time.");

            // User clicks Yes
            alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    sharedPreferences.edit().putBoolean("skipHome", true).commit();
                    Toast.makeText(getApplicationContext(), "Home activity will be skipped from now on.", Toast.LENGTH_SHORT).show();
                }
            });

            // User clicks No
            alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    sharedPreferences.edit().putBoolean("skipHome", false).commit();
                    Toast.makeText(getApplicationContext(), "Home activity will be displayed when app is opened.", Toast.LENGTH_SHORT).show();
                    dialog.cancel();
                }
            });

            alertDialog.show();
        }
    }

    private void actuallySkipHomeActivity() {
        // Get shared preferences
        final SharedPreferences sharedPreferences = this.getSharedPreferences(
                "uk.ac.bcu", Context.MODE_PRIVATE);
        // If true, skip to LocationSearchActivity
        boolean skipHome = false;
        if (sharedPreferences.getBoolean("skipHome", skipHome)) {
            if (InternetConnection.hasInternetConnection(this)) {
                Intent activityToSwitchTo = new Intent(getBaseContext(), LocationSearchActivity.class);
                startActivity(activityToSwitchTo);
            } else {
                Toast.makeText(getApplicationContext(), "Can't skip to Search activity as it requires an internet connection.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
