/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.javafxapplication1;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.spec.InvalidKeySpecException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author ntu-user
 */

public class RecoverySystem {

    private static final String binDirectory = "/home/ntu-user/App/.bin";

    public RecoverySystem() {

    }

    public void recoverLastChange(String fileName) throws ClassNotFoundException, InvalidKeySpecException {
        // Check if the file exists in the trash directory
        AuditTrailManager auditTrailManager = new AuditTrailManager();
        File binFile = new File(binDirectory + File.separator + fileName);

        if (binFile.exists()) {
            // Recover the file by moving it back to the original location
            File destinationFile = new File(fileName);
            try {
                Files.move(binFile.toPath(), destinationFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                auditTrailManager.log("File recovered", fileName, UserSession.getCurrentUserID());
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("File not found in the bin directory.");
        }
    }

    // Additional method to display files in the bin directory
    public void displayBinContents() {
        File displayBinDirectory = new File(binDirectory);
        File[] files = displayBinDirectory.listFiles();
        if (files != null) {
            Arrays.stream(files).forEach(System.out::println);
        } else {
            System.out.println("Bin directory is empty.");
        }
    }
    
    public String getBinDirectory() {
        return RecoverySystem.binDirectory;
    }
    
    private boolean lastModificationCheck(String modificationDate) {
        // Parse modificationDate string to LocalDateTime
        LocalDateTime lastModificationDateTime = LocalDateTime.parse(modificationDate, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        // Check if it's not modified in the last 31 days
        LocalDateTime maxTimeAway = LocalDateTime.now().minusDays(31);
        return lastModificationDateTime.isBefore(maxTimeAway);
    }
    
    private void deleteFile(String fileName) {
        // Delete the file from the file system
        Path filePath = Paths.get(binDirectory, fileName);

        try {
            Files.delete(filePath);
            System.out.println("File " + fileName + " deleted from the file system.");
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error deleting file " + fileName + " from the file system.");
        }
    }
    
    public void removeOutOfDateFiles() throws ClassNotFoundException, InvalidKeySpecException {
        DB myObj = new DB();
        AuditTrailManager auditTrailManager = new AuditTrailManager();
        ObservableList<UserFile> userFileData = myObj.getRemovedFiles(UserSession.getCurrentUserID());
        
        for (UserFile file : userFileData) {
            if (lastModificationCheck(file.getModificationDate())) {
                myObj.removeFileFromTable(file.getFileName());
                auditTrailManager.log("File deleted from recovery.", file.getFileName(), UserSession.getCurrentUserID());
                deleteFile(file.getFileName());
            }
        }
    }
}
