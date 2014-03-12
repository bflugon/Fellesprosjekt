package main.meeting;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import main.roomFinder.RoomFinderController;
import model.Appointment;
import util.GuiUtils;
import java.net.URL;
import java.util.ResourceBundle;

public class MeetingController implements Initializable {

    Appointment appointment;
    public Button meetingRoomButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        appointment = new Appointment(0, null, null, null, null, 0, null, null,null, null);
    }

    public void chooseRoom(ActionEvent actionEvent) throws Exception{

        Stage newStage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../roomFinder/roomFinder.fxml"));
        Parent root = (Parent)fxmlLoader.load();
        RoomFinderController roomFinderController = fxmlLoader.<RoomFinderController>getController();
        roomFinderController.setAppointment(appointment);
        roomFinderController.setParentController(this);
        if (appointment.getRoom() != null){
            if (appointment.getRoom().getRoomID() == 1){
                roomFinderController.setRoom(appointment.getAlternativeRoomName());
            }else{
                roomFinderController.setRoom(appointment.getRoom().getRoomName());
            }
        }

        newStage.setTitle("Velg rom");
        newStage.setScene(new Scene(root));
        newStage.show();
    }

    public void chooseParticipants(ActionEvent actionEvent) throws Exception{
        GuiUtils.createView("../participants/participants.fxml", "Velg deltaker", this.getClass());
    }

    public void exitOnSave(ActionEvent actionEvent) {
        GuiUtils.closeWindow(actionEvent);
    }

    public void updateView() {
        if (appointment.getAlternativeRoomName() == null){
            meetingRoomButton.setText(appointment.getRoom().getRoomName());
        }else{
            meetingRoomButton.setText(appointment.getAlternativeRoomName());
        }
    }
}
