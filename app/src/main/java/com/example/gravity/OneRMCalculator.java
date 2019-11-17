package com.example.gravity;

public class OneRMCalculator {
    float oneRepMax;

    public float getOneRepMax() {
        return oneRepMax;
    }

    public void setOneRepMax(float oneRepMax) {
        this.oneRepMax = oneRepMax;
    }

    public void calculateOneRepMax(float weight,int reps){
        //Epley Algorithm. Subject to Change..
        float max = weight*(1+(reps/30));
        this.oneRepMax = max;
    }
}
