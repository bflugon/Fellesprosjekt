package db;

import java.sql.*;

/**
 * Created with IntelliJ IDEA.
 * User: FF63
 * Date: 3/5/14
 * Time: 6:21 PM
 * To change this template use File | Settings | File Templates.
 */
public class DatabaseSettings {
    private static final String db_url = "jdbc:mysql://localhost:3306/fellesprosjekt";
    private static final String db_username = "herp";
    private static final String db_password = "derp";

    public static String getURL(){
        return db_url;
    }

    public static String getUsername(){
        return db_username;
    }

    public static String getPassword(){
        return db_password;
    }
}