// Author: Jordan Hancock
// Name: WidgetProvider.java
// Last Modified: 12/03/2014
// Purpose: Used to provide widget update functionality.
package uk.ac.bcu;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.RemoteViews;

public class WidgetProvider extends AppWidgetProvider {

    public static final String DATA_CHANGED = "uk.ac.bcu.DATA_CHANGED";

    // Called according to appwidget_providerinfo.xml
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

        // Each instance of widget has own id
        final int N = appWidgetIds.length;
        for (int i = 0; i < N; ++i) {
            RemoteViews remoteViews = updateWidgetListView(context,
                    appWidgetIds[i]);
            appWidgetManager.updateAppWidget(appWidgetIds[i],
                    remoteViews);
            
            // Intent for clickable list items within widget
            // Starts JobDetailActivity
            Intent startActivityIntent = new Intent(context, JobDetailActivity.class);

            // Pending intent gets extras from item clicked in list
            PendingIntent startActivityPendingIntent = PendingIntent.getActivity(context, 0, startActivityIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            remoteViews.setPendingIntentTemplate(R.id.listViewWidget, startActivityPendingIntent);
            
            // Start pending intent
            appWidgetManager.updateAppWidget(appWidgetIds[i], remoteViews);
        }
        
            // Refresh list
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.listViewWidget);

        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    private RemoteViews updateWidgetListView(Context context,
            int appWidgetId) {

        //which layout to show on widget
        RemoteViews remoteViews = new RemoteViews(
                context.getPackageName(), R.layout.appwidget);

        //RemoteViews Service needed to provide adapter for ListView
        Intent svcIntent = new Intent(context, AppWidgetService.class);

        //passing app widget id to that RemoteViews Service
        svcIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);

        //setting a unique Uri to the intent
        //don't know its purpose to me right now
        svcIntent.setData(Uri.parse(
                svcIntent.toUri(Intent.URI_INTENT_SCHEME)));

        //setting adapter to listview of the widget
        remoteViews.setRemoteAdapter(appWidgetId, R.id.listViewWidget,
                svcIntent);

        //setting an empty view in case of no data
        remoteViews.setEmptyView(R.id.listViewWidget, R.id.empty_view);

        return remoteViews;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);

        if (DATA_CHANGED.equals(intent.getAction())) {

            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            ComponentName thisAppWidget = new ComponentName(context.getPackageName(), WidgetProvider.class.getName());
            int[] appWidgetIds = appWidgetManager.getAppWidgetIds(thisAppWidget);

            onUpdate(context, appWidgetManager, appWidgetIds);
        }
    }
}
