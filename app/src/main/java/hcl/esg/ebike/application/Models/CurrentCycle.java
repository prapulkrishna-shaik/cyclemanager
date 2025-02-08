package hcl.esg.ebike.application.Models;

import android.os.Build;
import androidx.annotation.RequiresApi;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CurrentCycle {
     // Current date and time
     @RequiresApi(api = Build.VERSION_CODES.O)
     private LocalDateTime now = LocalDateTime.now();
     // Allotted time for the cycle
     @RequiresApi(api = Build.VERSION_CODES.O)
     public String allottedTime;
     // Cycle ID
     public String cycleID ;
     // Employee ID
     public String empID ;
     public String  damage,approve;

     @RequiresApi(api = Build.VERSION_CODES.O)
     public String getAllottedTime() {
          return allottedTime;
     }

     public String getCycleID() {
          return cycleID;
     }

     public String getEmpID() {
          return empID;
     }

     public void setEmpID(String empID) {
          this.empID = empID;
     }

     public void setCycleID(String cycleID) {
          this.cycleID = cycleID;
     }

     public String getDamage() {
          return damage;
     }

     public void setDamage(String damage) {
          this.damage = damage;
     }



     @RequiresApi(api = Build.VERSION_CODES.O)
     public void setAllottedTime(String allottedTime) {
          this.allottedTime = allottedTime;
     }

     // Default constructor
     public CurrentCycle() {
     }


     // Constructor to initialize current cycle details
     @RequiresApi(api = Build.VERSION_CODES.O)
     public CurrentCycle(String cycleID, String empID) {
          this.allottedTime = now.format(DateTimeFormatter.ofPattern("dd-MM-yy HH:mm"));
          this.cycleID = cycleID;
          this.empID = empID;
     }

     @RequiresApi(api = Build.VERSION_CODES.O)
     public CurrentCycle(String cycleID, String empID, String damage, String approve) {
          this.allottedTime = now.format(DateTimeFormatter.ofPattern("dd-MM-yy HH:mm"));
          this.cycleID = cycleID;
          this.empID = empID;
          this.damage = damage;
          this.approve = approve;
     }
}
