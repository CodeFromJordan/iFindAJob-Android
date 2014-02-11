// Author: Jordan Hancock
// Name: SearchActivity.java
// Last Modified: 7/02/2014
// Purpose: Activity which is used for search activity page.

package uk.ac.bcu;

import android.app.ListActivity;
import android.content.Intent;
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
        R.layout.location_list_cell,
        R.id.text, CELLS));
        
        onSearchRequested();
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
                activityToSwitchTo = new Intent(getBaseContext(), SearchActivity.class);
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
            new String[] { "No jobs here yet.." };
}
