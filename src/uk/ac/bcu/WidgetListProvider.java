// Author: Jordan Hancock
// Name: WidgetListProvider.java
// Last Modified: 12/03/2014
// Purpose: Handles bringing in Job data using DAO.
package uk.ac.bcu;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService.RemoteViewsFactory;
import java.util.ArrayList;
import java.util.List;
import uk.ac.db.DatabaseManager;
import uk.ac.model.Job;

public class WidgetListProvider implements RemoteViewsFactory {

    private List<Job> jobs;
    private List<String> jobsText;
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
        jobsText = new ArrayList<String>();

        int numberOfJobs = jobs.size();
        
        if(numberOfJobs > 5) { // If more than five jobs
            // Add last five added to list
            for(int i = numberOfJobs - 5; i < numberOfJobs; i++) {
                jobsText.add(jobs.get(i).getTitle() + " - " + jobs.get(i).getCity());
            }
        } else { // If five or less jobs
            // Add all of them
            for(Job job : jobs) {
                jobsText.add(job.getTitle() + " - " + job.getCity());
            }
        }
    }

    @Override
    public void onCreate() {
        populateListItem();
    }

    @Override
    public int getCount() {
        int numberOfJobs = jobs.size();
        if(numberOfJobs > 5) {
            return 5;
        } else {
            return numberOfJobs;
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
        final RemoteViews remoteView = new RemoteViews(
                context.getPackageName(), R.layout.widget_list_cell);

        remoteView.setTextViewText(R.id.text, jobsText.get(position));

        return remoteView;
    }
}
