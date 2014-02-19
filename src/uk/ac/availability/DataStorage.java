// Author: Jordan Hancock
// Name: DataStorage.java
// Last Modified: 18/02/2014
// Purpose: Static class used to check availability of internal storage/SD card.
package uk.ac.availability;

public class DataStorage {

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
}
