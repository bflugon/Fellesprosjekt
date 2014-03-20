package model;
/**
 * Created with IntelliJ IDEA.
 * User: FF63
 * Date: 3/13/14
 * Time: 2:02 PM
 * To change this template use File | Settings | File Templates.
 */
public class Config {
    private static final String ip = "178.79.173.4";
    private static final String smtpUsername = "fellesprosjektgruppe31@gmail.com";
    private static final String smtpPassword = "fppassword";

    public static String getIp(){
        return ip;
    }

    public static String getSmtpUsername(){
        return smtpUsername;
    }

    public static String getSmtpPassword(){
        return smtpPassword;
    }

}