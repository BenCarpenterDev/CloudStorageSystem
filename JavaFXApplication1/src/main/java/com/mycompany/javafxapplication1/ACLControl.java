/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.javafxapplication1;

import java.security.spec.InvalidKeySpecException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.ObservableList;

/**
 *
 * @author ntu-user
 */
public class ACLControl {
    DB myObj;
    private ObservableList<User> userData;
    private ObservableList<UserFile> userFileData;
    private ObservableList<UserACL> ACL;
    private ObservableList<UserContainerError> userContainerError;
    private ObservableList<UserFileEncryption> userFileEncryption;
    private ObservableList<User> latestUserRow;
    
    /**
     * @brief - constructor loads database data
     */
    ACLControl(DB myObj) {
        try {
            this.myObj = myObj;
            this.userData = myObj.getDataFromTable();
            this.userFileData = myObj.getDataFromFilesTable(UserSession.getCurrentUserID());
            this.ACL = myObj.getDataFromACLTable();
            this.userContainerError = myObj.getDataFromErrorsTable();
            this.userFileEncryption = myObj.getDataFromEncryptionTable();
            this.latestUserRow = myObj.getLatestUserRow();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ACLControl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    public void initialiseACL(int userID) {
        boolean readPerm = true;
        boolean writePerm = true;
        boolean delPerm = false;
        
        try {
        

//        // Iterate through the observable list to read values
//        for (User row : latestUserRow) {
//            // Print any results
//            System.out.println(row.getUserID());
//        }
        
        // Ensure list is not empty
//        if (!latestUserRow.isEmpty()) {
//            User row = latestUserRow.get(0);
//            // Access the class
//            fileID = row.getUserID() + 1;
//        }
        
            
        
            myObj.addDataToDB(readPerm, writePerm, delPerm, userID);
            
            System.out.println("Successfully Added To ACL");
            System.out.println(userID);
            
        } catch (InvalidKeySpecException ex) {
            Logger.getLogger(ACLControl.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ACLControl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
