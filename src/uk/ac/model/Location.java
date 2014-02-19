// Author: Jordan Hancock
// Name: Location.java
// Last Modified: 19/02/2014
// Purpose: Class used to map Location db entity.
// Library: ORMLite
package uk.ac.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import org.json.JSONException;
import org.json.JSONObject;

@DatabaseTable
public class Location {

    // Members and database field annotations
    @DatabaseField(id = true)
    private String id;

    @DatabaseField
    private String city;
    
    @DatabaseField
    private String query;

    // No argument constructor
    // Used for ORMLite
    public Location() {

    }

    public Location(String id, String city, String query) {
        this.id = id;
        this.city = city;
        this.query = query;
    }

    // Pass in Location JSONObject, parse into regular object
    // Used when reading in search results
    public Location(JSONObject location, String query) {
        try {
            this.id = location.getString("id");
            this.city = location.getString("city");
            this.query = query;
        } catch (JSONException ex) {
        }
    }

    // Getters
    public String getID() {
        return this.id;
    }

    public String getCity() {
        return this.city;
    }
    
    public String getQuery() {
        return this.query;
    }
    
    @Override
    public String toString() {
        return this.city + " - " + this.query;
    }
}
