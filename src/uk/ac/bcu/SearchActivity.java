/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.ac.bcu;

import android.app.ListActivity;
import android.os.Bundle;
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
        
        this.setTitle("Search - iFindAJob");
        
        setListAdapter(new ArrayAdapter<String>(this,
        R.layout.job_list_cell,
        R.id.text, CELLS));
    }
    
    static final String[] CELLS = 
            new String[] { "Job 0", "Job 1", "Job 2",
                            "Job 3", "Job 4", "Job 5",
                            "Job 6", "Job 7", "Job 8",
                            "Job 9", "Job 10"};
}
