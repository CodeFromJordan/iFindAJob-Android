// Author: Jordan Hancock
// Name: Job.java
// Last Modified: 18/02/2014
// Purpose: Class used to map Job db entity.
// Library: ORMLite
package uk.ac.dbentity;

import com.j256.ormlite.field.DatabaseField;
import org.json.JSONException;
import org.json.JSONObject;

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
    
    // No argument constructor
    // Used for ORMLite
    public Job() {
        
    }
    
    // Pass in seperate values
    // Used when loading in already stored results
    public Job(String id, String title, String companyName, 
            String city, String datePosted, boolean hasRelocationSupport, 
            boolean requiresTelecommunication, String url) {
        this.id = id;
        this.title = title;
        this.companyName = companyName;
        this.city = city;
        this.datePosted = datePosted;
        this.hasRelocationSupport = hasRelocationSupport;
        this.requiresTelecommunitcation = requiresTelecommunication;
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
            this.url = job.getString("url");
            
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
        } catch (JSONException ex) { }
    }
    
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
    
    public String getURL() {
        return this.url;
    }
}
