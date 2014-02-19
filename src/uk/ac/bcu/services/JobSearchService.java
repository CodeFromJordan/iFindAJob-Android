// Author: Jordan Hancock
// Name: LocationSearchService.java
// Last Modified: 16/02/2014
// Purpose: Manages second part of search where jobs are pulled from API for selected location.
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
public class JobSearchService extends AbstractService {

    private String query;
    private String location_id;
    private JSONArray results;

    public JobSearchService(String query, String location_id) {
        this.query = URLEncoder.encode(query);
        this.location_id = location_id;
    }

    public JSONArray getResults() {
        return results;
    }

    public void run() {
        String api_key = "5e389f733b28cfe33ac2f03aef32fb1a";
        String url = "http://www.authenticjobs.com/api/?api_key=" + api_key
                + "&method=aj.jobs.search&keywords=" + query
                + "&location=" + location_id + "&format=json";

        boolean error = false;
        HttpClient httpClient = null;
        try {
            httpClient = new DefaultHttpClient();
            HttpResponse data = httpClient.execute(new HttpGet(url));
            HttpEntity entity = data.getEntity();

            String result = EntityUtils.toString(entity, "UTF8");

            JSONObject json = new JSONObject(result);
            json = json.getJSONObject("listings");
            results = json.getJSONArray("listing");

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
