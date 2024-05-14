/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Project/Maven2/JavaApp/src/main/java/${packagePath}/${mainClassName}.java to edit this template
 */
package com.mycompany.javafxapplication1;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.Base64;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;


/**
 *
 * @author ntu-user
 */
public class DB {
    private String filePath = "jdbc:sqlite:/home/ntu-user/App/comp20081.db";
    private int timeout = 30;
    private String dataBaseName = "COMP20081";
    private String dataBaseTableName = "Users";
    private String dataBaseFilesTableName = "UserFiles";
    private String dataBaseErrorsTableName = "UserErrorsContainer";
    private String dataBaseACLTableName = "ACL";
    private String dataBaseEncryptionTableName = "EncryptionKeys";
    private String dataBaseAuditTableName = "AuditLog";
    Connection connection = null;
    private Random random = new SecureRandom();
    private String characters = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private int iterations = 10000;
    private int keylength = 256;
    private String saltValue;
    
    /**
     * @brief constructor - generates the salt if it doesn't exists or load it from the file .salt
     */
    DB() {
        try {
            File fp = new File(".salt");
            if (!fp.exists()) {
                saltValue = this.getSaltvalue(30);
                FileWriter myWriter = new FileWriter(fp);
                myWriter.write(saltValue);
                myWriter.close();
            } else {
                Scanner myReader = new Scanner(fp);
                while (myReader.hasNextLine()) {
                    saltValue = myReader.nextLine();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /*
    ############################################################################
    ############################################################################
    ############################################################################
    
    
    
                                Creating tables
    
    
    
    ############################################################################
    ############################################################################
    ############################################################################
    */
        
    /**
     * @brief create a new table for Users
     * @param tableName name of type String
     */
    public void createTable(String tableName) throws ClassNotFoundException {
        try {
            // create a database connection
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection(filePath);
            var statement = connection.createStatement();
            statement.setQueryTimeout(timeout);
            statement.executeUpdate("create table if not exists " + tableName + "(id integer primary key autoincrement, name string, password string)");

        } catch (SQLException ex) {
            Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                // connection close failed.
                System.err.println(e.getMessage());
            }
        }
    }
    
    /**
     * @brief create a new table for User Files
     * @param tableName 
     */
    public void createFilesTable(String tableName) throws ClassNotFoundException {
        try {
            // create the database connection
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection(filePath);
            var statement = connection.createStatement();
            statement.setQueryTimeout(timeout);
//            statement.executeUpdate("create table if not exists " + tableName + "(fileName string primary key, size integer, date string, modificationDate string, isDeleted boolean default 0, userId integer, foreign key(userId) references Users(id) on delete cascade)");
            statement.executeUpdate("create table if not exists " + tableName + "(fileName string, size integer, date string, modificationDate string, isDeleted boolean default 0, userId integer, primary key (fileName, userId), foreign key(userId) references Users(id) on delete cascade)");
        
        } catch (SQLException ex) {
            Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                System.err.println(e.getMessage());
            }
        }     
    }
    
    /**
     * @brief create a new table for User Container Errors
     * @param tableName 
     */
    public void createErrorsTable(String tableName) throws ClassNotFoundException {
        try {
            // create the database connection
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection(filePath);
            var statement = connection.createStatement();
            statement.setQueryTimeout(timeout);
            statement.executeUpdate("create table if not exists " + tableName + "(error integer primary key autoincrement, containerNumber integer, errorMessage string, date integer, userId integer, foreign key(userId) references Users(id))");
        
        } catch (SQLException ex) {
            Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                System.err.println(e.getMessage());
            }
        }     
    }
    
        /**
     * @brief create a new table for User Container Errors
     * @param tableName 
     */
    public void createACLTable(String tableName) throws ClassNotFoundException {
        try {
            // create the database connection
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection(filePath);
            var statement = connection.createStatement();
            statement.setQueryTimeout(timeout);
            statement.executeUpdate("create table if not exists " + tableName + "(aclId integer primary key autoincrement, readPermission boolean default 0, writePermission boolean default 0, deletePermission boolean default 0, isAdmin boolean default 0, userId integer, foreign key(userId) references Users(id) on delete cascade)");
        
        } catch (SQLException ex) {
            Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                System.err.println(e.getMessage());
            }
        }     
    }
    
