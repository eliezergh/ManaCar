package com.eliezergh.manacar;

import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class dbConnect {
    //Connection data
    private static final String driver = "com.mysql.jdbc.Driver";
    private static final String user = "alu9";
    private static final String pwd = "alu9*12345";
    private static final String url = "jdbc:mysql://51.38.237.31:3306/alu9";
    private static Connection conn;

    public Connection connect(){
        conn = null;
        try{
            Class.forName(driver);
            conn = DriverManager.getConnection("jdbc:mysql://51.38.237.31:3306/alu9", "alu9", "alu9*12345");
            System.out.println("after connection line");
            if (conn!=null){
                Log.i("DB Connection: ", "CONNECTION SUCCESFUL");
            } else {
                Log.e("DB Connection: ", "CONNECTION FAILED");
            }
        } catch (SQLException | ClassNotFoundException e){
            System.out.println("MySQL Connection: "+e.getMessage());
        }
        return conn;
    }
}
