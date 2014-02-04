package uk.ac.bcu;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends Activity
{
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        super.setTitle("iFindAJob");
        this.setTitle("iFindAJob - Home");
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
        if(item.getItemId() == R.id.itemSearch) { // Search Films menu item
            onSearchRequested();
            return true;
        }
        return false;
    }
    
    // When hardware key released
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_SEARCH) { // Hardware Search key
            onSearchRequested();
            return true;
        }
        return false;
    }
}
