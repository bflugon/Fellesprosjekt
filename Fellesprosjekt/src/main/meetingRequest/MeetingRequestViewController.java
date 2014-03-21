package main.meetingRequest;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import main.RegisterSingleton;
import main.calendar.CalendarController;
import model.Appointment;
import model.Person;
import util.GuiUtils;

import java.net.URL;
import java.util.ArrayList;
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
    public Label changedLabel;
    public Label participantsStatusLabel;

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

            int AID = appointment.getAppointmentID();
            leaderLabel.setText(RegisterSingleton.sharedInstance().getRegister().getPersonByUsername(appointment.getOwnerName()).getName());
            String dateChanged = RegisterSingleton.sharedInstance().getRegister().getDateChangedForAppointment(AID);
            System.out.println("Date changed: " + dateChanged + ", for AID: " + AID);
            changedLabel.setText(dateChanged);

            String username = RegisterSingleton.sharedInstance().getRegister().getUsername();
            Person user = RegisterSingleton.sharedInstance().getRegister().getPersonByUsername(username);

            ArrayList<Person> attendingList = RegisterSingleton.sharedInstance().getRegister().getAttendingPeople(AID);
            ArrayList<Person> notAttendingList = RegisterSingleton.sharedInstance().getRegister().getNotAttendingPeople(AID);
            ArrayList<Person> invitedList = RegisterSingleton.sharedInstance().getRegister().getInvitees(AID);
            if (attendingList == null){
                attendingList = new ArrayList<>();
            }
            if (notAttendingList == null){
                notAttendingList = new ArrayList<>();
            }
            if (invitedList == null){
                invitedList = new ArrayList<>();
            }

            for (Person invitedPerson : invitedList){
                if (invitedPerson.getUsername().equals(user.getUsername())){
                    participantsStatusLabel.setText("Du er invitert, men har ikke varslet om du deltar");
                }
            }

            for (Person attendingPerson : attendingList){
                if (attendingPerson.getUsername().equals(user.getUsername())){
                    participantsStatusLabel.setText("Du deltar");
                }
            }
            for (Person notAttendingPerson : notAttendingList){
                if (notAttendingPerson.getUsername().equals(user.getUsername())){
                    participantsStatusLabel.setText("Du deltar ikke");
                }
            }





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