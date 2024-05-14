/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.javafxapplication1;

/**
 *
 * @author ntu-user
 */
public class UserSession {
    private static User currentUser;

    public static void setCurrentUser(User user) {
        currentUser = user;
    }
    
    public static int getCurrentUserID() {
        return currentUser.getUserID();
    }

    public static User getCurrentUser() {
        return currentUser;
    }
}
