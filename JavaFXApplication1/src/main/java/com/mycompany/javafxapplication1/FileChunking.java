/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.javafxapplication1;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.util.List;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.ArrayList;

/**
 *
 * @author ntu-user
 */
public class FileChunking {
    
    public void splitFile(String path, String fileName) throws FileNotFoundException, IOException{
        File originalFile = new File(path);
        long fileSize = originalFile.length();
        long chunkSize = fileSize/4;// split into 4 files, for 4 containers
        
        try(FileInputStream fis = new FileInputStream(originalFile); BufferedInputStream bis = new BufferedInputStream(fis)){
            
            byte[] buffer = new byte[(int) chunkSize];
            int bytesRead;
            int fileCount = 1;
            
            while ((bytesRead = bis.read(buffer)) != -1 && fileCount <= 4){
                File outputFile = new File(originalFile.getParent(), fileName+"_part"+fileCount+".txt");
                
                try (FileOutputStream fos = new FileOutputStream(outputFile); BufferedOutputStream bos = new BufferedOutputStream(fos)){
                    bos.write(buffer, 0, bytesRead);
                }
                fileCount++;
            }
            
        }
        System.out.println(path+" chunked to 4 parts");    
        
    }
    
    public void joinFiles(File[] splitFiles, File outputFile) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(outputFile);
             BufferedOutputStream bos = new BufferedOutputStream(fos)) {

            for (File splitFile : splitFiles) {
                try (FileInputStream fis = new FileInputStream(splitFile);
                     BufferedInputStream bis = new BufferedInputStream(fis)) {

                    byte[] buffer = new byte[(int) splitFile.length()];
                    int bytesRead;
                    while ((bytesRead = bis.read(buffer)) != -1) {
                        bos.write(buffer, 0, bytesRead);
                    }
                }
            }
        }
    
    }
}
