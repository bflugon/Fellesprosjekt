package main.logInView;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.effect.Glow;
import javafx.scene.image.ImageView;
import main.GuiUtils;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * Created by markuslund92 on 06.03.14.
 */
public class LogInViewController{

    public ImageView glowFrogImageView;


    public void frogGlowOn(){
       glowFrogImageView.setEffect(new Glow(0.8));
    }
    public void frogGlowOff(){
        glowFrogImageView.setEffect(new Glow(0.0));
    }

    public void logInButtonOnAction(ActionEvent actionEvent) throws Exception{
        if (true){
            GuiUtils.createView("../calendar/calendar.fxml", "Kalender", this.getClass());
            ((Node) (actionEvent.getSource())).getScene().getWindow().hide();
        }else{

        }


    }
}
