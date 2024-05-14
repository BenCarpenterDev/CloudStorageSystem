package com.mycompany.javafxapplication1;

import com.jcraft.jsch.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ScpTo {
    

    //private static final String REMOTE_HOST = "172.18.0.4";// Individual container ID, container1: 172.18.0.4, container2: 172.18.0.3, container3: 172.18.0.2, container4: 172.18.0.5 
    //private static final String USERNAME = "root";
    //private static final String PASSWORD = "ntu-user";
    //private static final int REMOTE_PORT = 22;
    //private static final int SESSION_TIMEOUT = 10000;
    //private static final int CHANNEL_TIMEOUT = 5000;
    
    private ChannelSftp startJsch(String REMOTE_HOST, String USERNAME, String PASSWORD) throws JSchException {
        int REMOTE_PORT = 22;
        
        JSch jsch = new JSch();
        jsch.setKnownHosts("/home/ntu-user/known_hosts");
        Session jschSession = jsch.getSession(USERNAME, REMOTE_HOST, REMOTE_PORT);
        java.util.Properties config = new java.util.Properties();
        config.put("StrictHostKeyChecking", "no");
        jschSession.setConfig(config);
        jschSession.setPassword(PASSWORD);
        jschSession.connect();
        return (ChannelSftp) jschSession.openChannel("sftp");
    }


    public void uploadFile(String REMOTE_HOST, String USERNAME, String PASSWORD, String fileName) {

        Session jschSession = null;

        try {

            ChannelSftp channelSftp = startJsch(REMOTE_HOST, USERNAME,PASSWORD);
            channelSftp.connect();

            String hostFile = "/home/ntu-user/App/zipFiles/"+fileName+".zip";
            String containerFile = "/root/"+fileName+".zip";

            channelSftp.put(hostFile, containerFile);

            System.out.println(fileName+" transfered to "+REMOTE_HOST);
            channelSftp.exit();


        } catch (JSchException | SftpException e) {
            e.printStackTrace();
        } finally {
            if (jschSession != null) {
                jschSession.disconnect();
            }
        }

        System.out.println("Done");
    }
    
    public void downloadFile(String REMOTE_HOST, String USERNAME, String PASSWORD, String fileName) throws JSchException, SftpException {
        ChannelSftp channelSftp = startJsch(REMOTE_HOST, USERNAME,PASSWORD);
        channelSftp.connect();

        String containerFile = "/root/"+fileName+".zip";
        String hostFile = "/home/ntu-user/App/zipFiles/";

        channelSftp.get(containerFile, hostFile);

        System.out.println("File: "+fileName+" downloaded from "+REMOTE_HOST);
        channelSftp.exit();
    } 
    
    public void deleteFile(String REMOTE_HOST, String USERNAME, String PASSWORD, String fileName) throws JSchException, SftpException {
        ChannelSftp channelSftp = startJsch(REMOTE_HOST, USERNAME,PASSWORD);
        channelSftp.connect();

        String containerFile = "/root/"+fileName+".zip";

        channelSftp.rm(containerFile);

        System.out.println(fileName+" deleted from "+REMOTE_HOST);
        channelSftp.exit();
    } 
    
}


