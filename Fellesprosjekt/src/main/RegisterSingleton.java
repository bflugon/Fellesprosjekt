package main;

import net.Client;

/**
 * Created by kristian on 11.03.14.
 */
public class RegisterSingleton {

    private static RegisterSingleton instance = null;
    private Register register;

    protected RegisterSingleton() {
        // Exists only to defeat instantiation.
    }
    public static RegisterSingleton sharedInstance() {
        if(instance == null) {
            instance = new RegisterSingleton();
        }
        return instance;
    }

    public void setRegister(Register register){
        //Burde kanskje legge inn en begrensning her
        if (instance.register == null){
            return;
        }
        instance.register = register;
    }

    public Register getRegister(){
        //Burde kanskje legge inn en begrensning her
        return instance.register;
    }
}
