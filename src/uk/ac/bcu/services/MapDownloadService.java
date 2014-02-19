// Author: Jordan Hancock
// Name: MapDownloadService.java
// Last Modified: 18/02/2014
// Purpose: Manages download of city location map image.
package uk.ac.bcu.services;

// Used to download map image file from Google Maps
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import java.io.IOException;
import java.io.InputStream;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

public class MapDownloadService extends AbstractService {

    private String latitude;
    private String longitude;
    private Bitmap imgMap;

    // Constructor for MapDownloadService
    public MapDownloadService(String latitude, String longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    // Main service, use HttpClient to download bytes and convert to BitmapImage
    public void run() {
        // uses latitude and longitude for Google maps static image
        String urlString = "http://maps.googleapis.com/maps/api/staticmap?center=" + 
                latitude + "," + longitude + 
                "&zoom=14&size=150x100&sensor=false";
        HttpClient client = new DefaultHttpClient();
        HttpGet request = new HttpGet(urlString);
        
        InputStream in = null;
        
        boolean error;
        
        // Convert returned data to BitMap
        try {
            in = client.execute(request).getEntity().getContent();
            imgMap = BitmapFactory.decodeStream(in);
            in.close();
            error = false;
        } catch (IOException ex) { 
            error = true;
        }
        
        super.serviceComplete(error);
    }
    
    public Bitmap getMap() {
        return this.imgMap;
    }
}
