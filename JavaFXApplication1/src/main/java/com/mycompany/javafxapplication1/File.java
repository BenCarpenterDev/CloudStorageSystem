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
public class File {
    private SimpleStringProperty fileName;
    private SimpleIntegerProperty size;
    private SimpleIntegerProperty date;
    private SimpleIntegerProperty modificationDate;
    private SimpleIntegerProperty userId;

    File(String fileName, int size, int date, int modificationDate, int userId) {
        this.fileName = new SimpleStringProperty(fileName);
        this.size = new SimpleIntegerProperty(size);
        this.date = new SimpleIntegerProperty(date);
        this.modificationDate = new SimpleIntegerProperty(modificationDate);
        this.userId = new SimpleIntegerProperty(userId);
    }

    // File Name
    public String getFileName() {
        return fileName.get();
    }

    public void setFileUser(String fileName) {
        this.fileName.set(fileName);
    }

    // File Size
    public int getSize() {
        return size.get();
    }

    public void setSize(int size) {
        this.size.set(size);
    }
    
    // Date Created
    public int getDate() {
        return date.get();
    }
    
    public void setDate(int date) {
        this.date.set(date);
    }
    
    // Modification Date
    public int getModificationDate() {
        return modificationDate.get();
    }
    
    public void setModificationDate(int modificationDate) {
        this.modificationDate.set(modificationDate);
    }
    
    // User ID Associated
    public int getUserId() {
        return userId.get();
    }
}
