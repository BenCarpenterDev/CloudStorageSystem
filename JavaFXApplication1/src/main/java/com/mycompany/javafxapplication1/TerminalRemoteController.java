/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.mycompany.javafxapplication1;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpATTRS;
import com.jcraft.jsch.SftpException;
import com.jcraft.jsch.SftpStatVFS;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import com.jcraft.jsch.ChannelExec;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

/**
 * FXML Controller class
 *
 * @author ntu-user
 */
public class TerminalRemoteController implements Initializable{
    
    @FXML
    private TextArea outputText;
    
    @FXML
    private TextField inputText;
    
    @FXML
    private Button backBtn;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        runCommands();
    }
    
    public boolean containerChosen = false;
    public String containerNumber = "";
    
    private void runCommands(){
        
        outputText.appendText("Welcome to the remote terminal, choose a container, enter '1', '2', 3' or '4'");
        
        inputText.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER){
                String input = inputText.getText();

                if ("1".equals(input) && containerChosen == false){
                    outputText.appendText("\nChosen container 1");
                    containerNumber = input;
                    containerChosen = true;
                } else if("2".equals(input)&& containerChosen == false){
                    outputText.appendText("\nChosen container 2");
                    containerNumber = input;
                    containerChosen = true;
                }else if("3".equals(input)&& containerChosen == false){
                    outputText.appendText("\nChosen container 3");
                    containerNumber = input;
                    containerChosen = true;
                }else if("4".equals(input)&& containerChosen == false){
                    outputText.appendText("\nChosen container 4");
                    containerNumber = input;
                    containerChosen = true;
                }else if (containerChosen == true){
                    try {
                        sendCommand(input, containerNumber);
                        outputText.appendText("\nCommand executed");
                    } catch (JSchException ex) {
                     Logger.getLogger(TerminalRemoteController.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (IOException ex) {
                        Logger.getLogger(TerminalRemoteController.class.getName()).log(Level.SEVERE, null, ex);
                    }                        
                }
                inputText.clear(); 
            }
        });
        
    }

    public void sendCommand(String command, String containerNumber) throws JSchException, IOException
     {  
        JSch jsch = new JSch();
        Session session = jsch.getSession("root", "comp20081-files-container"+containerNumber, 22);
        java.util.Properties config = new java.util.Properties();
        config.put("StrictHostKeyChecking", "no");
        session.setConfig(config);
        session.setPassword("ntu-user");
        session.connect();


        Channel channel = session.openChannel("exec");
        ((ChannelExec)channel).setCommand(command);
        channel.connect();

        String line = "";

        try {
            BufferedReader newReader = new BufferedReader(new InputStreamReader(channel.getInputStream()));
            while((line=newReader.readLine())!=null){
                System.out.println(line);
                outputText.appendText("\n"+line);
            } 
        }catch(IOException e){
            e.printStackTrace();
        }
            
        channel.disconnect();
        session.disconnect();
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
}
