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

public class SavedJobsActivity extends ListActivity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        // Set up super
        super.onCreate(savedInstanceState);
        
        // Set up interface
        setContentView(R.layout.saved_jobs);
        this.setTitle("Saved Jobs");
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
    
    static final String[] CELLS = 
        new String[] { "No saved jobs to display.." };
}
