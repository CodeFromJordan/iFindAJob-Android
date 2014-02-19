// Author: Jordan Hancock
// Name: DatabaseHelper.java
// Last Modified: 18/02/2014
// Purpose: Class used to map from database to object.
package uk.ac.db;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.util.Log;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import java.util.ArrayList;
import java.util.List;
import uk.ac.model.Job;
import uk.ac.model.Location;

public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

    private static final String DATABASE_NAME = Environment.getExternalStorageDirectory().getAbsolutePath() + 
            "/iFindAJob/iFindAJobDB.sqlite";

    private static final int DATABASE_VERSION = 1; // Change when database structure changes

    // Dao object used to access tables
    private Dao<Location, Integer> locationDao = null;
    private Dao<Job, Integer> jobDao = null;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // When program first installed/run, create database and tables
    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, Location.class);
            TableUtils.createTable(connectionSource, Job.class);
        } catch (SQLException ex) {
            Log.e(DatabaseHelper.class.getName(), "Can't create database", ex);
        } catch (java.sql.SQLException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {
            // List of SQL statements
            List<String> allSql = new ArrayList<String>();
            switch (oldVersion) {
                case 1:
                // Add required SQL staments to allSql
            }
            for (String sql : allSql) { // Execute list of SQL statements
                database.execSQL(sql);
            }
        } catch (SQLException ex) {
            Log.e(DatabaseHelper.class.getName(), "Exception during onUpgrade", ex);
            throw new RuntimeException(ex);
        }
    }

    // Get locationDao
    public Dao<Location, Integer> getLocationDao() {
        // If it's null, try to get it before returning
        if (locationDao == null) {
            try {
                locationDao = getDao(Location.class);
            } catch (java.sql.SQLException ex) {
                ex.printStackTrace();
            }
        }

        return locationDao;
    }

    // Get jobDao
    public Dao<Job, Integer> getJobDao() {
        // If it's null, try to get it before returning
        if (jobDao == null) {
            try {
                jobDao = getDao(Job.class);
            } catch (java.sql.SQLException ex) {
                ex.printStackTrace();
            }
        }

        return jobDao;
    }
}
