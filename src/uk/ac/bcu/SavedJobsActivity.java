// Author: Jordan Hancock
// Name: SavedJobsActivity.java
// Last Modified: 7/02/2014
// Purpose: Activity which is used for saved jobs activity page.

package uk.ac.bcu;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;

public class SavedJobsActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
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
        switch(item.getItemId())
        {
            case R.id.itemHomeActivity:
                // Open Home activity
                return true;
            case R.id.itemSearchActivity:
                // Open Search activity
                return true;
            case R.id.itemSavedJobsActivity:
                // Open Saved Jobs activity
                return true; 
        }
        return false;
    }
    
    // When hardware key released
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        switch(keyCode)
        {
            case KeyEvent.KEYCODE_HOME:
                // Display home activity
                return true;
            case KeyEvent.KEYCODE_SEARCH:
                // Display search activity
                return true; 
        }
        return false;
    }
}
