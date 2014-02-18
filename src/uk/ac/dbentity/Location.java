// Author: Jordan Hancock
// Name: Location.java
// Last Modified: 18/02/2014
// Purpose: Class used to map Location db entity.
// Library: ORMLite
package uk.ac.dbentity;

import com.j256.ormlite.field.DatabaseField;
import org.json.JSONException;
import org.json.JSONObject;

public class Location {

    // Members and database field annotations
    @DatabaseField(id = true)
    private String id;

    @DatabaseField
    private String city;

    // No argument constructor
    // Used for ORMLite
    public Location() {

    }

    public Location(String id, String city) {
        this.id = id;
        this.city = city;
    }

    // Pass in Location JSONObject, parse into regular object
    // Used when reading in search results
    public Location(JSONObject location) {
        try {
            this.id = location.getString("id");
            this.city = location.getString("city");
        } catch (JSONException ex) {
        }
    }

    public String getID() {
        return this.id;
    }

    public String getCity() {
        return this.city;
    }
}
