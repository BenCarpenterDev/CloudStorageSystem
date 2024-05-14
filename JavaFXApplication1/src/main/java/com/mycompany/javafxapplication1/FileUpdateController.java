/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.mycompany.javafxapplication1;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.security.spec.InvalidKeySpecException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import javafx.scene.Group;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.paint.Color;

/**
 * FXML Controller class
 *
 * @author ntu-user
 */
public class FileUpdateController implements Initializable{

    static String fileNameString;

    /**
     * Initializes the controller class.
     */
    
    
    @FXML
    private Button backBtn;
    
    @FXML
    private Button updatefileBtn;
    
    @FXML
    private TextField fileNameField;
    
    @FXML
    private TextField fileContentField;
    
    private AuditTrailManager auditTrailManager = new AuditTrailManager();
    
    @FXML
    private void switchToSecondary(ActionEvent event){
        Stage secondaryStage = new Stage();
        Stage primaryStage = (Stage) backBtn.getScene().getWindow();
        try {
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

        } catch (Exception e) {
            e.printStackTrace();
        }   
    }
    
    @FXML
    private void updateContent(ActionEvent event) throws InvalidKeySpecException, ClassNotFoundException{
        DB myObj = new DB();
        UserACL currentUserACL = myObj.getSingleUserACLFromTable(UserSession.getCurrentUserID());
        if (currentUserACL.getWritePermission()) {
            try{
                FileManagementController fileManagerObj = new FileManagementController();

                FileWriter myWriter = new FileWriter("/home/ntu-user/App/decryptedFiles/"+ fileNameString + ".txt", false);          
                myWriter.write(fileContentField.getText());
                myWriter.close();

                fileManagerObj.createFileAllprocesses(fileNameString,"/home/ntu-user/App/decryptedFiles/");

                System.out.println("Successfully wrote to the file.");
                String modificationDate = getTimestamp();
                myObj.updateFileTable(fileNameString, modificationDate);
                auditTrailManager.log("File updated.", fileNameString, UserSession.getCurrentUserID());
            }catch (Exception e){
                e.printStackTrace();;
            } 
        } else {
            dialogue("Insufficient Write Permissions.","Please try again!");
            auditTrailManager.log("User attempted to create a file.", UserSession.getCurrentUser().getUser(), UserSession.getCurrentUserID());
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        
        try {
            File myObj = new File("/home/ntu-user/App/decryptedFiles/"+ fileNameString + ".txt");
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                String currentFileContent = myReader.nextLine();
                fileContentField.appendText(currentFileContent);
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        
        fileNameField.appendText(fileNameString);
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

    private String getTimestamp() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return now.format(formatter);
    }
}
