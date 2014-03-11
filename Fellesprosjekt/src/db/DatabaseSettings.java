package db;

import util.GeneralUtil;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: FF63
 * Date: 3/5/14
 * Time: 6:21 PM
 * To change this template use File | Settings | File Templates.
 */
public class DatabaseSettings {
    private static final ArrayList<String> databaseInfo = GeneralUtil.readFile("database.txt");
    private static final String db_url = databaseInfo.get(0);
    private static final String db_username = databaseInfo.get(1);
    private static final String db_password = databaseInfo.get(2);

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
