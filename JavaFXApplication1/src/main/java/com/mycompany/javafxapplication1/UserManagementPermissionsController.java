/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.mycompany.javafxapplication1;

import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;
import java.io.IOException;
import java.net.URL;
import java.security.spec.InvalidKeySpecException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author ntu-user
 */
public class UserManagementPermissionsController {
    
    @FXML
    private TableView dataTableView;
        
    @FXML
    private TextField userTextField;

    @FXML
    private TextField readTextField;
  
    @FXML
    private TextField writeTextField;
   
    @FXML
    private TextField deleteTextField;
    
    @FXML
    private TextField isAdminTextField;
    
    @FXML
    private Button setAdminBtn;
    
    @FXML
    private Button updatePermissionsBtn;
    
    @FXML
    private Button displayAuditLogBtn;
    
    @FXML
    private Button removeUserBtn;
    
    @FXML
    private Button backBtn;
    
    
    
    private AuditTrailManager auditTrailManager = new AuditTrailManager();
    
    
    
    @FXML
    private void setAdminBtnHandler(ActionEvent event) throws ClassNotFoundException, InvalidKeySpecException {
        DB myObj = new DB();
        ObservableList<User> userData = myObj.getDataFromTable();
        UserACL currentUserACL = myObj.getSingleUserACLFromTable(UserSession.getCurrentUserID());
        User userToGiveAdmin = myObj.getSingleUserFromTable(userTextField.getText());
        UserACL userToGiveAdminACL = myObj.getSingleUserACLFromTable(userToGiveAdmin.getUserID());
        
        int defaultAdminID = 1;
        String defaultAdminUser = null;
        
        for (User user : userData) {
            if (user.getUserID() == defaultAdminID) {
                defaultAdminUser = user.getUser(); 
            }
        }
        
        // current user is admin and specified user exists but not admin
        if (myObj.usernameCheck(userTextField.getText()) && currentUserACL.getIsAdmin() && !userToGiveAdminACL.getIsAdmin()) {
            System.out.println("Specified User does not have admin permissions, giving permissions.");
            myObj.updateACLAdminTable(userToGiveAdmin.getUserID(), true);
            auditTrailManager.log("User given admin privelages.", userTextField.getText(), UserSession.getCurrentUserID());
        // current user is admin and specified user exists and is already admin    
        } else if (myObj.usernameCheck(userTextField.getText()) && currentUserACL.getIsAdmin() && userToGiveAdminACL.getIsAdmin() && !userTextField.getText().equals(defaultAdminUser)) {
            System.out.println("Specified User already has admin permissions, removing permissions.");
            myObj.updateACLAdminTable(userToGiveAdmin.getUserID(), false);
            auditTrailManager.log("Removing Users admin privelages.", userTextField.getText(), UserSession.getCurrentUserID());
        // current user is main admin
        } else if (myObj.usernameCheck(userTextField.getText()) && currentUserACL.getIsAdmin() && userToGiveAdminACL.getIsAdmin() && userTextField.getText().equals(defaultAdminUser)) {
            System.out.println("Cannot edit this User's permissions.");
        } else {
            dialogue("Insufficient Permissions.","Please try again!");
        }
        initialise();        
    }
    
    @FXML
    private void updatePermissionsBtnHandler(ActionEvent event) throws ClassNotFoundException, InvalidKeySpecException {
        DB myObj = new DB();
        User updatedUser = myObj.getSingleUserFromTable(userTextField.getText());
        UserACL updatedUserACL = myObj.getSingleUserACLFromTable(updatedUser.getUserID());
        boolean newReadPermission;
        boolean newWritePermission;
        boolean newDeletePermission;
        
        newReadPermission = "1".equals(readTextField.getText());
        newWritePermission = "1".equals(writeTextField.getText());
        newDeletePermission = "1".equals(deleteTextField.getText());
        myObj.updateACLTable(updatedUserACL.getACLID(), newReadPermission, newWritePermission, newDeletePermission);
        auditTrailManager.log("User permissions updated.", userTextField.getText(), UserSession.getCurrentUserID());
        initialise();
    }
    
