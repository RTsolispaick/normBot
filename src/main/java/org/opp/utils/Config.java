package org.opp.utils;



public class Config {
    public String getName() {
        return System.getenv("TG_NAME");
    }
    public String getTokenTG() {
        return System.getenv("TG_TOKEN");
    }
    public String getTokenVK(){
        return System.getenv("VK_TOKEN");
    }
    public String getDBhost(){return System.getenv("DB_HOST");}
    public String getDBport(){return System.getenv("DB_PORT");}
    public String getDBname(){return System.getenv("DB_NAME");}
    public String getDBuser(){return System.getenv("DB_USERNAME");}
    public String getDBpass(){return System.getenv("DB_PASSWORD");}
}