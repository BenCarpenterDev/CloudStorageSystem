/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.mycompany.javafxapplication1;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author ntu-user
 */
public class RegisterController {

    /**
     * Initializes the controller class.
     */
    @FXML
    private Button registerBtn;

    @FXML
    private Button backLoginBtn;

    @FXML
    private PasswordField passPasswordField;

    @FXML
    private PasswordField rePassPasswordField;

    @FXML
    private TextField userTextField;
    
//    @FXML
//    private Text fileText;
    
    
    private AuditTrailManager auditTrailManager = new AuditTrailManager();


    private void dialogue(String headerMsg, String contentMsg) {
        Stage secondaryStage = new Stage();
        Group root = new Group();
        Scene scene = new Scene(root, 300, 300, Color.DARKGRAY);
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText(headerMsg);
        alert.setContentText(contentMsg);
        Optional<ButtonType> result = alert.showAndWait();
    }

    @FXML
    @SuppressWarnings("empty-statement")
    private void registerBtnHandler(ActionEvent event) {
        Stage secondaryStage = new Stage();
        Stage primaryStage = (Stage) registerBtn.getScene().getWindow();
        try {
            FXMLLoader loader = new FXMLLoader();
            DB myObj = new DB();
//          ACLControl aclController = new ACLControl(myObj);
            User currentUser;
            if ((passPasswordField.getText().equals(rePassPasswordField.getText()) && ((!userTextField.getText().isEmpty()) && (!passPasswordField.getText().isEmpty())) && !myObj.usernameCheck(userTextField.getText()))) {
                int generatedUserID = myObj.addDataToDB(userTextField.getText(), passPasswordField.getText());
                if (generatedUserID != -1) {
                    // User registration successful
                    // Set UserSession as current user
                    currentUser = myObj.getSingleUserFromTable(userTextField.getText());
                    UserSession.setCurrentUser(currentUser);
                    // Update ACL control table with the generated userID
                    ACLControl aclController = new ACLControl(myObj);
                    aclController.initialiseACL(generatedUserID);
                    dialogue("Adding information to the database", "Successful!");
                    auditTrailManager.log("User registered.", UserSession.getCurrentUser().getUser(), UserSession.getCurrentUserID());
                    String[] credentials = {userTextField.getText(), passPasswordField.getText()};
                    loader.setLocation(getClass().getResource("secondary.fxml"));
                    Parent root = loader.load();
                    Scene scene = new Scene(root, 1280, 720);
                    secondaryStage.setScene(scene);
                    SecondaryController controller = loader.getController();
                    secondaryStage.setTitle("Show users");
//                    controller.initialise();
                    String msg = "some data sent from Register Controller";
                    secondaryStage.setUserData(msg);
                } else {
                    // Handle the case where user registration failed
                    dialogue("User registration failed.", "Please try again!");
                }
            } else {
                dialogue("Invalid details provided.", "Please try again!");
                loader.setLocation(getClass().getResource("register.fxml"));
                Parent root = loader.load();
                Scene scene = new Scene(root, 640, 480);
                secondaryStage.setScene(scene);
                secondaryStage.setTitle("Register a new User");
            }
            secondaryStage.show();
            primaryStage.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void backLoginBtnHandler(ActionEvent event) {
        Stage secondaryStage = new Stage();
        Stage primaryStage = (Stage) backLoginBtn.getScene().getWindow();
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("primary.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root, 640, 480);
            secondaryStage.setScene(scene);
            secondaryStage.setTitle("Login");
            secondaryStage.show();
            primaryStage.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private String getTimestamp() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return now.format(formatter);
    }
    
}
