/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.ac.bcu;

import android.app.ListActivity;
import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;

/**
 *
 * @author jordan
 */
public class SearchableActivity extends ListActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search);
        
        // Get the intent, verify the action and get the query
        Intent intent = getIntent();
        if(Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            doSearch(query);
        }
    }
    
    public void doSearch(String query) {
        String[] result = new String[] { query };
        
        setListAdapter(new ArrayAdapter<String>(this,
        R.layout.job_list_cell,
        R.id.text,
        result));
    }
}
