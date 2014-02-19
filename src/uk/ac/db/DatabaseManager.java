// Author: Jordan Hancock
// Name: DatabaseManager.java
// Last Modified: 18/02/2014
// Purpose: Singleton class used for database access.
package uk.ac.db;

import android.content.Context;
import android.database.SQLException;
import java.util.List;
import uk.ac.model.Job;
import uk.ac.model.Location;

public class DatabaseManager {

    static private DatabaseManager instance;

    static public void init(Context ctx) {
        if (instance == null) {
            instance = new DatabaseManager(ctx);
        }
    }

    static public DatabaseManager getInstance() {
        return instance;
    }

    private DatabaseHelper helper;

    private DatabaseManager(Context ctx) {
        helper = new DatabaseHelper(ctx);
    }

    private DatabaseHelper getHelper() {
        return this.helper;
    }

    public List<Location> getAllLocations() throws SQLException {
        List<Location> locations = null;

        try {
            locations = getHelper().getLocationDao().queryForAll();
        } catch (java.sql.SQLException ex) {
            ex.printStackTrace();
        }

        return locations;
    }

    public List<Job> getAllJobs() {
        List<Job> jobs = null;

        try {
            jobs = getHelper().getJobDao().queryForAll();
        } catch (java.sql.SQLException ex) {
            ex.printStackTrace();
        }

        return jobs;
    }
}
