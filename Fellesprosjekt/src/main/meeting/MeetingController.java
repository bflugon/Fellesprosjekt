package main.meeting;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.cell.ComboBoxListCell;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class MeetingController implements Initializable {

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void chooseRoom(ActionEvent actionEvent) throws Exception{
        Stage newStage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("../roomFinder/roomFinder.fxml"));
        newStage.setTitle("Velg rom");
        newStage.setScene(new Scene(root));
        newStage.show();

    }

    public void chooseParticipants(ActionEvent actionEvent) throws Exception{
        Stage newStage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("../participants/participants.fxml"));
        newStage.setTitle("Velg deltaker");
        newStage.setScene(new Scene(root));
        newStage.show();
    }

    public void exitOnSave(ActionEvent actionEvent) {
        ((Node)(actionEvent.getSource())).getScene().getWindow().hide();
    }
}
