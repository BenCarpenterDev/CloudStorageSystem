package com.mycompany.javafxapplication1;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * JavaFX App
 */
public class App extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        Stage secondaryStage = new Stage();
        DB myObj = new DB();
        myObj.checkDirectory("App", "/home/ntu-user/App/");
        myObj.checkDirectory("zipFiles", "/home/ntu-user/App/zipFiles/");
        myObj.checkDirectory("decryptedFiles", "/home/ntu-user/App/decryptedFiles/");
        myObj.checkDirectory(".bin", "/home/ntu-user/App/.bin");
        
        /*
        //||| Comment above line to... |||
        //||| drop tables if any issues. ||||

        try {
            myObj.delTable(myObj.getTableName());
            myObj.delTable(myObj.getFilesTableName());
            myObj.delTable(myObj.getErrorsTableName());
            myObj.delTable(myObj.getACLTableName());
            myObj.delTable(myObj.getEncryptionTableName());
            myObj.delTable(myObj.getAuditTableName());
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
        }
        //*/
        
        myObj.log("\n----------------------------------------------------");
        myObj.log("\n---------- Verifying Database Connections ----------");
        myObj.log("\n----------------------------------------------------");
        
        try {
            myObj.createTable(myObj.getTableName());
            myObj.createFilesTable(myObj.getFilesTableName());
            myObj.createErrorsTable(myObj.getErrorsTableName());
            myObj.createACLTable(myObj.getACLTableName());
            myObj.createEncryptionTable(myObj.getEncryptionTableName());
            myObj.createAuditTable(myObj.getAuditTableName());
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        myObj.log("\n----------------------------------------------------");
        myObj.log("\n--------------- Connections Verified ---------------");
        myObj.log("\n----------------------------------------------------");
      
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("primary.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root, 640, 480);
            secondaryStage.setScene(scene);
            secondaryStage.setTitle("Primary View");
            secondaryStage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch();
    }

}