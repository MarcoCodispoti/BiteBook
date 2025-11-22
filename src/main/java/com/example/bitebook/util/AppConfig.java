package com.example.bitebook.util;

public class AppConfig{
    private static AppConfig instance;

    private boolean demoMode;

//    private AppConfig(){
//        this.demoMode = false;
//    }

    private AppConfig() {
        // 1. Definisci il nome della tua proprietà
        String propertyName = "bitebook.mode";


        String mode = System.getProperty(propertyName, "persistence");

        this.demoMode = mode.equalsIgnoreCase("demo");

        System.out.println("--- BiteBook avviato in modalità: " +
                (this.demoMode ? "DEMO" : "PERSISTENCE") + " ---");
    }


    public static AppConfig getInstance(){
        if(instance == null){
            instance = new AppConfig();
        }
        return instance;
    }

    public boolean isDemoMode(){
        return this.demoMode;
    }

    public void setDemoMode(boolean demoMode){
        this.demoMode = demoMode;
    }


}
