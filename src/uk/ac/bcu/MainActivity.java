// Author: Jordan Hancock
// Name: MainActivity.java
// Last Modified: 16/02/2014
// Purpose: Activity which is used for main (home) activity page.
package uk.ac.bcu;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends Activity {

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        // Set up super
        super.onCreate(savedInstanceState);
        super.setTitle("iFindAJob");

        // Set up interface
        setContentView(R.layout.main);
        this.setTitle("Home");
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
