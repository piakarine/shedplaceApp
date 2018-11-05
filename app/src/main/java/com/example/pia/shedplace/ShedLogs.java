package com.example.pia.shedplace;

public class ShedLogs {

    // instance variables
    private int shednbr;
    //private String username;
    private String date;
    private float temperature;
    private float humidity;
    private float ammonia;
    private String treatment;
    private String latitude;
    private String longitude;

    // constructor
    //public ShedLogs(int shednbr, String username, String date, String latitud, String longitud, float temperature,
                    //float humidity, float ammonia, String treatment){
    public ShedLogs(int shednbr, float temperature, float humidity, float ammonia, String treatment, String date, String latitude, String longitude){
        this.shednbr = shednbr;
        this.ammonia =ammonia;
        //this.username=username;
        this.date = date;
        this.latitude =latitude;
        this.longitude = longitude;
        this.temperature =temperature;
        this.humidity=humidity;
        this.treatment= treatment;

    }

    // get methods

    public int getShedNbr(){
        return this.shednbr;
    }

   /* public String getUsername(){
        return this.username;
    }*/

    public String getDate(){
        return this.date;
    }

    public String getLatitude(){
        return this.latitude;
    }
    public String getLongitude(){
        return this.longitude;
    }
    public float getTemperature(){
        return this.temperature;
    }
    public float getHumidity(){
        return this.humidity;
    }
    public float getAmmonia(){
        return this.ammonia;
    }

    public String getTreatment(){
        return this.treatment;
    }

}
