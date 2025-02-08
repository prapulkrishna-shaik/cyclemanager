package hcl.esg.ebike.application.Models;

import android.os.Build;
import androidx.annotation.RequiresApi;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class History {
    public String  damage,approve,repair,color;

    public String getRepair() {
        return repair;
    }

    public void setRepair(String repair) {
        this.repair = repair;
    }

    public String getDuration() {
        return duration;
    }
    public String duration;

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getEmpId() {
        return empId;
    }

    public String getCycleId() {
        return cycleId;
    }

    public void setCycleId(String cycleId) {
        this.cycleId = cycleId;
    }

    public void setEmpId(String empId) {
        this.empId = empId;
    }

    public String getDamage() {
        return damage;
    }

    public void setDamage(String damage) {
        this.damage = damage;
    }

    // Current LocalDateTime
    @RequiresApi(api = Build.VERSION_CODES.O)
    private LocalDateTime now = LocalDateTime.now();

    // Employee ID
    public String empId = null;

    public String getApprove() {
        return approve;
    }

    public void setApprove(String approve) {
        this.approve = approve;
    }

    // Cycle ID
    public String cycleId = null;
    // Duration of cycle usage
    public String alloatTime = null;


    public String getAlloatTime() {
        return alloatTime;
    }

    public void setAlloatTime(String alloatTime) {
        this.alloatTime = alloatTime;
    }

    // Default constructor
    public History() {}

    // Constructor to initialize history details

    @RequiresApi(api = Build.VERSION_CODES.O)
    public History(String cycleID, String empID,String color) {
        this.alloatTime =  now.format(DateTimeFormatter.ofPattern("dd-MM-yy HH:mm"));
        this.empId = empID;
        this.cycleId = cycleID;
        this.color=color;

    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public History(String cycleID, String empID,String allottedTime,String repair,String color) {

        this.empId = empID;
        this.cycleId = cycleID;
        this.duration = allottedTime + " to " + now.format(DateTimeFormatter.ofPattern("dd-MM-yy HH:mm"));
        this.repair=repair;
        this.color=color;

    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    public History(String cycleID, String empID,String allottedTime,String color) {

        this.empId = empID;
        this.cycleId = cycleID;
        this.duration = allottedTime+ " to " + now.format(DateTimeFormatter.ofPattern("dd-MM-yy HH:mm"));
        this.color=color;

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public History(String cycleID, String empID, String allottedTime,String color, String damage, String approve) {
        this.duration = allottedTime + " to " + now.format(DateTimeFormatter.ofPattern("dd-MM-yy HH:mm"));
        this.empId = empID;
        this.cycleId = cycleID;
        this.damage = damage;
        this.approve = approve;
        this.color=color;
    }

}