package com.example.gravity;

public class Total {

    float total;
    float recordDeadlift;
    float recordSquat;
    float recordBench;

    public void Total(float recordDeadlift,float recordSquat, float recordBench){
        this.recordBench = recordBench;
        this.recordSquat = recordSquat;
        this.recordDeadlift = recordDeadlift;
        this.total = recordBench + recordSquat + recordDeadlift;
    }

    public float getTotal() {
        return total;
    }

    public void setTotal(float total) {
        this.total = total;
    }

    public float getRecordDeadlift() {
        return recordDeadlift;
    }

    public void setRecordDeadlift(float recordDeadlift) {
        this.recordDeadlift = recordDeadlift;
    }

    public float getRecordSquat() {
        return recordSquat;
    }

    public void setRecordSquat(float recordSquat) {
        this.recordSquat = recordSquat;
    }

    public float getRecordBench() {
        return recordBench;
    }

    public void setRecordBench(float recordBench) {
        this.recordBench = recordBench;
    }

}
