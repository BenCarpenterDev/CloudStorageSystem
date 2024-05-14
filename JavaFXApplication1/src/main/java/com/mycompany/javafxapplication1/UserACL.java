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
public class UserACL {
    private SimpleIntegerProperty aclId;
    private SimpleBooleanProperty readPermission;
    private SimpleBooleanProperty writePermission;
    private SimpleBooleanProperty deletePermission;
    private SimpleBooleanProperty isAdmin;
    private SimpleIntegerProperty userId;

    UserACL(int aclId, boolean readPermission, boolean writePermission, boolean deletePermission, boolean isAdmin, int userId) {
        this.aclId = new SimpleIntegerProperty(aclId);
        this.readPermission = new SimpleBooleanProperty(readPermission);
        this.writePermission = new SimpleBooleanProperty(writePermission);
        this.deletePermission = new SimpleBooleanProperty(deletePermission);
        this.isAdmin = new SimpleBooleanProperty(isAdmin);
        this.userId = new SimpleIntegerProperty(userId);
    }

    // ACL ID
    public int getACLID() {
        return aclId.get();
    }
    
    // Read Permission
    public boolean getReadPermission() {
        return readPermission.get();
    }

    public void setReadPermission(boolean readPermission) {
        this.readPermission.set(readPermission);
    }

    // Write Permission
    public boolean getWritePermission() {
        return writePermission.get();
    }

    public void setWritePermission(boolean writePermission) {
        this.writePermission.set(writePermission);
    }
    
    // Delete Permission
    public boolean getDeletePermission() {
        return deletePermission.get();
    }

    public void setDeletePermission(boolean deletePermission) {
        this.deletePermission.set(deletePermission);
    }
    
    // Admin Permission
    public boolean getIsAdmin() {
        return isAdmin.get();
    }
    
    public void setIsAdmin(boolean isAdmin) {
        this.isAdmin.set(isAdmin);
    }

    
    // User ID
    public int getUserID() {
        return userId.get();
    }
}
