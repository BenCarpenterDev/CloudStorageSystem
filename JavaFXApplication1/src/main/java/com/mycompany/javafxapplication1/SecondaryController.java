package com.mycompany.javafxapplication1;

import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.security.spec.InvalidKeySpecException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;
import javafx.scene.Group;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;


public class SecondaryController {
    
    
    @FXML
    private TextField userTextField;
    
    @FXML
    private TableView dataTableView;

    @FXML
    private Button secondaryButton;
    
    @FXML
    private Button refreshBtn;
    
    @FXML
    private Button fileBtn;
    
    @FXML
    private TextField customTextField;
    
    @FXML
    private Button terminalBtn;
    
    @FXML
    private Button userManageBtn;
    
    @FXML
    private Button recoveryBtn;
    
    @FXML
    private Button terminalRemoteBtn;
    
    public static String userString;
    
    @FXML
    private TextArea actionOutputs;
    
    @FXML
    private Button shareFileBtn;
    
    @FXML
    private TextField toShareTextField;
    
    private AuditTrailManager auditTrailManager = new AuditTrailManager();
    
    

    
    @FXML
    private void RefreshBtnHandler(ActionEvent event) throws JSchException, SftpException, IOException{
        initialise();
    }
    
        
    @FXML
    private void switchToPrimary(){
        Stage secondaryStage = new Stage();
        Stage primaryStage = (Stage) secondaryButton.getScene().getWindow();
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
    
    public void initialise() {
        customTextField.setText("User " + UserSession.getCurrentUserID() + " successfully logged in.");
        
        
        DB myObj = new DB();
        ObservableList<User> userData;
        ObservableList<UserFile> userFileData;
        ObservableList<UserContainerError> containerErrorData;
        
        try {
            dataTableView.getColumns().clear();
            
            userFileData = myObj.getDataFromFilesTable(UserSession.getCurrentUserID());
            
            
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
            actionOutputs.appendText("\nTable data refreshed");
            
            
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(SecondaryController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    @FXML
    private void switchToTerminal(ActionEvent event){
        Stage secondaryStage = new Stage();
        Stage primaryStage = (Stage) terminalBtn.getScene().getWindow();
        try {

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("terminal.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root, 1280, 720);
            secondaryStage.setScene(scene);
            secondaryStage.setTitle("Terminal");
            secondaryStage.show();
            primaryStage.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @FXML
    private void switchToTerminalRemote(ActionEvent event){
        Stage secondaryStage = new Stage();
        Stage primaryStage = (Stage) terminalRemoteBtn.getScene().getWindow();
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
    
    @FXML
    private void switchToFileManagement(ActionEvent event){
        Stage secondaryStage = new Stage();
        Stage primaryStage = (Stage) fileBtn.getScene().getWindow();
        try {
            File temp = new File("/home/ntu-user/App/temp.txt");
            temp.createNewFile();
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("FileManagement.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root, 640, 480);
            secondaryStage.setScene(scene);
            secondaryStage.setTitle("File Management");
            secondaryStage.show();
            primaryStage.close();

        } catch (Exception e) {
            e.printStackTrace();
        }  
    }
    

    @FXML
    private void userManageBtnHandler(ActionEvent event) {
        Stage secondaryStage = new Stage();
        Stage primaryStage = (Stage) userManageBtn.getScene().getWindow();
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("UserManagement.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root, 640, 480);
            secondaryStage.setScene(scene);
            UserManagementController controller = loader.getController();
            controller.initialise();
            secondaryStage.setTitle("User Management");
            secondaryStage.show();
            primaryStage.close();

        } catch (Exception e) {
            e.printStackTrace();
        }  
    }
    
    @FXML
    private void recoveryBtnHandler(ActionEvent event) {
        Stage secondaryStage = new Stage();
        Stage primaryStage = (Stage) recoveryBtn.getScene().getWindow();
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("RecoveryManagement.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root, 640, 480);
            secondaryStage.setScene(scene);
            RecoveryManagementController controller = loader.getController();
            controller.initialise();
            secondaryStage.setTitle("Recovery Management");
            secondaryStage.show();
            primaryStage.close();

        } catch (Exception e) {
            e.printStackTrace();
        }  
    }
  
    @FXML
    private void switchToFileUpdate(ActionEvent event) throws IOException, JSchException, SftpException, ClassNotFoundException, InvalidKeySpecException{
        DB myObj = new DB();
        UserACL currentUserACL = myObj.getSingleUserACLFromTable(UserSession.getCurrentUserID());
        if (currentUserACL.getReadPermission() || currentUserACL.getWritePermission()) {
            if (fileName == ""){
              System.out.println("No file selected to edit");
              actionOutputs.appendText("\nNo file selected to edit");
            } else {

                Stage secondaryStage = new Stage();
                Stage primaryStage = (Stage) fileBtn.getScene().getWindow();
                try {

                    FXMLLoader loader = new FXMLLoader();
                    loader.setLocation(getClass().getResource("FileUpdate.fxml"));
                    Parent root = loader.load();
                    Scene scene = new Scene(root, 640, 480);
                    secondaryStage.setScene(scene);
                    secondaryStage.setTitle("Update File");
                    secondaryStage.show();
                    primaryStage.close();

                } catch (Exception e) {
                    e.printStackTrace();
                } 
            }
        } else {
            dialogue("Insufficient Read Permissions.","Please try again!");
            auditTrailManager.log("User attempted to read a file.", UserSession.getCurrentUser().getUser(), UserSession.getCurrentUserID());
        }
    }
    
    //* Getters and Setters for tableSelection()//*
    public class FileDataModel {

        private String fileName;
        private int size;
        private int date;
        private int modificationDate;
        private int userId;

        
        public String getFileName() {
            return fileName;
        }
    }
    
    public String fileName = "";
    
    //* This selects files from tableview, allows to see info, edit and delete files//*
    @FXML
    private void tableSelection() throws JSchException, IOException, SftpException{
        
        Object tableSelectionData = dataTableView.getSelectionModel().getSelectedItem();
        if(tableSelectionData==null){
            System.out.println("No data selected");
            actionOutputs.appendText("\nNo data selected");
        }else{
            System.out.println(tableSelectionData);
            UserFile fileData = (UserFile) tableSelectionData;
            
            fileName = fileData.getFileName();
            FileUpdateController.fileNameString = fileName;
            
            //ScpTo containerObj = new ScpTo();
            //containerObj.downloadFile("comp20081-files-container1", "root", "ntu-user", fileName);
            
            FileManagementController  encryptObj = new FileManagementController();
            //encryptObj.fileDecryption(fileName, "password");
            encryptObj.retrieveFileDecryptMerge(fileName);
            System.out.println(fileName);
            
            
            File myObj = new File("/home/ntu-user/App/decryptedFiles/"+ fileName + ".txt");
            
            String Output =("\nFile name: "+myObj.getName()+"\n"+
                            "Absolute path: "+myObj.getAbsolutePath()+"\n"+
                            "Writeable: "+myObj.canWrite()+"\n"+
                            "Readable " + myObj.canRead()+"\n"+
                            "File size in bytes "+myObj.length()+"\n");
            
            System.out.println(Output);
            actionOutputs.appendText(Output);
        }
    }
    
    //* This deletes files - Task 3 File Managment - Lab 7 //*
    @FXML
    private void deleteFiles() throws ClassNotFoundException, InvalidKeySpecException, IOException, JSchException, SftpException{
        DB myObj = new DB();
        ScpTo containerObj = new ScpTo();
        AuditTrailManager auditTrailManager = new AuditTrailManager();
        RecoverySystem recoverySystem = new RecoverySystem();
        UserACL currentUserACL = myObj.getSingleUserACLFromTable(UserSession.getCurrentUserID());
        if (currentUserACL.getDeletePermission()) {
            File f = new File("/home/ntu-user/App/decryptedFiles/"+ fileName + ".txt"); 
            File destinationFile = new File(recoverySystem.getBinDirectory() + File.separator + fileName);
            // delete file from UserFiles table.
            containerObj.deleteFile("comp20081-files-container1", "root", "ntu-user", fileName+"_part1");
            containerObj.deleteFile("comp20081-files-container2", "root", "ntu-user", fileName+"_part2");
            containerObj.deleteFile("comp20081-files-container3", "root", "ntu-user", fileName+"_part3");
            containerObj.deleteFile("comp20081-files-container4", "root", "ntu-user", fileName+"_part4");

            System.out.println("Deleted the file: " + f.getName());



            String timestamp = getTimestamp();

            /*
            EDIT TO UPDATE isDeleted instead of removing
            */
            UserFile removedFile = myObj.getSingleFileFromFileTable(fileName);


            myObj.updateRemovedFileTable(fileName, true);
            Files.move(f.toPath(), destinationFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            auditTrailManager.log("File deleted.", fileName, UserSession.getCurrentUserID());
            initialise();
        } else {
            dialogue("Insufficient Delete Permissions.","Please try again!");
            auditTrailManager.log("User attempted to delete a file.", UserSession.getCurrentUser().getUser(), UserSession.getCurrentUserID());
        }
    }
    
    @FXML
    private void shareFileBtnHandler(ActionEvent event) throws InvalidKeySpecException, ClassNotFoundException {
        DB myObj = new DB();
        AuditTrailManager auditTrailManager = new AuditTrailManager();
        File myFileObj = new File("/home/ntu-user/App/decryptedFiles/"+ fileName + ".txt");
        
        String date = getTimestamp();

        String fileid = myObj.addDataToDB(fileName, myFileObj.length(), date, date, myObj.getUserIdByUsername(toShareTextField.getText()));
        auditTrailManager.log("File shared.", fileName, UserSession.getCurrentUserID());
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
}
