// Author: Jordan Hancock
// Name: InternetConnection.java
// Last Modified: 18/02/2014
// Purpose: Static class used to check connection to internet.
package uk.ac.availability;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class InternetConnection {

    public static boolean hasInternetConnection(Context c) {
        // Creates connectivity manager using context network info
        ConnectivityManager connectivityManager
                = (ConnectivityManager) c.getSystemService(Context.CONNECTIVITY_SERVICE);

        // Gets a list of active networks available to the device
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();

        // Returns true if network list not null and device is connected
        // Else returns false
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
