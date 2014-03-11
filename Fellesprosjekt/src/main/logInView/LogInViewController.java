package main.logInView;

import db.DatabaseHandlerSingleton;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;

import javafx.scene.effect.Glow;
import javafx.scene.image.ImageView;
import util.GuiUtils;

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
            return;
        }

        statusLabel.setVisible(false);
        progressIndicator.setVisible(true);

//        if (DatabaseHandlerSingleton.getInstance().authenticate(usernameTextField.getText(), passwordField.getText()){
//            //Add user credentials to register
//            GuiUtils.createView("../calendar/calendar.fxml", "Kalender", this.getClass());
//            ((Node) (actionEvent.getSource())).getScene().getWindow().hide();
//        }else{
//            statusLabel.setText("Kunne ikke logge inn. Prøv igjen.");
//        }

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


}
