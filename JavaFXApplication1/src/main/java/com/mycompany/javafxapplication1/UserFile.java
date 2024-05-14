/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.javafxapplication1;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleIntegerProperty;

/**
 *
 * @author ntu-user
 */
public class UserFile {
    private SimpleStringProperty fileName;
    private SimpleIntegerProperty size;
    private SimpleStringProperty date;
    private SimpleStringProperty modificationDate;
    private SimpleBooleanProperty isDeleted;
    private SimpleIntegerProperty userId;

    UserFile(String fileName, int size, String date, String modificationDate, boolean isDeleted, int userId) {
        this.fileName = new SimpleStringProperty(fileName);
        this.size = new SimpleIntegerProperty(size);
        this.date = new SimpleStringProperty(date);
        this.modificationDate = new SimpleStringProperty(modificationDate);
        this.isDeleted = new SimpleBooleanProperty(isDeleted);
        this.userId = new SimpleIntegerProperty(userId);
    }
    
    // UserFile Name
    public String getFileName() {
        return fileName.get();
    }

    public void setFileUser(String fileName) {
        this.fileName.set(fileName);
    }

    // UserFile Size
    public int getSize() {
        return size.get();
    }

    public void setSize(int size) {
        this.size.set(size);
    }
    
    // Date Created
    public String getDate() {
        return date.get();
    }
    
    public void setDate(String date) {
        this.date.set(date);
    }
    
    // Modification Date
    public String getModificationDate() {
        return modificationDate.get();
    }
    
    public void setModificationDate(String modificationDate) {
        this.modificationDate.set(modificationDate);
    }
    
    // isDeleted
    public boolean getIsDeleted() {
        return isDeleted.get();
    }
    
    public void setIsDeleted(boolean isDeleted) {
        this.isDeleted.set(isDeleted);
    }
    
    // User ID Associated
    public int getUserId() {
        return userId.get();
    }
}
