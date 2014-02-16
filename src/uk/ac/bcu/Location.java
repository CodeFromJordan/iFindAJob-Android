// Author: Jordan Hancock
// Name: Location.java
// Last Modified: 16/02/2014
// Purpose: Activity which is used for to store location details.
package uk.ac.bcu;

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
