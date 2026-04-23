/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.smartcampus.api;

import com.smartcampus.api.model.SensorReading;
import com.smartcampus.api.model.Sensor;
import com.smartcampus.api.model.Room;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 *
 * @author User
 */
public class DataStore {
    
    // Store all rooms
    public static Map<String, Room> rooms = new HashMap<>();

    // Store all sensors
    public static Map<String, Sensor> sensors = new HashMap<>();

    // Store readings (sensorId: list of readings)
    public static Map<String, List<SensorReading>> readings = new HashMap<>();
}
