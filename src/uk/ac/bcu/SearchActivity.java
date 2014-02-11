// Author: Jordan Hancock
// Name: SearchActivity.java
// Last Modified: 11/02/2014
// Purpose: Activity which is used for search activity page.

package uk.ac.bcu;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;

/**
 *
 * @author jordan
 */
public class SearchActivity extends ListActivity {
    private Button btnSearch;
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        // Set up super
        super.onCreate(savedInstanceState);
        
        // Populate list
        setListAdapter(new ArrayAdapter<String>(this,
        R.layout.list_cell,
        R.id.text, CELLS));
        
        // Set up interface
        setContentView(R.layout.search); // Layout
        this.setTitle("Search"); // Title
        setupControls(); // Controls
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
                // Set as Main activity Locations
                activityToSwitchTo = new Intent(getBaseContext(), MainActivity.class);
                startActivity(activityToSwitchTo);
                return true;
            case R.id.itemSearchActivity:
                // Set as Search activity
                //activityToSwitchTo = new Intent(getBaseContext(), SearchActivity.class);
                //startActivity(activityToSwitchTo);
                onSearchRequested();
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
            case KeyEvent.KEYCODE_SEARCH:
                onSearchRequested();
                return true; 
        }
        return false;
    }
    
    private void setupControls() {
        // Search button
        btnSearch = (Button)findViewById(R.id.search_button);
        btnSearch.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                onSearchRequested();
            }
        });
        
        // Next control
        // Code goes here
    }
    
    static final String[] CELLS = 
            new String[] { "No saved locations to display.." };
}
