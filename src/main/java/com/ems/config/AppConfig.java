package com.ems.config;


import org.glassfish.jersey.server.ResourceConfig;

public class AppConfig extends ResourceConfig {
    public AppConfig() {
        packages("com.ems.controller");
        packages("com.ems.middleware");
        register(org.glassfish.jersey.media.multipart.MultiPartFeature.class);
    }
}
