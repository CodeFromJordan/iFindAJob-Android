// Author: Jordan Hancock
// Name: DataStorage.java
// Last Modified: 18/02/2014
// Purpose: Static class used to check availability of internal storage/SD card.
package uk.ac.availability;

import android.content.Context;

public class DataStorage {

    private static final String myDataDirectory = "/iFindAJob/";
    private static final String databaseName = "iFindAJobDB.sqlite";
    
    // Checks if internal storage should be used (always call this)
    public static boolean shouldUseInternalStorage() {
        // If SD card is available, return False
        // If not available, return True
        return !shouldUseExternalStorage();
    }

    // Checks if SD is available and should be used
    private static boolean shouldUseExternalStorage() {
        // If it is available, return True
        // Else return false
        return android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
    }

    // Returns path to internal app data directory
    public static String getInternalDataLocation(Context context) {
        return context.getApplicationInfo().dataDir + myDataDirectory + databaseName;
    }
    
    public static String getExternalDataLocation() {
        return android.os.Environment.getExternalStorageDirectory().getAbsolutePath() + 
                    myDataDirectory + databaseName;
    }
    
    // Returns path to app data directory
    public static String getDynamicDataLocation(Context context) {
        if (shouldUseInternalStorage() == true) {
            return getInternalDataLocation(context);
        } else {
            return getExternalDataLocation();
        }
    }
}
