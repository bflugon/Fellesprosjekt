package main.logInView;

//import com.javafx.tools.doclets.formats.html.SourceToHTMLConverter;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;

import javafx.scene.effect.Glow;
import javafx.scene.image.ImageView;
import main.Register;
import util.GuiUtils;

import main.Register;
import main.RegisterSingleton;
import main.Main;
import net.Client;

import java.awt.*;

/**
 * Created by markuslund92 on 06.03.14.
 */
public class LogInViewController{


    public ImageView glowFrogImageView;
    public TextField usernameTextField;
    public PasswordField passwordField;
    public Label statusLabel;
    public ProgressIndicator progressIndicator;



    public void frogGlowOn(){
       glowFrogImageView.setEffect(new Glow(0.8));
    }
    public void frogGlowOff(){
        glowFrogImageView.setEffect(new Glow(0.0));
    }

    public void logInButtonOnAction(ActionEvent actionEvent) throws Exception{

        if (emptyUsernameTextField() && emptyPasswordField()){
            statusLabel.setText("Fyll inn brukernavn og passord");

            return;
        }else if (emptyUsernameTextField()){
            statusLabel.setText("Fyll inn brukernavn");
            return;
        }else if (emptyPasswordField()){
            statusLabel.setText("Fyll inn passord");
            //return;
        }

        statusLabel.setVisible(false);
        progressIndicator.setVisible(true);

        if(RegisterSingleton.sharedInstance().getRegister() == null){
            System.out.println("Setter registerert i log in");

            RegisterSingleton.sharedInstance().setRegister(new Register(new Client()));
        }

        System.out.print(RegisterSingleton.sharedInstance().getRegister());


        if (RegisterSingleton.sharedInstance().getRegister().authenticate(usernameTextField.getText(), passwordField.getText())){

            RegisterSingleton.sharedInstance().setInitialRegisterData();

            GuiUtils.createView("../calendar/calendar.fxml", "Kalender", this.getClass());
            ((Node) (actionEvent.getSource())).getScene().getWindow().hide();
        }else{
            statusLabel.setVisible(true);
            progressIndicator.setVisible(false);
            System.out.println("Login fail" + usernameTextField.getText() + passwordField.getText());
            statusLabel.setText("Kunne ikke logge inn. Prøv igjen.");
        }
    }

    private boolean emptyUsernameTextField(){
        if (usernameTextField.getText().equals("")){
            System.out.println("Empty username");
            return true;
        }else{

            System.out.println("Not empty username");
            return false;
        }
    }
    private boolean emptyPasswordField(){
        if (passwordField.getText().equals("")){
            System.out.println("Empty password");
            return true;
        }else{
            System.out.println("Not empty password");
            return false;
		}
    }


    public void usernameTextFieldOnAction(ActionEvent actionEvent) throws Exception{
        logInButtonOnAction(actionEvent);
    }

    public void passwordFieldTextField(ActionEvent actionEvent) throws Exception{
        logInButtonOnAction(actionEvent);

    }

    public void registerButtonOnAction(ActionEvent actionEvent) throws Exception{
        if(RegisterSingleton.sharedInstance().getRegister() == null){
            System.out.println("Setter registerert i ny bruker");
            RegisterSingleton.sharedInstance().setRegister(new Register(new Client()));
        }

        GuiUtils.createView("../registerNewUser/registerNewUser.fxml", "Registrer ny bruker", this.getClass());

    }
}
