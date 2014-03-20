package main.meetingRequest;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import main.RegisterSingleton;
import main.calendar.CalendarController;
import model.Appointment;
import util.GuiUtils;

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
    public Button attendButton;
    public Button declineButton;
    public Label leaderLabel;
    private CalendarController parentController;


    private Appointment appointment = null;

    public void attendMeetingButtonOnAction(ActionEvent actionEvent) {
        RegisterSingleton.sharedInstance().getRegister().updateAttending(appointment.getAppointmentID(), RegisterSingleton.sharedInstance().getRegister().getUsername(),1);
        GuiUtils.closeWindow(actionEvent);
        parentController.updateCalendarView();
    }

    public void declineMeetingButtonOnAction(ActionEvent actionEvent) {
        RegisterSingleton.sharedInstance().getRegister().updateAttending(appointment.getAppointmentID(), RegisterSingleton.sharedInstance().getRegister().getUsername(),0);
        GuiUtils.closeWindow(actionEvent);
        parentController.updateCalendarView();
    }

    public void setAppointment(Appointment a){
        this.appointment = a;
        updateView();
    }

    public Appointment getAppointment(){
        return this.appointment;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        updateView();
    }

    public void updateView(){
        if (this.appointment != null){


            nameLabel.setText(appointment.getAppointmentName());
            startLabel.setText(appointment.getAppointmentStart());
            endLabel.setText(appointment.getAppointmentEnd());
            descriptionLabel.setText(appointment.getDescription());
            leaderLabel.setText(RegisterSingleton.sharedInstance().getRegister().getPersonByUsername(appointment.getOwnerName()).getName());

            if (appointment.getPriority() == 0){
                priorityLabel.setText("Lav prioritet");
            }else if (appointment.getPriority() == 1){
                priorityLabel.setText("Middels prioritet");
            }else{
                priorityLabel.setText("Høy prioritet");
            }

            if(appointment.getRoom().getRoomID() == 1){
                System.out.println("Rom ID: " + appointment.getRoom().getRoomID());
                System.out.println("Rom navn: " + appointment.getAlternativeLocation());
                roomLabel.setText(appointment.getAlternativeLocation());
            }else{
                System.out.println("eh");

                roomLabel.setText(appointment.getRoom().getRoomName());
            }
        }
    }

    public void setParentController(CalendarController m){
        parentController = m;
    }
}