/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.mycompany.javafxapplication1;

import java.io.IOException;
import java.io.File;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.security.spec.InvalidKeySpecException;
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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author ntu-user
 */
public class RecoveryManagementController {

    @FXML
    private TableView dataTableView;
    
    @FXML
    private Button backBtn;
    
    @FXML
    private Button recoverBtn;
    
    
    @FXML
    private void backBtnHandler(ActionEvent event){
        try {
            Stage secondaryStage = new Stage();
            Stage primaryStage = (Stage) backBtn.getScene().getWindow();
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
    private void recoverBtnHandler(ActionEvent event) throws IOException, InvalidKeySpecException, ClassNotFoundException {
        FileManagementController fileManagerObj = new FileManagementController();
        DB myObj = new DB();
        Object tableSelectionData = dataTableView.getSelectionModel().getSelectedItem();
        UserFile fileData = (UserFile) tableSelectionData;
        String fileName = fileData.getFileName();
        AuditTrailManager auditTrailManager = new AuditTrailManager();
        RecoverySystem recoverySystem = new RecoverySystem();
        File destinationFile = new File("/home/ntu-user/App/decryptedFiles/"+ fileName + ".txt"); 
        File recoveredFile = new File(recoverySystem.getBinDirectory() + File.separator + fileName);
        
        
        myObj.updateRemovedFileTable(fileName, false);
        Files.move(recoveredFile.toPath(), destinationFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
        
        fileManagerObj.createFileAllprocesses(fileName, "/home/ntu-user/App/decryptedFiles/");
        
        auditTrailManager.log("File recovered.", recoveredFile.getName(), UserSession.getCurrentUserID());
        
        initialise();
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
    
    public void initialise() throws IOException, ClassNotFoundException, InvalidKeySpecException {     
        DB myObj = new DB();
        RecoverySystem recoverySystem = new RecoverySystem();
        
        recoverySystem.removeOutOfDateFiles();
        
        ObservableList<UserFile> userFileData;
        
        try {
            dataTableView.getColumns().clear();
            
            userFileData = myObj.getRemovedFiles(UserSession.getCurrentUserID());
            
            
            TableColumn fileName = new TableColumn("File Name");
            fileName.setCellValueFactory(
            new PropertyValueFactory<>("fileName"));

            TableColumn size = new TableColumn("Size");
            size.setCellValueFactory(
            new PropertyValueFactory<>("size"));
            
            TableColumn date = new TableColumn("Date Created");
            date.setCellValueFactory(
            new PropertyValueFactory<>("date"));
            
            TableColumn dateModified = new TableColumn("Date Modified");
            dateModified.setCellValueFactory(
            new PropertyValueFactory<>("modificationDate"));
            
            dataTableView.setItems(userFileData);
            dataTableView.getColumns().addAll(fileName, size, date, dateModified);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(RecoveryManagementController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    
    
    
    
}
