package main.meeting;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import main.GuiUtils;

import java.net.URL;
import java.util.ResourceBundle;

public class MeetingController implements Initializable {

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void chooseRoom(ActionEvent actionEvent) throws Exception{
        GuiUtils.createView("../roomFinder/roomFinder.fxml", "Velg rom", this.getClass());
    }

    public void chooseParticipants(ActionEvent actionEvent) throws Exception{
        GuiUtils.createView("../participants/participants.fxml", "Velg deltaker", this.getClass());
    }

    public void exitOnSave(ActionEvent actionEvent) {
        ((Node)(actionEvent.getSource())).getScene().getWindow().hide();
    }
}
