/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */

package com.mycompany.javafxapplication1;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.File;

/**
 * FXML Controller class
 *
 * @author ntu-user
 */
public class TerminalController implements Initializable {
   
    /**
     * Initializes the controller class.
     * 
     * 
     */
    
    @FXML
    private TextArea outputText;
    
    @FXML
    private TextField inputText;
    
    @FXML
    private Button backBtn;
    
    
    private void runCommands(){
        
        outputText.appendText("Welcome to the terminal, type in 'help' to show commands");

        inputText.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER){
                String input = inputText.getText();
                String[] words = input.split("\\W+");

                switch(words[0]){
                    case "help":
                       outputText.appendText("\nThe following commands are: mv, cp, ls, mkdir, ps, whoami, tree, nano, cd");
                       break;
                    case "mv":
                        try {
                            runTerminal(input, outputText);
                        } catch (IOException ex) {
                            Logger.getLogger(TerminalController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                       break;
                    case "cp":
                        try {
                            runTerminal(input, outputText);
                        } catch (IOException ex) {
                            Logger.getLogger(TerminalController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                       break;
                    case "cd":
                        try {
                            runTerminal(input, outputText);
                        } catch (IOException ex) {
                            Logger.getLogger(TerminalController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                       break;
                    case "ls":
                        try {
                            runTerminal(input, outputText);
                        } catch (IOException ex) {
                            Logger.getLogger(TerminalController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                       break;
                    case "mkdir":
                        try {
                            runTerminal(input, outputText);
                        } catch (IOException ex) {
                            Logger.getLogger(TerminalController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                       break;
                    case "ps":
                        try {
                            runTerminal(input, outputText);
                        } catch (IOException ex) {
                            Logger.getLogger(TerminalController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                       break;
                    case "whoami":
                        try {
                            runTerminal(input, outputText);
                        } catch (IOException ex) {
                            Logger.getLogger(TerminalController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                       break;
                    case "tree":
                        try {
                            runTerminal(input, outputText);
                        } catch (IOException ex) {
                            Logger.getLogger(TerminalController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                       break;
                    case "nano":
                        var processBuilder = new ProcessBuilder();
                        processBuilder.command("terminator", "-e","nano");
                        {
                            try {
                                var process = processBuilder.start();
                            } catch (IOException ex) {
                                Logger.getLogger(TerminalController.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                       break;    
    
                    default:
                       outputText.appendText("Command is invalid");    
                       break;
                }               
                inputText.clear();               
            }      
        });
    }
    

    
    public void runTerminal(String Command, TextArea outputText) throws IOException{
        
        var processBuilder = new ProcessBuilder();

        processBuilder.command("/bin/bash", "-c",Command);
        
        File directory = new File("/home/ntu-user/");
        
        processBuilder.directory(directory);

        String line = "";

        try {
            var process = processBuilder.start();
            BufferedReader newReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            
            while((line=newReader.readLine())!=null){
                System.out.println(line);
                outputText.appendText("\n"+line);
            }
            
        }catch(IOException e){
            e.printStackTrace();
        }
    }
    
    
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
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        runCommands();
    }

}
