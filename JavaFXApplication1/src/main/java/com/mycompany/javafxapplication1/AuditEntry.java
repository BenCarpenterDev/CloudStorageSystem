/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.javafxapplication1;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author ntu-user
 */
public class AuditEntry {
    private SimpleIntegerProperty auditId;
    private SimpleStringProperty timestamp;
    private SimpleStringProperty action;
    private SimpleStringProperty change;
    private SimpleIntegerProperty userId;

    AuditEntry(int auditId, String timestamp, String action, String change, int userId) {
        this.auditId = new SimpleIntegerProperty(auditId);
        this.timestamp = new SimpleStringProperty(timestamp);
        this.action = new SimpleStringProperty(action);
        this.change = new SimpleStringProperty(change);
        this.userId = new SimpleIntegerProperty(userId);
    }
    
    // AuditId
    public int getAuditId() {
        return auditId.get();
    }
    
    // Audit timestamp
    public String getTimestamp() {
        return timestamp.get();
    }

    // Audit action
    public String getAction() {
        return action.get();
    }
    
    // Audit change
    public String getChange() {
        return change.get();
    }    
    // Audit userId
    public int getUserId() {
        return userId.get();
    }
}
