package com.example.gravity;

import java.util.ArrayList;

public class User {
    String name;
    OneRMCalculator recordBench;
    OneRMCalculator recordSquat;
    OneRMCalculator recordDeadlift;
    ArrayList<Total> records;
    public void User(String name, OneRMCalculator recordBench, OneRMCalculator recordSquat, OneRMCalculator recordDeadlift, ArrayList<Total> records){
        this.name = name;
        this.recordBench = recordBench;
        this.recordSquat = recordSquat;
        this.recordDeadlift = recordDeadlift;
        this.records = records;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public OneRMCalculator getRecordBench() {
        return recordBench;
    }

    public void setRecordBench(OneRMCalculator recordBench) {
        this.recordBench = recordBench;
    }

    public OneRMCalculator getRecordSquat() {
        return recordSquat;
    }

    public void setRecordSquat(OneRMCalculator recordSquat) {
        this.recordSquat = recordSquat;
    }

    public OneRMCalculator getRecordDeadlift() {
        return recordDeadlift;
    }

    public void setRecordDeadlift(OneRMCalculator recordDeadlift) {
        this.recordDeadlift = recordDeadlift;
    }

    public ArrayList<Total> getRecords() {
        return records;
    }

    public void setRecords(ArrayList<Total> records) {
        this.records = records;
    }
}
