// Author: Jordan Hancock
// Name: Location.java
// Last Modified: 18/02/2014
// Purpose: Class used to map Location db entity.
package uk.ac.dbentity;

public class Location {

    private String id;
    private String city;

    public Location(String id, String city) {
        this.id = id;
        this.city = city;
    }

    public String getID() {
        return this.id;
    }

    public String getCity() {
        return this.city;
    }

    @Override
    public String toString() {
        return this.city;
    }
}
