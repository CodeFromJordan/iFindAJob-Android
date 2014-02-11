/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.ac.bcu;

/**
 *
 * @author jordan
 */
public class Location {
    private String id;
    private String city;
    
    public Location(String id, String city) {
        this.id = id;
        this.city = city;
    }
    
    public String getID()
    {
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
