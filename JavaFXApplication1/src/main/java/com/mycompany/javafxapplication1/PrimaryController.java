package com.mycompany.javafxapplication1;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;

public class PrimaryController {

    @FXML
    private Button registerBtn;

    @FXML
    private TextField userTextField;

    @FXML
    private PasswordField passPasswordField;
    
    private AuditTrailManager auditTrailManager = new AuditTrailManager();

    @FXML
    private Button remoteTerminalBtn;

    @FXML
    private void registerBtnHandler(ActionEvent event) {
        Stage secondaryStage = new Stage();
        Stage primaryStage = (Stage) registerBtn.getScene().getWindow();
        DB myObj = new DB();
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("register.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root, 640, 480);
            secondaryStage.setScene(scene);
            secondaryStage.setTitle("Register a new User");
            secondaryStage.show();
            primaryStage.close();
        } catch (Exception e) {
            e.printStackTrace();
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

    @FXML
    private void switchToSecondary() {
        Stage secondaryStage = new Stage();
        Stage primaryStage = (Stage) registerBtn.getScene().getWindow();
        try {
            DB myObj = new DB();
            String[] credentials = {userTextField.getText(), passPasswordField.getText()};
            
            // Assuming getSingleUserFromDatabase finds a match and returns User instance based on the provided username
            User currentUser = myObj.getSingleUserFromTable(userTextField.getText());
        
            if(myObj.validateUser(userTextField.getText(), passPasswordField.getText())){
                UserSession.setCurrentUser(currentUser);
                auditTrailManager.log("User logged in.", UserSession.getCurrentUser().getUser(), UserSession.getCurrentUserID());
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("secondary.fxml"));
                Parent root = loader.load();
                Scene scene = new Scene(root, 1280, 720);
                secondaryStage.setScene(scene);
                SecondaryController controller = loader.getController();
                controller.initialise();
                secondaryStage.setTitle("Show Users");
                String msg="some data sent from Primary Controller";
                secondaryStage.setUserData(msg);
                secondaryStage.show();
                primaryStage.close();
            }
            else{
                dialogue("Invalid User Name / Password","Please try again!");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    

    private String getTimestamp() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return now.format(formatter);
    }

    @FXML
    private void switchToTerminalRemote(ActionEvent event){
        Stage secondaryStage = new Stage();
        Stage primaryStage = (Stage) remoteTerminalBtn.getScene().getWindow();
        try {

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("terminalRemote.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root, 1280, 720);
            secondaryStage.setScene(scene);
            secondaryStage.setTitle("Terminal Remote");
            secondaryStage.show();
            primaryStage.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        
    
    }
    
}
