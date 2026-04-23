/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.smartcampus.api.model;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author User
 */
public class Room {
   
    private String id;       // Unique identifier , e.g., "LIB -301"
    private String name;     // Human - readable name , e.g., " LibraryQuiet Study"
    private int capacity ;   // Maximum occupancy for safety regulations
    private List <String > sensorIds = new ArrayList <>();  // Collection of IDs of sensors deployed in this room

    public Room() {
    }
     
    // Constructors , getters , setters ..
    public Room(String id, String name, int capacity){
        this.id=id;
        this.name=name;
        this.capacity=capacity;
    }
    
    // getters
    public String getId(){
        return id;
    }
    
    public String getName(){
        return name;
    }
    
    public int getCapacity(){
        return capacity;
    } 
    
    public List<String> getSensorIds() {
        return sensorIds;
    }
    
    // setters  
    public void setId(String id){
        this.id=id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }
    
    public void setSensorIds(List<String> sensorIds) {
        this.sensorIds = sensorIds;
    }
    
}


