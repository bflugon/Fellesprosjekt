package main.registerNewUser;

import javafx.event.ActionEvent;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import main.RegisterSingleton;

/**
 * Created by Markus Lund on 18.03.14.
 */
public class RegisterNewUserViewController {
    public TextField nameTextField;
    public TextField usernameTextField;
    public TextField emailTextField;
    public PasswordField passwordTextField;
    public PasswordField repeatPasswordTextField;


    public void registerNewUserButtonOnAction(ActionEvent actionEvent) {

        if(nameTextField.getText().equals("") ||
                usernameTextField.getText().equals("") ||
                emailTextField.getText().equals("") ||
                passwordTextField.getText().equals("") ||
                repeatPasswordTextField.getText().equals("") ||
                !passwordTextField.getText().equals(repeatPasswordTextField.getText())){
            System.out.println("Something is wrong!");
        }else{
            RegisterSingleton.sharedInstance().getRegister().createAccount(usernameTextField.getText(),passwordTextField.getText(),nameTextField.getText(),emailTextField.getText());
        }

    }
}
