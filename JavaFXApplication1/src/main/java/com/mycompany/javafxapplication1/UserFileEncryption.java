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
public class UserFileEncryption {
    private SimpleIntegerProperty encryptionId;
    private SimpleStringProperty encryptionKey;
    private SimpleIntegerProperty fileId;

    UserFileEncryption(int encryptionId, String encryptionKey, int fileId) {
        this.encryptionId = new SimpleIntegerProperty(encryptionId);
        this.encryptionKey = new SimpleStringProperty(encryptionKey);
        this.fileId = new SimpleIntegerProperty(fileId);
    }

    // Encryption ID
    public int getEncryptionID() {
        return encryptionId.get();
    }
    
    // Encryption Key
    public String encryptionKey() {
        return encryptionKey.get();
    }
    
    // File ID
    public int getFileID() {
        return fileId.get();
    }
}
