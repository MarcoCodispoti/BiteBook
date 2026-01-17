package com.example.bitebook.util;


public class AppConfig{

    
    private final boolean demoMode;

    private static final String PROPERTY_KEY = "bitebook.mode";
    private static final String MODE_DEMO = "demo";
    private static final String MODE_PERSISTENCE = "persistence";


    private AppConfig() {
        String mode = System.getProperty(PROPERTY_KEY, MODE_PERSISTENCE);
        this.demoMode = MODE_DEMO.equalsIgnoreCase(mode);
    }



    private static class SingletonHolder {
        private static final AppConfig INSTANCE = new AppConfig();
    }


    public static synchronized AppConfig getInstance() {
        return SingletonHolder.INSTANCE;
    }


    public boolean isDemoMode() {
        return this.demoMode;
    }

}
