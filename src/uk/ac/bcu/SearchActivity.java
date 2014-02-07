// Author: Jordan Hancock
// Name: SearchActivity.java
// Last Modified: 7/02/2014
// Purpose: Activity which is used for search activity page.

package uk.ac.bcu;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;

/**
 *
 * @author jordan
 */
public class SearchActivity extends ListActivity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search);
        
        this.setTitle("Search");
        
        setListAdapter(new ArrayAdapter<String>(this,
        R.layout.job_list_cell,
        R.id.text, CELLS));
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
    
    // When search button pressed, open search bar
    
    static final String[] CELLS = 
            new String[] { "Job 0", "Job 1", "Job 2",
                            "Job 3", "Job 4", "Job 5",
                            "Job 6", "Job 7", "Job 8",
                            "Job 9", "Job 10"};
}
