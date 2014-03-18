package main.meetingRequest;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import model.Appointment;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Markus Lund on 18.03.14.
 */
public class MeetingRequestViewController implements Initializable {
    public Label nameLabel;
    public Label startLabel;
    public Label endLabel;
    public Label descriptionLabel;
    public Label priorityLabel;
    public Label roomLabel;

    private Appointment appointment = null;

    public void attendMeetingButtonOnAction(ActionEvent actionEvent) {
        //Setter status til deltar
        //lukker deretter vinduet
    }

    public void declineMeetingButtonOnAction(ActionEvent actionEvent) {
        //setter status til deltar ikke
        //lukker deretter vinduet
    }

    public void setAppointment(Appointment a){
        this.appointment = a;
    }

    public Appointment getAppointment(){
        return this.appointment;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if (this.appointment != null){
            nameLabel.setText(appointment.getAppointmentName());
            startLabel.setText(appointment.getAppointmentStart());
            endLabel.setText(appointment.getAppointmentEnd());
            descriptionLabel.setText(appointment.getDescription());

            if (appointment.getPriority() == 0){
                priorityLabel.setText("Lav prioritet");
            }else if (appointment.getPriority() == 1){
                priorityLabel.setText("Middels prioritet");
            }else{
                priorityLabel.setText("Høy prioritet");
            }

            if(appointment.getRoom().getRoomID() == 1){
                roomLabel.setText(appointment.getAlternativeRoomName());
            }else{
                roomLabel.setText(appointment.getRoom().getRoomName());
            }
        }
    }
}
