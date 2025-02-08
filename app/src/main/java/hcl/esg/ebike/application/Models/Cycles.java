package hcl.esg.ebike.application.Models;

import android.widget.Button;
import java.util.HashMap;
import java.util.Map;

public class Cycles {
    private String cid,cycleId;


    private Map<String, String> reasons;



    public Map<String, String> getReasons() {
        return reasons;
    }

    public void setReasons(Map<String, String> reasons) {
        this.reasons = reasons;
    }

    public String  getAvailability() {
        return availability;
    }

    private String availability;

    public String getLocation() {
        return location;
    }

    public String getColor() {
        return color;
    }

    private String color;
    private String location,RequestTime,ReturnTime;

    private Button damage,Approve,Repaired;

    public String getCid() {
        return cid;
    }

    public Button getRepaired() {
        return Repaired;
    }

    public void setRepaired(Button repaired) {
        Repaired = repaired;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public Button getDamage() {
        return damage;
    }


    public String getRequestTime() {
        return RequestTime;
    }

    public String getReturnTime() {
        return ReturnTime;
    }

    public void setReturnTime(String returnTime) {
        ReturnTime = returnTime;
    }

    public void setRequestTime(String requestTime) {
        RequestTime = requestTime;
    }

    public Button getApprove() {
        return Approve;
    }

    public void setApprove(Button approve) {
        Approve = approve;
    }

    public String getCycleId() {
        return cycleId;
    }

    public void setCycleId(String cycleId) {
        this.cycleId = cycleId;
    }

    public void setDamage(Button damage) {
        damage = damage;
    }

    public Cycles() {
        // Default constructor required for calls to DataSnapshot.getValue(Cycles.class)
    }


    public Cycles(String cycleId) {

        this.cycleId = cycleId;



    }

    public Cycles(String cid, String availability, String color, String location) {
        this.location = location;
        this.cid = cid;
        this.availability = availability;
        this.color = color;
    }

    public Cycles(String cid, String color, String location) {
        this.location = location;
        this.cid = cid;
        this.color = color;
    }


}