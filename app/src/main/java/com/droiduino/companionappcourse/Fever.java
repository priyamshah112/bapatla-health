package com.droiduino.companionappcourse;

public class Fever {

    public String findfever(float temperature){
        if(temperature<99){
            return("No Fever");
        }
        else if(temperature>=99 && temperature<101){
            return("Mild Fever");
        }
        else if(temperature>=101 && temperature<104){
            return("Moderate Fever");
        }
        else if(temperature>=104){
            return("High Fever");
        }
        else {
            return "No Fever";
        }
    }
}
