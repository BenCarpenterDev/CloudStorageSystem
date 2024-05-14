/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.javafxapplication1;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleIntegerProperty;

/**
 *
 * @author ntu-user
 */
public class UserContainerError {
    private SimpleIntegerProperty errorId;
    private SimpleIntegerProperty containerNumber;
    private SimpleStringProperty errorMessage;
    private SimpleIntegerProperty date;
    private SimpleIntegerProperty userId;

    UserContainerError(int errorId, int containerNumber, String errorMessage, int date, int userId) {
        this.errorId = new SimpleIntegerProperty(errorId);
        this.containerNumber = new SimpleIntegerProperty(containerNumber);
        this.errorMessage = new SimpleStringProperty(errorMessage);
        this.date = new SimpleIntegerProperty(date);
        this.userId = new SimpleIntegerProperty(userId);
    }

    // Container Error ID
    public int getErrorID() {
        return errorId.get();
    }
    
    // Container Number
    public int getContainerNumber() {
        return containerNumber.get();
    }

    // Container Error Message
    public String getErrorMessage() {
        return errorMessage.get();
    }

    public void setErrorMessage(String message) {
        this.errorMessage.set(message);
    }
    
    // Date Error Occured
    public int getDate() {
        return date.get();
    }
    
    public void setDate(int date) {
        this.date.set(date);
    }
    
    // User ID Associated
    public int getUserId() {
        return userId.get();
    }
}
