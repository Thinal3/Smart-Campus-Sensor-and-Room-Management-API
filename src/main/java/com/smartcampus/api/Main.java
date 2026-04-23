/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.smartcampus.api;


import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;

import java.net.URI;
/**
 *
 * @author User
 */
public class Main {

    // Base URL where server will run
    public static final String BASE_URI = "http://localhost:8080/api/v1/";

    // Method to start the server
    public static HttpServer startServer() {

        // Scan the whole project package for resources, filters, mappers
        final ResourceConfig rc = new ResourceConfig().packages("com.smartcampus.api");

        // Create and start the server using Grizzly
        return GrizzlyHttpServerFactory.createHttpServer(URI.create(BASE_URI), rc);
    }

    // Main method
    public static void main(String[] args) {

        final HttpServer server = startServer();

        System.out.println("Server started at " + BASE_URI);
        System.out.println("Press Ctrl+C to stop...");
    }
}