    @FXML
    private void displayAuditLogBtnHandler(ActionEvent event) throws ClassNotFoundException, SQLException {
        auditTrailManager.displayAuditTrail();
        DB myObj = new DB();
        ObservableList<AuditEntry> auditLog;
        
        try {
            dataTableView.getColumns().clear();
            
            auditLog = myObj.getAuditLog();
            
            
            TableColumn auditId = new TableColumn("Audit ID");
            auditId.setCellValueFactory(
            new PropertyValueFactory<>("auditId"));

            TableColumn timestamp = new TableColumn("Timestamp");
            timestamp.setCellValueFactory(
            new PropertyValueFactory<>("timestamp"));
            
            TableColumn action = new TableColumn("Action");
            action.setCellValueFactory(
            new PropertyValueFactory<>("action"));
            
            TableColumn change = new TableColumn("Change");
            change.setCellValueFactory(
            new PropertyValueFactory<>("change"));
            
            TableColumn userId = new TableColumn("UserId");
            userId.setCellValueFactory(
            new PropertyValueFactory<>("userId"));            
            
            dataTableView.setItems(auditLog);
            dataTableView.getColumns().addAll(auditId, timestamp, action, change, userId);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(SecondaryController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @FXML
    private void removeUserBtnHandler(ActionEvent event) throws ClassNotFoundException, InvalidKeySpecException {
        DB myObj = new DB();
        UserACL currentUserACL = myObj.getSingleUserACLFromTable(UserSession.getCurrentUserID());
        if ((currentUserACL.getIsAdmin()) && !(userTextField.getText().equals(UserSession.getCurrentUser().getUser())) && !(isAdminTextField.getText().equals("1"))) {
            int removedId = myObj.getUserIdByUsername(userTextField.getText());
            myObj.removeUserFromTable(removedId);
            auditTrailManager.log("User was removed.", userTextField.getText(), UserSession.getCurrentUserID());
            initialise();
        } else {
           dialogue("Insufficient Permissions.","Please try again!");
        }
    }
    
    @FXML
    private void backBtnHandler(ActionEvent event) throws ClassNotFoundException{
        try {
            Stage secondaryStage = new Stage();
            Stage primaryStage = (Stage) backBtn.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("UserManagement.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root, 1280, 720);
            secondaryStage.setScene(scene);
            UserManagementController controller = loader.getController();
            controller.initialise();
            secondaryStage.setTitle("User Management");
            secondaryStage.show();
            primaryStage.close();
        } catch (IOException ex) {
            Logger.getLogger(UserManagementController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void initialise() {      
        DB myObj = new DB();
        ObservableList<User> userData;
        
        try {
            dataTableView.getColumns().clear();
            userTextField.clear();
            readTextField.clear();
            writeTextField.clear();
            deleteTextField.clear();
            isAdminTextField.clear();
            
            userData = myObj.getDataFromTable();
            
            
            TableColumn userName = new TableColumn("Username");
            userName.setCellValueFactory(
            new PropertyValueFactory<>("user"));
            
            dataTableView.setItems(userData);
            dataTableView.getColumns().addAll(userName);
            
            
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(SecondaryController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
       
    private String getTimestamp() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return now.format(formatter);
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
    
    @FXML
    private void tableSelection() throws ClassNotFoundException {
        Object tableSelectionData = dataTableView.getSelectionModel().getSelectedItem();
        
        if (!(tableSelectionData == null)) {
            User userData = (User) tableSelectionData;
        
            userTextField.setText(userData.getUser());
            System.out.println(userData.getUser());
            
            DB myObj = new DB();
            UserACL editingUserACL;
            User editingUser = myObj.getSingleUserFromTable(userTextField.getText());
        
        
            editingUserACL = myObj.getSingleUserACLFromTable(editingUser.getUserID());
        
            int readPermissionInt = editingUserACL.getReadPermission() ? 1 : 0;
            int writePermissionInt = editingUserACL.getWritePermission() ? 1 : 0;
            int deletePermissionInt = editingUserACL.getDeletePermission() ? 1 : 0;
            int adminPermissionInt = editingUserACL.getIsAdmin() ? 1 : 0;
        
            readTextField.setText(String.valueOf(readPermissionInt));
            writeTextField.setText(String.valueOf(writePermissionInt));
            deleteTextField.setText(String.valueOf(deletePermissionInt));
            isAdminTextField.setText(String.valueOf(adminPermissionInt));
        }
        

    }
}
