/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.javafxapplication1;

import java.security.spec.InvalidKeySpecException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
//import java.util.List;

/**
 *
 * @author ntu-user
 */
public class AuditTrailManager {
    
//    private List<String> auditTrail;
    private DB myObj = new DB();

    public AuditTrailManager() {
//        this.auditTrail = new ArrayList<>();
    }

    public void log(String action, String change, int userId) throws InvalidKeySpecException, ClassNotFoundException {
        String timestamp = getTimestamp();
//        String logEntry = String.format("[%s] %s: %s", timestamp, action, fileName);
//        auditTrail.add(logEntry);
        myObj.addDataToDB(timestamp, action, change, userId);
    }

    public void displayAuditTrail() throws ClassNotFoundException, SQLException {
        myObj.displayAuditTrail();
    }

    private String getTimestamp() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return now.format(formatter);
    }    
    
    
}
