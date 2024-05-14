package com.mycompany.javafxapplication1;

import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import javafx.scene.control.TextField;
import java.io.FileWriter;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.security.spec.InvalidKeySpecException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Optional;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import javafx.scene.Group;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.paint.Color;
import net.lingala.zip4j.ZipFile;

import net.lingala.zip4j.io.outputstream.ZipOutputStream;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.model.enums.AesKeyStrength;
import net.lingala.zip4j.model.enums.CompressionMethod;
import net.lingala.zip4j.model.enums.EncryptionMethod;

/**
 * FXML Controller class
 *
 * @author ntu-user
 */
public class FileManagementController {
   
    /**
     * Initializes the controller class.
     * 
     * 
     */
    
    @FXML
    private Button backBtn;
    
    @FXML
    private Button createfileBtn;
    
    @FXML
    private TextField fileNameField;
    
    @FXML
    private TextField fileContentField;
    
    @FXML
    private Button saveContentsBtn;
    
    private AuditTrailManager auditTrailManager = new AuditTrailManager();
    
    @FXML
    private void switchToSecondary(ActionEvent event){
        Stage secondaryStage = new Stage();
        Stage primaryStage = (Stage) backBtn.getScene().getWindow();
        try {

            File temp = new File("/home/ntu-user/App/temp.txt");
            temp.delete();
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
    private void createFiles() throws ClassNotFoundException, InvalidKeySpecException {
        DB myObj = new DB();
        UserACL currentUserACL = myObj.getSingleUserACLFromTable(UserSession.getCurrentUserID());
        if (currentUserACL.getWritePermission()) {
            
        
        
            String fileNameString = fileNameField.getText();
            String fileContentString = fileContentField.getText();
            String fileid = "";
            String fileId = "";
            try{

                File f = new File("/home/ntu-user/App/"+ fileNameString + ".txt");
                if(f.createNewFile()){
                    System.out.println("File created: " + f.getName());
                    editFileContents("/home/ntu-user/App/"+ fileNameString + ".txt", fileContentString);

                    try{
                        File tempObj = new File("/home/ntu-user/App/temp.txt");

                        Path path = tempObj.toPath();

                        String date = getTimestamp();

                        fileid = myObj.addDataToDB(fileNameString, tempObj.length(), date, date, UserSession.getCurrentUserID());
                        auditTrailManager.log("File created.", fileNameString, UserSession.getCurrentUserID());

                    } catch (Exception e){
                        e.printStackTrace();
                    }

                    createFileAllprocesses(fileNameString,"/home/ntu-user/App/");
                    f.delete();


                }else{
                    System.out.println("File already exists");
                }
            }catch(IOException e){
                System.err.println(e);
            } 
        } else {
            dialogue("Insufficient Write Permissions.","Please try again!");
            auditTrailManager.log("User attempted to create a file.", UserSession.getCurrentUser().getUser(), UserSession.getCurrentUserID());
        }
    }
    
    private void editFileContents(String fileName, String Contents){
        try{
            
            FileWriter myWriter = new FileWriter(fileName);
            myWriter.write(Contents);
            myWriter.close();
            System.out.println("Successfully wrote to the file.");
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    
    @FXML
    private void saveFileContentsTemp(){
        String fileContentString = fileContentField.getText();
        editFileContents("/home/ntu-user/App/temp.txt", fileContentString);
    }
    
    //* This controls encryption - Task 4 Encryption/Decryption //*
    public void fileEncryption(String fileName, String path) throws FileNotFoundException, IOException{
        ArrayList filesToZip = new ArrayList();
        filesToZip.add(new File(path+fileName+".txt"));

        ZipParameters zipParameters = new ZipParameters();
        zipParameters.setEncryptFiles(true);
        zipParameters.setEncryptionMethod(EncryptionMethod.AES);

        ZipFile zipFile = new ZipFile("/home/ntu-user/App/zipFiles/"+fileName+".zip", "password".toCharArray());
        
        zipFile.addFiles(filesToZip, zipParameters);   
        
        //zipFile.createSplitZipFile(filesToZip, zipParameters, true, 65536);//file chunkung
        
        System.out.println("File successfully encrypted, using AES encryption, password is password");
        
    }
    
    //* This controls Decryption - Task 4 Encryption/Decryption//*
    public void fileDecryption(String fileName, String password) throws FileNotFoundException, IOException{
        
        String fileZip = "/home/ntu-user/App/zipFiles/"+fileName+".zip";

        new ZipFile(fileZip,  password.toCharArray()).extractFile(fileName+".txt", "/home/ntu-user/App/decryptedFiles/");
    }
    
    public void createFileAllprocesses(String fileNameString, String path) throws IOException{
        FileChunking chunkyObj = new FileChunking();
        chunkyObj.splitFile(path+fileNameString+".txt", fileNameString);

        fileEncryption(fileNameString+"_part1",path);
        fileEncryption(fileNameString+"_part2",path);
        fileEncryption(fileNameString+"_part3",path);
        fileEncryption(fileNameString+"_part4",path);
        
        File f = new File("/home/ntu-user/App/"+ fileNameString+"_part1" + ".txt");
        f.delete();
        f = new File("/home/ntu-user/App/"+ fileNameString+"_part2" + ".txt");
        f.delete();
        f = new File("/home/ntu-user/App/"+ fileNameString+"_part3" + ".txt");
        f.delete();
        f = new File("/home/ntu-user/App/"+ fileNameString+"_part4" + ".txt");
        f.delete();
        
        ScpTo uploadObj = new ScpTo();
        uploadObj.uploadFile("comp20081-files-container1", "root", "ntu-user", fileNameString+"_part1");
        uploadObj.uploadFile("comp20081-files-container2", "root", "ntu-user", fileNameString+"_part2");
        uploadObj.uploadFile("comp20081-files-container3", "root", "ntu-user", fileNameString+"_part3");
        uploadObj.uploadFile("comp20081-files-container4", "root", "ntu-user", fileNameString+"_part4");
    }
    
    public void retrieveFileDecryptMerge(String fileNameString) throws JSchException, SftpException, IOException{//retrieves ziped files, decrypts them, then merges file
        FileChunking chunkyObj = new FileChunking();
        ScpTo containerObj = new ScpTo();
        containerObj.downloadFile("comp20081-files-container1", "root", "ntu-user", fileNameString+"_part1");
        containerObj.downloadFile("comp20081-files-container2", "root", "ntu-user", fileNameString+"_part2");
        containerObj.downloadFile("comp20081-files-container3", "root", "ntu-user", fileNameString+"_part3");
        containerObj.downloadFile("comp20081-files-container4", "root", "ntu-user", fileNameString+"_part4");
            
        fileDecryption(fileNameString+"_part1", "password");
        fileDecryption(fileNameString+"_part2", "password");
        fileDecryption(fileNameString+"_part3", "password");
        fileDecryption(fileNameString+"_part4", "password");
        
        File[] splitFiles={
            new File("/home/ntu-user/App/decryptedFiles/"+ fileNameString+"_part1" + ".txt"),
            new File("/home/ntu-user/App/decryptedFiles/"+ fileNameString+"_part2" + ".txt"),
            new File("/home/ntu-user/App/decryptedFiles/"+ fileNameString+"_part3" + ".txt"),
            new File("/home/ntu-user/App/decryptedFiles/"+ fileNameString+"_part4" + ".txt")
        };
        
        File outputFile = new File("/home/ntu-user/App/decryptedFiles/"+ fileNameString+".txt");
        
        chunkyObj.joinFiles(splitFiles, outputFile);

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