        /**
     * @brief create a new table for User Container Errors
     * @param tableName 
     */
    public void createEncryptionTable(String tableName) throws ClassNotFoundException {
        try {
            // create the database connection
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection(filePath);
            var statement = connection.createStatement();
            statement.setQueryTimeout(timeout);
            statement.executeUpdate("create table if not exists " + tableName + "(encryptId integer primary key autoincrement, encryptionKey string, fileId integer)");
//            statement.executeUpdate("create table if not exists " + tableName + "(encryptId integer primary key autoincrement, encryptionKey string, fileId integer, foreign key(fileId) references UserFiles(fileId))");
        
        } catch (SQLException ex) {
            Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                System.err.println(e.getMessage());
            }
        }     
    }
    
    // create the audit table
    /**
     * @brief create a new table for Users
     * @param tableName name of type String
     */
    public void createAuditTable(String tableName) throws ClassNotFoundException {
        try {
            // create a database connection
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection(filePath);
            var statement = connection.createStatement();
            statement.setQueryTimeout(timeout);
            statement.executeUpdate("create table if not exists " + tableName + "(auditId integer primary key autoincrement, timestamp string, action string, change string, userId integer)");

        } catch (SQLException ex) {
            Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                // connection close failed.
                System.err.println(e.getMessage());
            }
        }
    }
    
    
    /*
    ############################################################################
    ############################################################################
    ############################################################################
    
    
    
                                Deleting tables
    
    
    
    ############################################################################
    ############################################################################
    ############################################################################
    */

    
    /**
     * @brief delete table
     * @param tableName of type String
     */
    public void delTable(String tableName) throws ClassNotFoundException {
        try {
            // create a database connection
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection(filePath);
            var statement = connection.createStatement();
            statement.setQueryTimeout(timeout);
            statement.executeUpdate("drop table if exists " + tableName);
        } catch (SQLException ex) {
            Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                // connection close failed.
                System.err.println(e.getMessage());
            }
        }
    }
    
    /*
    ############################################################################
    ############################################################################
    ############################################################################
    
    
    
                                Adding data to tables
    
    
    
    ############################################################################
    ############################################################################
    ############################################################################
    */

    
    // Add data to the user table in database.
    /**
     * @brief add data to the database method
     * @param user name of type String
     * @param password of type String
     * @return userID value of inserted data
     */
        public int addDataToDB(String user, String password) throws InvalidKeySpecException, ClassNotFoundException {
        int generatedUserID = -1;
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection(filePath);
            var statement = connection.createStatement();
            statement.setQueryTimeout(timeout);
            System.out.println("Adding User: " + user + ", Password: " + password);
            int affectedRows = statement.executeUpdate("insert into " + dataBaseTableName + " (name, password) values('" + user + "','" + generateSecurePassword(password) + "')", Statement.RETURN_GENERATED_KEYS);
            
            if (affectedRows > 0) {
                // Retrieve the generated keys
                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        generatedUserID = generatedKeys.getInt(1);  // Assuming userID is an integer
                    }
                }
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                try {
                    if (connection != null) {
                        connection.close();
                    }
                } catch (SQLException e) {
                    // connection close failed.
                    System.err.println(e.getMessage());
                }
            }
        }
        return generatedUserID;
    }
        
    // Add data to the file metadata table in database.    
    /**
     * @brief add data to the database method
     * @param userFileName of type String
     * @param size of type int
     * @param date of type int
     * @param modificationDate of type int
     * @param userId of type int
     * @return fileName of stored file
     */
    public String addDataToDB(String userFileName, long size, String date, String modificationDate, int userId) throws InvalidKeySpecException, ClassNotFoundException {
        String storedFileName = "default";
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection(filePath);
            var statement = connection.createStatement();
            statement.setQueryTimeout(timeout);
            System.out.println("Adding File: " + userFileName);
            int affectedRows = statement.executeUpdate("insert into " + dataBaseFilesTableName + " (fileName, size, date, modificationDate, userId) values('" + userFileName + "','" + size + "','" + date + "','" + modificationDate + "','" + userId + "')");
            if (affectedRows > 0) {
                // Retrieve the generated keys
                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        storedFileName = generatedKeys.getString(1);  // Assuming userID is an integer
                    }
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                try {
                    if (connection != null) {
                        connection.close();
                    }
                } catch (SQLException e) {
                    // connection close failed.
                    System.err.println(e.getMessage());
                }
            }
        }
        return storedFileName;
    }
        
    // Add data to the container errors table in database.    
    /**
     * @brief add data to the database method
     * @param containerNumber of type int
     * @param errorMessage of type String
     * @param date of type int
     * @param userId of type int
     * @return errorId of container error added
     */
    public int addDataToDB(int containerNumber, String errorMessage, int date, int userId) throws InvalidKeySpecException, ClassNotFoundException {
        int generatedErrorID = -1;
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection(filePath);
            var statement = connection.createStatement();
            statement.setQueryTimeout(timeout);
            int affectedRows = statement.executeUpdate("insert into " + dataBaseErrorsTableName + " (containerNumber, errorMessage, date, userId) values('" + containerNumber + "','" + errorMessage + "','" + date + "','" + userId + "')");
            if (affectedRows > 0) {
                // Retrieve the generated keys
                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        generatedErrorID = generatedKeys.getInt(1);  // Assuming userID is an integer
                    }
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                try {
                    if (connection != null) {
                        connection.close();
                    }
                } catch (SQLException e) {
                    // connection close failed.
                    System.err.println(e.getMessage());
                }
            }
        }
        return generatedErrorID;
    }
    
    // Add data to the acl table in database.    
    /**
     * @brief add data to the database method
     * @param readPermission of type Boolean
     * @param writePermission of type Boolean
     * @param deletePermission of type Boolean
     * @param userId of type int
     * @return aclId of user added to acl list
     */
    public int addDataToDB(boolean readPermission, boolean writePermission, boolean deletePermission, int userId) throws InvalidKeySpecException, ClassNotFoundException {
        int generatedACLID = -1;
        int affectedRows;
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection(filePath);
            var statement = connection.createStatement();
            statement.setQueryTimeout(timeout);
            
            // Convert boolean values to integers (1 or 0)
            int readPermissionInt = readPermission ? 1 : 0;
            int writePermissionInt = writePermission ? 1 : 0;
            int deletePermissionInt = deletePermission ? 1 : 0;
            
            if (userId == 1) {
                readPermissionInt = 1;
                writePermissionInt = 1;
                deletePermissionInt = 1;
                int isAdmin = 1;
                affectedRows = statement.executeUpdate("insert into " + dataBaseACLTableName + " (readPermission, writePermission, deletePermission, isAdmin, userId) values('" + readPermissionInt + "','" + writePermissionInt + "','" + deletePermissionInt + "','" + isAdmin + "','" + userId + "')");
            } else {
                affectedRows = statement.executeUpdate("insert into " + dataBaseACLTableName + " (readPermission, writePermission, deletePermission, userId) values('" + readPermissionInt + "','" + writePermissionInt + "','" + deletePermissionInt + "','" + userId + "')");
            }
            
            if (affectedRows > 0) {
                // Retrieve the generated keys
                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        generatedACLID = generatedKeys.getInt(1);  // Assuming userID is an integer
                    }
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                try {
                    if (connection != null) {
                        connection.close();
                    }
                } catch (SQLException e) {
                    // connection close failed.
                    System.err.println(e.getMessage());
                }
            }
        }
        return generatedACLID;
    }
    
    // Add data to the encryption key table in database.    
    /**
     * @brief add data to the database method
     * @param encryptionKey of type String
     * @param fileId of type int
     * @return encryptionKeyId of the encryption key stored
     */
    public int addDataToDB(String encryptionKey, int fileId) throws InvalidKeySpecException, ClassNotFoundException {
        int generatedEncryptionID = -1;
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection(filePath);
            var statement = connection.createStatement();
            statement.setQueryTimeout(timeout);
            int affectedRows = statement.executeUpdate("insert into " + dataBaseEncryptionTableName + " (encryptionKey, fileId) values('" + encryptionKey + "','" + fileId + "')");
            if (affectedRows > 0) {
                // Retrieve the generated keys
                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        generatedEncryptionID = generatedKeys.getInt(1);  // Assuming userID is an integer
                    }
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                try {
                    if (connection != null) {
                        connection.close();
                    }
                } catch (SQLException e) {
                    // connection close failed.
                    System.err.println(e.getMessage());
                }
            }
        }
        return generatedEncryptionID;
    }
    
    // Add data to the audit table in database.    
    /**
     * @brief add data to the database method
     * @param timestamp of type String
     * @param action of type String
     * @param change of type String
     * @param userId of type integer
     * @return encryptionKeyId of the encryption key stored
     */
    public int addDataToDB(String timestamp, String action, String change, int userId) throws InvalidKeySpecException, ClassNotFoundException {
        int generatedAuditID = -1;
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection(filePath);
            var statement = connection.createStatement();
            statement.setQueryTimeout(timeout);
            int affectedRows = statement.executeUpdate("insert into " + dataBaseAuditTableName + " (timestamp, action, change, userId) values('" + timestamp + "','" + action + "','" + change + "','" + userId + "')");
            if (affectedRows > 0) {
                // Retrieve the generated keys
                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        generatedAuditID = generatedKeys.getInt(1);
                    }
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                try {
                    if (connection != null) {
                        connection.close();
                    }
                } catch (SQLException e) {
                    // connection close failed.
                    System.err.println(e.getMessage());
                }
            }
        }
        return generatedAuditID;
    }
    
    
    /*
    ############################################################################
    ############################################################################
    ############################################################################
    
    
    
                             Retrieving All data from tables
    
    
    
    ############################################################################
    ############################################################################
    ############################################################################
    */
        
    /**
     * @brief get data from the Database method
     * @return results as ResultSet
     */
    public ObservableList<User> getDataFromTable() throws ClassNotFoundException {
        ObservableList<User> result = FXCollections.observableArrayList();
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection(filePath);
            var statement = connection.createStatement();
            statement.setQueryTimeout(timeout);
            ResultSet rs = statement.executeQuery("select * from " + this.dataBaseTableName);
            while (rs.next()) {
                // read the result set
                result.add(new User(rs.getInt("id"), rs.getString("name"),rs.getString("password")));
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                // connection close failed.
                System.err.println(e.getMessage());
            }
        }
        return result;
    }
    
    /**
     * @brief get data from the Files Table
     * @param userId
     * @return results as ResultSet
     */
    public ObservableList<UserFile> getDataFromFilesTable(int userId) throws ClassNotFoundException {
        ObservableList<UserFile> result = FXCollections.observableArrayList();
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection(filePath);
            var statement = connection.createStatement();
            statement.setQueryTimeout(timeout);
            ResultSet rs = statement.executeQuery("select * from " + this.dataBaseFilesTableName + " where isDeleted = 0 and userId = " + userId);
            while (rs.next()) {
                // read the result set
                result.add(new UserFile(rs.getString("fileName"), rs.getInt("size"), rs.getString("date"), rs.getString("modificationDate"), rs.getBoolean("isDeleted"), rs.getInt("userId")));
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                // connection close failed.
                System.err.println(e.getMessage());
            }
        }
        return result;
    }
    
    /**
     * @brief get data from the Files Table
     * @return results as ResultSet
     */
    public ObservableList<UserFile> getAllActiveFiles() throws ClassNotFoundException {
        ObservableList<UserFile> result = FXCollections.observableArrayList();
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection(filePath);
            var statement = connection.createStatement();
            statement.setQueryTimeout(timeout);
            ResultSet rs = statement.executeQuery("select * from " + this.dataBaseFilesTableName + " where isDeleted = 0");
            while (rs.next()) {
                // read the result set
                result.add(new UserFile(rs.getString("fileName"), rs.getInt("size"), rs.getString("date"), rs.getString("modificationDate"), rs.getBoolean("isDeleted"), rs.getInt("userId")));
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                // connection close failed.
                System.err.println(e.getMessage());
            }
        }
        return result;
    }
    
    /**
     * @brief get data from the Files Table
     * @return results as ResultSet
     */
    public ObservableList<UserFile> getRemovedFiles(int userId) throws ClassNotFoundException {
        ObservableList<UserFile> result = FXCollections.observableArrayList();
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection(filePath);
            var statement = connection.createStatement();
            statement.setQueryTimeout(timeout);
            ResultSet rs = statement.executeQuery("select * from " + this.dataBaseFilesTableName + " where isDeleted = 1 and userId = " + userId);
            while (rs.next()) {
                // read the result set
                result.add(new UserFile(rs.getString("fileName"), rs.getInt("size"), rs.getString("date"), rs.getString("modificationDate"), rs.getBoolean("isDeleted"), rs.getInt("userId")));
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                // connection close failed.
                System.err.println(e.getMessage());
            }
        }
        return result;
    }
    
    /**
     * @brief get data from the Files Table
     * @retunr results as ResultSet
     */
    public ObservableList<UserContainerError> getDataFromErrorsTable() throws ClassNotFoundException {
        ObservableList<UserContainerError> result = FXCollections.observableArrayList();
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection(filePath);
            var statement = connection.createStatement();
            statement.setQueryTimeout(timeout);
            ResultSet rs = statement.executeQuery("select * from " + this.dataBaseErrorsTableName);
            while (rs.next()) {
                // read the result set
                result.add(new UserContainerError(rs.getInt("error"), rs.getInt("containerNumber"), rs.getString("errorMessage"), rs.getInt("date"), rs.getInt("userId")));
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                // connection close failed.
                System.err.println(e.getMessage());
            }
        }
        return result;
    }
    
    /**
     * @brief get data from the Files Table
     * @return results as ResultSet
     */
    public ObservableList<UserACL> getDataFromACLTable() throws ClassNotFoundException {
        ObservableList<UserACL> result = FXCollections.observableArrayList();
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection(filePath);
            var statement = connection.createStatement();
            statement.setQueryTimeout(timeout);
            ResultSet rs = statement.executeQuery("select * from " + this.dataBaseACLTableName);
            while (rs.next()) {
                // read the result set
                result.add(new UserACL(rs.getInt("aclId"), rs.getBoolean("readPermission"), rs.getBoolean("writePermission"), rs.getBoolean("deletePermission"), rs.getBoolean("isAdmin"), rs.getInt("userId")));
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                // connection close failed.
                System.err.println(e.getMessage());
            }
        }
        return result;
    }
    
    /**
     * @brief get data from the Files Table
     * @return results as ResultSet
     */
    public ObservableList<UserFileEncryption> getDataFromEncryptionTable() throws ClassNotFoundException {
        ObservableList<UserFileEncryption> result = FXCollections.observableArrayList();
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection(filePath);
            var statement = connection.createStatement();
            statement.setQueryTimeout(timeout);
            ResultSet rs = statement.executeQuery("select * from " + this.dataBaseErrorsTableName);
            while (rs.next()) {
                // read the result set
                result.add(new UserFileEncryption(rs.getInt("encryptionId"), rs.getString("encryptionKey"), rs.getInt("fileId")));
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                // connection close failed.
                System.err.println(e.getMessage());
            }
        }
        return result;
    }
    
    /**
     * @brief get the latest row added to ACL table.
     * @return results as ResultSet
     */
    public ObservableList<User> getLatestUserRow() throws ClassNotFoundException {
        ObservableList<User> result = FXCollections.observableArrayList();
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection(filePath);
            var statement = connection.createStatement();
            statement.setQueryTimeout(timeout);
            ResultSet rs = statement.executeQuery("select * from " + this.dataBaseTableName + " order by rowid desc limit 1");
            while (rs.next()) {
                // read the result set
                result.add(new User(rs.getInt("id"), rs.getString("name"),rs.getString("password")));
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                // connection close failed.
                System.err.println(e.getMessage());
            }
        }
        return result;
    }
    
    /**
     * @brief get data from the Files Table
     * @return results as ResultSet
     */
    public ObservableList<AuditEntry> getAuditLog() throws ClassNotFoundException {
        ObservableList<AuditEntry> result = FXCollections.observableArrayList();
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection(filePath);
            var statement = connection.createStatement();
            statement.setQueryTimeout(timeout);
            ResultSet rs = statement.executeQuery("select * from " + this.dataBaseAuditTableName);
            while (rs.next()) {
                // read the result set
                result.add(new AuditEntry(rs.getInt("auditId"), rs.getString("timestamp"), rs.getString("action"), rs.getString("change"), rs.getInt("userId")));
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                // connection close failed.
                System.err.println(e.getMessage());
            }
        }
        return result;
    }
    
    /*
    ############################################################################
    ############################################################################
    ############################################################################
    
    
    
                        Get specific row of data
    
    
    
    ############################################################################
    ############################################################################
    ############################################################################
    */
    
    // select single row from user table
    /**
     * @brief get data from the User Database method
     * @param username
     * @retunr results as ResultSet
     */
    public User getSingleUserFromTable(String username) throws ClassNotFoundException {
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection(filePath);
            var statement = connection.createStatement();
            statement.setQueryTimeout(timeout);
//          ResultSet rs = statement.executeQuery("select * from " + this.dataBaseTableName + " where name = " + username + " and password = " + password);
            String query = "SELECT * FROM " + this.dataBaseTableName + " WHERE name = ?";
            try (PreparedStatement pstmt = connection.prepareStatement(query)) {
                pstmt.setString(1, username);
//                pstmt.setString(2, password);
                ResultSet rs = pstmt.executeQuery();
                if (rs.next()) {
                    // User found, return the User object
                    System.out.println("User found.");
                    return new User(rs.getInt("id"), rs.getString("name"), rs.getString("password"));
                }
            }
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                System.err.println(e.getMessage());
            }
        }
        System.out.println("No User Found.");
        return null;
    }
    
    // select single row from user table
    /**
     * @brief get data from the User Database method
     * @param fileName
     * @return results as ResultSet
     */
    public UserFile getSingleFileFromFileTable(String fileName) throws ClassNotFoundException {
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection(filePath);
            var statement = connection.createStatement();
            statement.setQueryTimeout(timeout);
//          ResultSet rs = statement.executeQuery("select * from " + this.dataBaseTableName + " where name = " + username + " and password = " + password);
            String query = "SELECT * FROM " + this.dataBaseFilesTableName + " WHERE fileName = ?";
            try (PreparedStatement pstmt = connection.prepareStatement(query)) {
                pstmt.setString(1, fileName);
//                pstmt.setString(2, password);
                ResultSet rs = pstmt.executeQuery();
                if (rs.next()) {
                    // File found, return the File object
                    System.out.println("File found.");
                    return new UserFile(rs.getString("fileName"), rs.getInt("size"), rs.getString("date"), rs.getString("modificationDate"), rs.getBoolean("isDeleted"), rs.getInt("userId"));
                }
            }
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                System.err.println(e.getMessage());
            }
        }
        System.out.println("No User Found.");
        return null;
    }
    
     // select single row from user acl table
    /**
     * @brief get data from the User Database method
     * @param userId
     * @retunr results as ResultSet
     */
    public UserACL getSingleUserACLFromTable(int userId) throws ClassNotFoundException {
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection(filePath);
            var statement = connection.createStatement();
            statement.setQueryTimeout(timeout);
            String query = "SELECT * FROM " + this.dataBaseACLTableName + " WHERE userId = ?";
            try (PreparedStatement pstmt = connection.prepareStatement(query)) {
                pstmt.setString(1, String.valueOf(userId));
                ResultSet rs = pstmt.executeQuery();
                if (rs.next()) {
                    // UserACL found, return the UserACL object
                    System.out.println("ACL Permissions Found.");
                    return new UserACL(rs.getInt("aclId"), rs.getBoolean("readPermission"), rs.getBoolean("writePermission"), rs.getBoolean("deletePermission"), rs.getBoolean("isAdmin"), rs.getInt("userId"));
                }
            }
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                System.err.println(e.getMessage());
            }
        }
        System.out.println("No ACL Permissions Found.");
        return null;
    }
    
    /*
    ############################################################################
    ############################################################################
    ############################################################################
    
    
    
                                Updating Tables
    
    
    
    ############################################################################
    ############################################################################
    ############################################################################
    */
    

    // User table
    
    /**
     * @brief update the User Table for the provided UserID, with no password changes.
     * @param userId
     * @param user
     */
    public void updateUserTable(int userId, String user) throws ClassNotFoundException, InvalidKeySpecException {
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection(filePath);
            var statement = connection.createStatement();
            statement.setQueryTimeout(timeout);
            
            String updateQuery = "UPDATE " + dataBaseTableName + " SET name = ? WHERE id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(updateQuery);
            preparedStatement.setString(1, user);
            preparedStatement.setString(2, String.valueOf(userId));
            int rowsUpdated = preparedStatement.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("User Table Update successful!");
            } else {
                System.out.println("No User Table Rows Were Updated.");
            }
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                System.err.println(e.getMessage());
            }
        }
    }
    
    /**
     * @brief update the User Table for the provided UserID
     * @param userId
     * @param user
     * @param password
     */
    public void updateUserTable(int userId, String user, String password) throws ClassNotFoundException, InvalidKeySpecException {
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection(filePath);
            var statement = connection.createStatement();
            statement.setQueryTimeout(timeout);
            
            String updateQuery = "UPDATE " + dataBaseTableName + " SET name = ?, password = ? WHERE id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(updateQuery);
            preparedStatement.setString(1, user);
            preparedStatement.setString(2, generateSecurePassword(password));
            preparedStatement.setString(3, String.valueOf(userId));
            int rowsUpdated = preparedStatement.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("User Table Update successful!");
            } else {
                System.out.println("No User Table Rows Were Updated.");
            }
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                System.err.println(e.getMessage());
            }
        }
    }    
    
    
    // ACL Table
    /**
     * @brief update the ACL Table row for provided UserID
     * @param userId
     * @param readPermission
     * @param writePermission
     * @param deletePermission
     */
    public void updateACLTable(int userId, boolean readPermission, boolean writePermission, boolean deletePermission) throws ClassNotFoundException, InvalidKeySpecException {
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection(filePath);
            var statement = connection.createStatement();
            statement.setQueryTimeout(timeout);
            
            int readPermissionInt = readPermission ? 1 : 0;
            int writePermissionInt = writePermission ? 1 : 0;
            int deletePermissionInt = deletePermission ? 1 : 0;
            
            String updateQuery = "UPDATE " + dataBaseACLTableName + " SET readPermission = ?, writePermission = ?, deletePermission = ? WHERE userId = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(updateQuery);
            preparedStatement.setInt(1, readPermissionInt);
            preparedStatement.setString(2, String.valueOf(writePermissionInt));
            preparedStatement.setString(3, String.valueOf(deletePermissionInt));
            preparedStatement.setString(4, String.valueOf(userId));
            int rowsUpdated = preparedStatement.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("ACL Table Update successful!");
            } else {
                System.out.println("No ACL Table Rows Were Updated.");
            }
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                System.err.println(e.getMessage());
            }
        }
    }   
    
    // update acl admin
    /**
     * @brief update the ACL Table admin row for provided UserID
     * @param userId
     * @param isAdmin
     */
    public void updateACLAdminTable(int userId, boolean isAdmin) throws ClassNotFoundException, InvalidKeySpecException {
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection(filePath);
            var statement = connection.createStatement();
            statement.setQueryTimeout(timeout);
            
            int isAdminInt = isAdmin ? 1 : 0;
            
            String updateQuery = "UPDATE " + dataBaseACLTableName + " SET isAdmin = ? WHERE userId = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(updateQuery);
            preparedStatement.setInt(1, isAdminInt);
            preparedStatement.setString(2, String.valueOf(userId));
            int rowsUpdated = preparedStatement.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("ACL Table Update successful!");
            } else {
                System.out.println("No ACL Table Rows Were Updated.");
            }
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                System.err.println(e.getMessage());
            }
        }
    }    
    
    // File table
    /**
     * @brief update the File Table for the provided FileID
     * @param userFileName
     * @param size
     * @param modificationDate
     */
    public void updateFileTable(String userFileName, /*int size,*/ String modificationDate) throws ClassNotFoundException, InvalidKeySpecException {
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection(filePath);
            var statement = connection.createStatement();
            statement.setQueryTimeout(timeout);
            
            String updateQuery = "UPDATE " + dataBaseFilesTableName + " SET fileName = ?, modificationDate = ? WHERE fileName = ?";
//            String updateQuery = "UPDATE " + dataBaseFilesTableName + " SET fileName = ?, size = ?, modfificationDate = ? WHERE fileName = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(updateQuery);
            preparedStatement.setString(1, userFileName);
//            preparedStatement.setString(2, String.valueOf(size));
            preparedStatement.setString(2, modificationDate);
            preparedStatement.setString(3, userFileName);
            int rowsUpdated = preparedStatement.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("File Table Update successful!");
            } else {
                System.out.println("No File Table Rows Were Updated.");
            }
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                System.err.println(e.getMessage());
            }
        }
    } 
    
    /**
     * @brief update the File Table for the provided FileID
     * @param userFileName
     * @param isDeleted
     */
    public void updateRemovedFileTable(String userFileName, boolean isDeleted) throws ClassNotFoundException, InvalidKeySpecException {
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection(filePath);
            var statement = connection.createStatement();
            statement.setQueryTimeout(timeout);
            
            int isDeletedInt = isDeleted ? 1 : 0;
            
            String updateQuery = "UPDATE " + dataBaseFilesTableName + " SET isDeleted = ? WHERE fileName = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(updateQuery);
            preparedStatement.setInt(1, isDeletedInt);
            preparedStatement.setString(2, userFileName);
            int rowsUpdated = preparedStatement.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("File Table Update successful!");
            } else {
                System.out.println("No File Table Rows Were Updated.");
            }
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                System.err.println(e.getMessage());
            }
        }
    }
    
    // Encryption table
    /**
     * @brief update the Encryption Table for the provided FileID
     * @param encryptId
     * @param encryptKey
     * @param fileId
     */
    public void updateEncryptionTable(String encryptId, String encryptKey, String fileId) throws ClassNotFoundException, InvalidKeySpecException {
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection(filePath);
            var statement = connection.createStatement();
            statement.setQueryTimeout(timeout);
            
            String updateQuery = "UPDATE " + dataBaseEncryptionTableName + " SET encryptionKey = ? WHERE fileId = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(updateQuery);
            preparedStatement.setString(1, encryptKey);
            preparedStatement.setString(2, fileId);
            int rowsUpdated = preparedStatement.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Encryption Table Update successful!");
            } else {
                System.out.println("No Encryption Table Rows Were Updated.");
            }
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                System.err.println(e.getMessage());
            }
        }
    }    
    
    // Container Error table
    /**
     * @brief update the File Table for the provided FileID
     * @param errorId
     * @param errorMessage
     */
    public void updateErrorTable(int errorId, String errorMessage) throws ClassNotFoundException, InvalidKeySpecException {
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection(filePath);
            var statement = connection.createStatement();
            statement.setQueryTimeout(timeout);
            
            String updateQuery = "UPDATE " + dataBaseErrorsTableName + " SET errorMessage = ? WHERE error = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(updateQuery);
            preparedStatement.setString(1, errorMessage);
            preparedStatement.setString(2, String.valueOf(errorId));
            int rowsUpdated = preparedStatement.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Container Error Table Update successful!");
            } else {
                System.out.println("No Error Table Rows Were Updated.");
            }
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                System.err.println(e.getMessage());
            }
        }
    }
    
    
    /*
    ############################################################################
    ############################################################################
    ############################################################################
    
    
    
                            Remove existing data
    
    
    
    ############################################################################
    ############################################################################
    ############################################################################
    */
    
    
    // User table
    /**
     * @brief get data from the User Database method
     * @param userId
     */
    public void removeUserFromTable(int userId) throws ClassNotFoundException {
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection(filePath);
            var statement = connection.createStatement();
            statement.setQueryTimeout(timeout);
            
            String deleteQuery = "DELETE FROM " + dataBaseTableName + " WHERE id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(deleteQuery);
            preparedStatement.setInt(1, userId);
            
            int rowsDeleted = preparedStatement.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("User successfully deleted from " + dataBaseTableName + ".");
            } else {
                System.out.println("No rows were deleted.");
            }

        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                System.err.println(e.getMessage());
            }
        }
    }
    
    
    // ACL table
    /**
     * @brief get data from the User Database method
     * @param userID
     */
    public void removeUserACLFromTable(int userID) throws ClassNotFoundException {
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection(filePath);
            var statement = connection.createStatement();
            statement.setQueryTimeout(timeout);
            
            String deleteQuery = "DELETE FROM " + dataBaseACLTableName + " WHERE userId = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(deleteQuery);
            preparedStatement.setString(1, String.valueOf(userID));
            
            int rowsDeleted = preparedStatement.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("ACL record successfully deleted from " + dataBaseACLTableName + ".");
            } else {
                System.out.println("No rows were deleted.");
            }

        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                System.err.println(e.getMessage());
            }
        }
    }
    
    
    
    // File table 
    /**
     * @brief get data from the User Database method
     * @param userFileName
     */
    public void removeFileFromTable(String userFileName) throws ClassNotFoundException {
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection(filePath);
            var statement = connection.createStatement();
            statement.setQueryTimeout(timeout);
            
            String deleteQuery = "DELETE FROM " + dataBaseFilesTableName + " WHERE fileName = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(deleteQuery);
            preparedStatement.setString(1, userFileName);
            
            int rowsDeleted = preparedStatement.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("File successfully deleted from " + dataBaseFilesTableName + ".");
            } else {
                System.out.println("No rows were deleted.");
            }

        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                System.err.println(e.getMessage());
            }
        }
    }
    
    
    // Encryption table
    /**
     * @brief get data from the User Database method
     * @param fileID
     */
    public void removeEncryptionFromTable(int fileID) throws ClassNotFoundException {
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection(filePath);
            var statement = connection.createStatement();
            statement.setQueryTimeout(timeout);
            
            String deleteQuery = "DELETE FROM " + dataBaseEncryptionTableName + " WHERE fileId = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(deleteQuery);
            preparedStatement.setString(1, String.valueOf(fileID));
            
            int rowsDeleted = preparedStatement.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("Encryption record successfully deleted from " + dataBaseEncryptionTableName + ".");
            } else {
                System.out.println("No rows were deleted.");
            }

        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                System.err.println(e.getMessage());
            }
        }
    }
    
    
    // Container error table
    /**
     * @brief get data from the User Database method
     * @param errorID
     */
    public void removeContainerErrorFromTable(String errorID) throws ClassNotFoundException {
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection(filePath);
            var statement = connection.createStatement();
            statement.setQueryTimeout(timeout);
            
            String deleteQuery = "DELETE FROM " + dataBaseErrorsTableName + " WHERE error = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(deleteQuery);
            preparedStatement.setString(1, errorID);
            
            int rowsDeleted = preparedStatement.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("Error record successfully deleted from " + dataBaseErrorsTableName + ".");
            } else {
                System.out.println("No rows were deleted.");
            }

        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                System.err.println(e.getMessage());
            }
        }
    }
    
    
    
    
    
    
    
    
    
    
    /*
    ############################################################################
    ############################################################################
    ############################################################################
    
    
    
                                Extra Methods
    
    
    
    ############################################################################
    ############################################################################
    ############################################################################
    */
    
    /**
     * @brief decode password method
     * @param user name as type String
     * @param pass plain password of type String
     * @return true if the credentials are valid, otherwise false
     */
    public boolean validateUser(String user, String pass) throws InvalidKeySpecException, ClassNotFoundException {
        Boolean flag = false;
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection(filePath);
            var statement = connection.createStatement();
            statement.setQueryTimeout(timeout);
            ResultSet rs = statement.executeQuery("select name, password from " + this.dataBaseTableName);
            String inPass = generateSecurePassword(pass);
            // Let's iterate through the java ResultSet
            while (rs.next()) {
                if (user.equals(rs.getString("name")) && rs.getString("password").equals(inPass)) {
                    flag = true;
                    break;
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                // connection close failed.
                System.err.println(e.getMessage());
            }
        }

        return flag;
    }

    
    private String getSaltvalue(int length) {
        StringBuilder finalval = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            finalval.append(characters.charAt(random.nextInt(characters.length())));
        }

        return new String(finalval);
    }

    /* Method to generate the hash value */
    private byte[] hash(char[] password, byte[] salt) throws InvalidKeySpecException {
        PBEKeySpec spec = new PBEKeySpec(password, salt, iterations, keylength);
        Arrays.fill(password, Character.MIN_VALUE);
        try {
            SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            return skf.generateSecret(spec).getEncoded();
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new AssertionError("Error while hashing a password: " + e.getMessage(), e);
        } finally {
            spec.clearPassword();
        }
    }

    public String generateSecurePassword(String password) throws InvalidKeySpecException {
        String finalval = null;

        byte[] securePassword = hash(password.toCharArray(), saltValue.getBytes());

        finalval = Base64.getEncoder().encodeToString(securePassword);

        return finalval;
    }
    
    public void checkDirectory(String name, String path){
        File directory = new File(path);
        
        if(!directory.exists()){
            System.out.println(name+" directory doesn't exist, creating directory");
            new File(path).mkdirs();
        }
        else{
            System.out.println(name+" directory already exists");
        }
    }
    
    public boolean usernameCheck(String usernameToCheck) {
        try {
            ObservableList<User> userList = getDataFromTable();

            // Iterate through the list and check for the existence of the username
            for (User user : userList) {
                if (user.getUser().equals(usernameToCheck)) {
                    return true;  // Username already exists
                }
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return false;  // Username does not exist
    }
    
    public boolean filenameCheck(String filenameToCheck) {
        try {
            ObservableList<UserFile> fileList = getDataFromFilesTable(UserSession.getCurrentUserID());

            // Iterate through the list and check for the existence of the filename
            for (UserFile file : fileList) {
                if (file.getFileName().equals(filenameToCheck)) {
                    return true;  // File already exists
                }
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return false;  // File does not exist
    }
    
    public int getUserIdByUsername(String usernameToFind) {
        try {
            ObservableList<User> userList = getDataFromTable();

            // Iterate through the list and search for the userId based on the username
            for (User user : userList) {
                if (user.getUser().equals(usernameToFind)) {
                    return user.getUserID();  // Return the userId associated with the username
                }
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return -1;  // Return a value indicating that the username was not found
    }

    /**
     * @brief get users table name
     * @return table name as String
     */
    public String getTableName() {
        return this.dataBaseTableName;
    }
    
    /**
     * @brief get user files table name
     * @return table name as String
     */
    public String getFilesTableName() {
        return this.dataBaseFilesTableName;
    }
    
    /**
     * @brief get user errors table name
     * @return table name as String
     */
    public String getErrorsTableName() {
        return this.dataBaseErrorsTableName;
    }
    
    /**
     * @brief get user acl table name
     * @return table name as String
     */
    public String getACLTableName() {
        return this.dataBaseACLTableName;
    }
    
    /**
     * @brief get encryption key table name
     * @return table name as String
     */
    public String getEncryptionTableName() {
        return this.dataBaseEncryptionTableName;
    }
    
    /**
     * @brief get audit log table name
     * @return table name as String
     */
    public String getAuditTableName() {
        return this.dataBaseAuditTableName;
    }

    
    /**
     * @brief print a message on screen method
     * @param message of type String
     */
    public void log(String message) {
        System.out.println(message);

    }
    
    public void displayAuditTrail() throws ClassNotFoundException, SQLException {
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection(filePath);
            var statement = connection.createStatement();
            statement.setQueryTimeout(timeout);
            
            String selectAuditSQL = "select * from " + dataBaseAuditTableName;
            
            ResultSet resultSet = statement.executeQuery(selectAuditSQL);

            List<String> entries = new ArrayList<>();
            while (resultSet.next()) {
                int id = resultSet.getInt("AuditId");
                String timestamp = resultSet.getString("timestamp");
                String action = resultSet.getString("action");
                String change = resultSet.getString("change");
                int userId = resultSet.getInt("userId");

                String entry = String.format(id + ":[%s] %s: %s - ID: %d", timestamp, action, change, userId);
                entries.add(entry);
            }

            if (!entries.isEmpty()) {
                entries.forEach(System.out::println);
            } else {
                System.out.println("Audit trail is empty.");
            }
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                System.err.println(e.getMessage());
            }
        }
    }
}
