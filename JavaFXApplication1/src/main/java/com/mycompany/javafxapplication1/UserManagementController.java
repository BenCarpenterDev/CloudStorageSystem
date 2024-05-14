/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.mycompany.javafxapplication1;

import static com.mycompany.javafxapplication1.SecondaryController.userString;
import java.io.IOException;
import java.net.URL;
import java.security.spec.InvalidKeySpecException;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author ntu-user
 */

public class UserManagementController {
    
    @FXML
    private Button backMainBtn;
    
    @FXML
    private Button updateUserBtn;
    
    @FXML
    private Button managePermissionsBtn;
    
    @FXML
    private TextField userTextField;
    
    @FXML
    private PasswordField passPasswordField;
    
    @FXML
    private PasswordField repassPasswordField;
    
    

    @FXML
    private void backMainBtnHandler(ActionEvent event){
        try {
            Stage secondaryStage = new Stage();
            Stage primaryStage = (Stage) backMainBtn.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("secondary.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root, 1280, 720);
            secondaryStage.setScene(scene);
            SecondaryController controller = loader.getController();
            controller.initialise();
            secondaryStage.setTitle("Secondary");
            secondaryStage.show();
            primaryStage.close();
        } catch (IOException ex) {
            Logger.getLogger(UserManagementController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @FXML
    private void updateUserBtnHandler(ActionEvent event) throws InvalidKeySpecException, ClassNotFoundException {        
        // update user tables with new details
        DB myObj = new DB();
        AuditTrailManager auditTrailManager = new AuditTrailManager();
        String newUsername;
        String newPassword;
        
        // initial data validation
        if (!(userTextField.getText().isBlank() || 
            (passPasswordField.getText().isBlank() && !repassPasswordField.getText().isBlank() || (!passPasswordField.getText().isBlank() && repassPasswordField.getText().isBlank())) ||
            !(passPasswordField.getText().equals(repassPasswordField.getText())) ||
            (myObj.usernameCheck(userTextField.getText()) && !UserSession.getCurrentUser().getUser().equals(userTextField.getText())))) {
            
            // set values
            newUsername = userTextField.getText();
            
            // validate password change
            if (passPasswordField.getText().isBlank() || repassPasswordField.getText().isBlank()) {
                myObj.updateUserTable(UserSession.getCurrentUserID(), newUsername);
                auditTrailManager.log("Username updated.", newUsername, UserSession.getCurrentUserID());
            } else {
                newPassword = repassPasswordField.getText();
                myObj.updateUserTable(UserSession.getCurrentUserID(), newUsername, newPassword);
                auditTrailManager.log("Username/Password updated.", newUsername, UserSession.getCurrentUserID());
            }
            
            // refresh current UserSession
            User updatedUser;
            updatedUser = myObj.getSingleUserFromTable(userTextField.getText());
            UserSession.setCurrentUser(updatedUser);
        } else {
            dialogue("Invalid Form Usage.","Please try again!");
        }
        
        // refresh form
        initialise();
    }
    
    
    @FXML
    private void managePermissionsBtnHandler(ActionEvent event) throws InvalidKeySpecException, ClassNotFoundException {
        DB myObj = new DB();
        UserACL currentUserACL = myObj.getSingleUserACLFromTable(UserSession.getCurrentUserID());
        if (currentUserACL.getIsAdmin()) {
            try {
                Stage secondaryStage = new Stage();
                Stage primaryStage = (Stage) managePermissionsBtn.getScene().getWindow();
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("UserManagementPermissions.fxml"));
                Parent root = loader.load();
                Scene scene = new Scene(root, 1280, 720);
                secondaryStage.setScene(scene);
                UserManagementPermissionsController controller = loader.getController();
                controller.initialise();
                secondaryStage.setTitle("User Management");
                secondaryStage.show();
                primaryStage.close();
            } catch (IOException ex) {
                Logger.getLogger(UserManagementController.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
           dialogue("Insufficient Permissions.","Please try again!");
        }

        
    }
    
    
    
    
    
    
    
    
    
    
    
    private void dialogue(String headerMsg, String contentMsg) {
        Stage secondaryStage = new Stage();
        Group root = new Group();
        Scene scene = new Scene(root, 300, 300, Color.DARKGRAY);

        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText(headerMsg);
        alert.setContentText(contentMsg);

        Optional<ButtonType> result = alert.showAndWait();
    }
    
    public void initialise() throws ClassNotFoundException {
        DB myObj = new DB();
        ObservableList<User> usersData;
        
        UserACL currentUserACL;
        
        usersData = myObj.getDataFromTable();
        currentUserACL = myObj.getSingleUserACLFromTable(UserSession.getCurrentUserID());
        
        passPasswordField.clear();
        repassPasswordField.clear();
        userTextField.clear();
        
        userTextField.setText(UserSession.getCurrentUser().getUser());

           
    }
}
