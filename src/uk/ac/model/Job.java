// Author: Jordan Hancock
// Name: Job.java
// Last Modified: 19/02/2014
// Purpose: Class used to map Job db entity.
// Library: ORMLite
package uk.ac.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import org.json.JSONException;
import org.json.JSONObject;

@DatabaseTable
public class Job {
    // Members and database field annotations
    @DatabaseField(id = true)
    private String id;
    
    @DatabaseField
    private String title;
    
    @DatabaseField
    private String companyName;
    
    @DatabaseField
    private String city;
    
    @DatabaseField
    private String datePosted;
    
    @DatabaseField
    private boolean hasRelocationSupport;
    
    @DatabaseField
    private boolean requiresTelecommunitcation;
    
    @DatabaseField
    private String description;
    
    @DatabaseField
    private String url;
    
    @DatabaseField
    private String latitude;
    
    @DatabaseField
    private String longitude;
    
    // No argument constructor
    // Used for ORMLite
    public Job() {
        
    }
    
    // Pass in seperate values
    // Used when loading in already stored results
    public Job(String id, String title, String companyName, 
            String city, String datePosted, boolean hasRelocationSupport, 
            boolean requiresTelecommunication, String description, 
            String url, String latitude, String longitude) {
        this.id = id;
        this.title = title;
        this.companyName = companyName;
        this.city = city;
        this.datePosted = datePosted;
        this.hasRelocationSupport = hasRelocationSupport;
        this.requiresTelecommunitcation = requiresTelecommunication;
        this.description = description;
        this.url = url;
        this.latitude = latitude;
        this.longitude = longitude;
    }
    
    // Pass in Job JSONObject, parse into regular object
    // Used when reading in search results
    public Job(JSONObject job) {
        try {
            this.id = job.getString("id");
            this.title = job.getString("title");
            this.companyName = job.getJSONObject("company").getString("id");
            this.city = job.getJSONObject("company").getJSONObject("location").getString("city");
            this.datePosted = job.getString("post_date");
            this.description = job.getString("description");
            this.url = job.getString("url");
            this.latitude = job.getJSONObject("company").getJSONObject("location").getString("lat");
            this.longitude = job.getJSONObject("company").getJSONObject("location").getString("lng");
            
            // Translate from 1/0 to Yes/No
            if (job.getString("relocation_assistance") == "1") {
                this.hasRelocationSupport = true;
            } else {
                this.hasRelocationSupport = false;
            }

            if (job.getString("telecommuting") == "1") {
                this.requiresTelecommunitcation = true;
            } else {
                this.requiresTelecommunitcation = false;
            }
        } catch (JSONException ex) { 
            this.id = "bad_job";
        }
    }
    
    // Getters
    public String getID() {
        return this.id;
    }
    
    public String getTitle() {
        return this.title;
    }
    
    public String getCompanyName() {
        return this.companyName;
    }
    
    public String getCity() {
        return this.city;
    }
    
    public String getDatePosted() {
        return this.datePosted;
    }
    
    public boolean getHasRelocationSupport() {
        return this.hasRelocationSupport;
    }
    
    public boolean getRequiresTelecommunication() {
        return this.requiresTelecommunitcation;
    }
    
    public String getDescription() {
        return this.description;
    }
    
    public String getURL() {
        return this.url;
    }
    
    public String getLatitude() {
        return this.latitude;
    }
    
    public String getLongitude() {
        return this.longitude;
    }
}
