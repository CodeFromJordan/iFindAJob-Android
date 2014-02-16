// Author: Jordan Hancock
// Name: LocationSearchService.java
// Last Modified: 16/02/2014
// Purpose: Manages first part of search where locations are pulled from API.
package uk.ac.bcu.services;

import java.net.URLEncoder;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author jordan
 */
public class LocationSearchService extends AbstractService {

    private String query;
    private JSONArray results;

    public LocationSearchService(String query) {
        this.query = URLEncoder.encode(query);
    }

    public String getQuery() {
        return query;
    }

    public JSONArray getResults() {
        return results;
    }

    public void run() {
        String api_key = "5e389f733b28cfe33ac2f03aef32fb1a";
        String url = "http://www.authenticjobs.com/api/?api_key="
                + api_key + "&method=aj.jobs.getLocations&keywords=" + query + "&format=json";

        boolean error = false;
        HttpClient httpClient = null;
        try {
            httpClient = new DefaultHttpClient();
            HttpResponse data = httpClient.execute(new HttpGet(url));
            HttpEntity entity = data.getEntity();

            String result = EntityUtils.toString(entity, "UTF8");

            JSONObject json = new JSONObject(result);
            json = json.getJSONObject("locations");
            results = json.getJSONArray("location");

            if (results.length() == 0) {
                error = true;
            }
        } catch (Exception e) {
            results = null;
            error = true;
        } finally {
            httpClient.getConnectionManager().shutdown();
        }

        super.serviceComplete(error);
    }
}
