// Author: Jordan Hancock
// Name: WidgetListProvider.java
// Last Modified: 12/03/2014
// Purpose: Handles bringing in Job data using DAO.
package uk.ac.bcu;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService.RemoteViewsFactory;
import java.util.ArrayList;
import java.util.List;
import uk.ac.db.DatabaseManager;
import uk.ac.model.Job;

public class WidgetListProvider implements RemoteViewsFactory {

    private List<Job> jobs;
    private Context context = null;
    private int appWidgetId;

    public WidgetListProvider(Context context, Intent intent) {
        this.context = context;

        appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID);

        populateListItem();
    }

    private void populateListItem() {
        // Read location objects from database
        jobs = DatabaseManager.getInstance().getAllJobs();
        
        int numberOfJobs = jobs.size(); // Get size of jobs list
        
        // Get last five items of jobs
        while(jobs.size() > 5) { // While more than five items in jobs
            jobs.remove(0); // Remove first item
        }
    }

    @Override
    public void onCreate() {
        populateListItem();
    }

    @Override
    public int getCount() {
        int numberOfJobs = jobs.size();

        if (numberOfJobs >= 5) { // If five or more jobs saved
            return 5; // Just return count as five
        } else { // If less than five jobs System.out.println("test");saved
            return numberOfJobs; // Return exact number of jobs
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public void onDataSetChanged() {
        populateListItem();
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public RemoteViews getViewAt(int position) {
        // Called by a loop, gets each item from saved list and gets text for cell
        final RemoteViews remoteView = new RemoteViews(
                context.getPackageName(), R.layout.widget_list_cell);

        // Each item has a bundle which stores its job details
        Bundle extras = new Bundle();
        extras.putInt("job_row_number", position);
        extras.putString("job_id", jobs.get(position).getID());
        extras.putString("job_title", jobs.get(position).getTitle());
        extras.putString("job_company_name", jobs.get(position).getCompanyName());
        extras.putString("job_city", jobs.get(position).getCity());
        extras.putString("job_date_posted", jobs.get(position).getDatePosted());
        extras.putBoolean("job_relocation", jobs.get(position).getHasRelocationSupport());
        extras.putBoolean("job_telecommunication", jobs.get(position).getRequiresTelecommunication());
        extras.putString("job_description", jobs.get(position).getDescription());
        extras.putString("job_url", jobs.get(position).getURL());
        extras.putString("job_lat", jobs.get(position).getLatitude());
        extras.putString("job_lon", jobs.get(position).getLongitude());
        extras.putBoolean("from_saved", true); // It's from saved job page
        
        // Job details for each row get added to an intentjobs.get(position).getTitle() + " - " + jobs.get(position).
        Intent fillInIntent = new Intent();
        fillInIntent.putExtras(extras);
        
        // FillInIntent returned to WidgetProvider with extras to open JobDetailView
        remoteView.setOnClickFillInIntent(R.id.widget_text, fillInIntent);
        
        // Get job details for cell
        Job jobForRow = jobs.get(position);
        String jobString = jobForRow.getTitle() + " - " + jobForRow.getCity();
        
        // Write text to cell
        remoteView.setTextViewText(R.id.widget_text, jobString);

        return remoteView;
    }
}
