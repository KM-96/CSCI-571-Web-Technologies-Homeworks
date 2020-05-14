package org.usc.csci571.newsapp.models;

import org.usc.csci571.newsapp.utils.Constants;

public class OpenWeatherModel {
    private int temperature;
    private String main;

    public OpenWeatherModel() {
    }

    public void setTemperature(String temperature) {
        double value = Double.parseDouble(temperature);
        this.temperature = (int) Math.round(value);
    }

    public String getTemperature() {
        return temperature + Constants.DEGREE_CENTIGRADE;
    }

    public String getMain() {
        return main;
    }

    public void setMain(String main) {
        this.main = main;
    }

    @Override
    public String toString() {
        return "OpenWeatherModel{" +
                "temperature=" + temperature +
                ", main='" + main + '\'' +
                '}';
    }
}
