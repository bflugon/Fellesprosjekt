package main.meeting;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import main.RegisterSingleton;
import main.calendar.CalendarController;
import main.roomFinder.RoomFinderController;
import model.Appointment;
import model.Person;
import util.GeneralUtil;
import util.GuiUtils;

import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.ResourceBundle;

public class MeetingController implements Initializable {


    Appointment appointment;

    //Deklarerer GUIelementer
    public Button meetingRoomButton;
    public TextField startTimeTextField;
    public TextField startDateTextField;
    public TextField endTimeTextField;
    public TextField endDateTextField;
    public TextField nameTextField;
    public RadioButton lowPriRadioButton;
    public RadioButton mediumPriRadioButton;
    public RadioButton highPriRadioButton;
    public TextArea descriptionTextArea;
    public ToggleGroup priorityToggleGroup;

    private int numberOfInvited;
    private boolean isEditable;


    private CalendarController parent;



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {


        appointment = new Appointment(0, null, null, null, null, 0, null, null,null, null);
    }

    public void nameTextFieldType(){
        nameTextField.setStyle("-fx-border-width: 0px;");
    }
    public void startTimeTextFieldType(){
        startTimeTextField.setStyle("-fx-border-width: 0px;");
    }
    public void startDateTextFieldType(){
        startDateTextField.setStyle("-fx-border-width: 0px;");
        endDateTextField.setText(startDateTextField.getText());
    }
    public void startDateTextFieldReleased(){
        endDateTextField.setText(startDateTextField.getText());
    }
    public void endTimeTextFieldType(){
        endTimeTextField.setStyle("-fx-border-width: 0px;");
    }
    public void endDateTextFieldType(){
        endDateTextField.setStyle("-fx-border-width: 0px;");
    }

    public void chooseRoom(ActionEvent actionEvent) throws Exception{

        meetingRoomButton.setStyle("-fx-border-width: 0px;");
        Stage newStage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../roomFinder/roomFinder.fxml"));
        Parent root = (Parent)fxmlLoader.load();
        RoomFinderController roomFinderController = fxmlLoader.<RoomFinderController>getController();
        roomFinderController.setAppointment(appointment);
        roomFinderController.setParentController(this);

        //Sender med riktig rom hvis det eksisterer
        if (appointment.getRoom() != null){
            if (appointment.getRoom().getRoomID() == 1){
                roomFinderController.setRoom(appointment.getAlternativeLocation());
            }else{
                roomFinderController.setRoom(appointment.getRoom().getRoomName());
            }
        }

        //Sender med antall personer for å finne størrelse på rom
        roomFinderController.setMinCapacity(getNumberOfInvited());

        newStage.setTitle("Velg rom");
        newStage.setScene(new Scene(root));
        newStage.show();
    }


    public void setParent(CalendarController parent) {
        this.parent = parent;
    }


    public void chooseParticipants(ActionEvent actionEvent) throws Exception{
        GuiUtils.createView("../participants/participants.fxml", "Velg deltaker", this.getClass());
    }

    public void setEditable(boolean isEditable) {
        this.isEditable = isEditable;
    }


    public void exitOnSave(ActionEvent actionEvent) {
        String redBorderStyling = "-fx-border-color: RED; -fx-border-width: 2px;";


        if (nameTextField.getText().equals("")){
            nameTextField.setPromptText("FYLL INN NAVN!");
            //nameTextField.setEffect(redDropShadow);
            nameTextField.setStyle(redBorderStyling);
        }

        else if(!isValidTime(startTimeTextField.getText())){

            System.out.println("Invalid startTime format: " + startTimeTextField.getText());
            startTimeTextField.setStyle(redBorderStyling);

        }else if(!isValidDate(startDateTextField.getText())){

            System.out.println("Invalid startDate format: " + startDateTextField.getText());
            startDateTextField.setStyle(redBorderStyling);

        }else if(!isValidTime(endTimeTextField.getText())){

            System.out.println("Invalid endTime format: " + endTimeTextField.getText());
            endTimeTextField.setStyle(redBorderStyling);


        }else if(!isValidDate(endDateTextField.getText())){

            System.out.println("Invalid endDate format: " + endDateTextField.getText());
            endDateTextField.setStyle(redBorderStyling);
        }else {
            if (appointment.getRoom() == null) {
                System.out.println("No room selected");
                meetingRoomButton.setStyle(redBorderStyling);

            } else {

                appointment.setAppointmentName(nameTextField.getText());
                appointment.setDescription(descriptionTextArea.getText());

                String startTimeString = startDateTextField.getText() + " " + startTimeTextField.getText() + ":00";
                String endTimeString = endDateTextField.getText() + " " + endTimeTextField.getText() + ":00";

                Date startTime = GeneralUtil.stringToDate(startTimeString);
                Date endTime = GeneralUtil.stringToDate(endTimeString);

                appointment.setAppointmentStart(startTime);
                appointment.setAppointmentEnd(endTime);

                if (priorityToggleGroup.getSelectedToggle().equals(lowPriRadioButton)) {
                    appointment.setPriority(1);
                }
                if (priorityToggleGroup.getSelectedToggle().equals(mediumPriRadioButton)) {
                    appointment.setPriority(2);
                }
                if (priorityToggleGroup.getSelectedToggle().equals(highPriRadioButton)) {
                    appointment.setPriority(3);
                }


                //Legger til møtet i databasen!


                if (isEditable){
                    RegisterSingleton.sharedInstance().getRegister().editAppointment(appointment,appointment.getRoom());
                }else{
                    appointment = RegisterSingleton.sharedInstance().getRegister().addAppointment(appointment.getAppointmentName(), startTimeString, endTimeString, appointment.getDescription(), appointment.getPriority(), RegisterSingleton.sharedInstance().getRegister().getUsername(), appointment.getRoom(), appointment.getAlternativeLocation());
                    //Finner aller personer
                    ArrayList<Person> allPersons = RegisterSingleton.sharedInstance().getRegister().getPersons();

                    for (Person p : allPersons){
                        RegisterSingleton.sharedInstance().getRegister().invitePerson(p, appointment);
                        System.out.println(p.getName() + "\t was invited to meeting: " + appointment.getAppointmentName());
                    }

                    String meetingName = nameTextField.getText();
                }
                parent.updateCalendarView();
                GuiUtils.closeWindow(actionEvent);
            }
        }
    }

    private boolean isValidDate(String s){
        if (s.length() > 10 || s.length() < 10){
            return false;
        }
        if ( Character.isDigit(s.charAt(0)) && Character.isDigit(s.charAt(1))
                && s.charAt(4) == '-' && s.charAt(7) == '-'
                && Character.isDigit(s.charAt(2)) && Character.isDigit(s.charAt(3))
                && Character.isDigit(s.charAt(5)) && Character.isDigit(s.charAt(6))
                && Character.isDigit(s.charAt(8)) && Character.isDigit(s.charAt(9)) ){
            return true;
        }
        return false;
    }

    private boolean isValidTime(String s){
        if (s.length() > 5 || s.length() < 5){
            return false;
        }
        if (Character.isDigit(s.charAt(0)) && Character.isDigit(s.charAt(1)) && s.charAt(2) == ':' && Character.isDigit(s.charAt(3)) && Character.isDigit(s.charAt(4))){
            return true;
        }
        return false;
    }



    public void updateView() {
        if (appointment.getAlternativeLocation() == null){
            meetingRoomButton.setText(appointment.getRoom().getRoomName());
        }else{
            meetingRoomButton.setText(appointment.getAlternativeLocation());
        }
    }
    public int getNumberOfInvited() {
        return numberOfInvited;
    }

    public void setNumberOfInvited(int numberOfInvited) {
        this.numberOfInvited = numberOfInvited;
    }

    public void infoButtonOnAction(ActionEvent actionEvent) {
        System.out.println();
        System.out.println("Følgende informasjon ligger i nåværende appointment:");
        System.out.println();
        System.out.println("Name:\t\t\t" + appointment.getAppointmentName());
        //System.out.println("Start tid:\t\t" + appointment.getAppointmentStart());
        //System.out.println("Slutt tid:\t\t" + appointment.getAppointmentEnd());
        System.out.println("Beskrivelse:\t" + appointment.getDescription());
        System.out.println("Prioritet:\t\t" + appointment.getPriority());
        System.out.println("Valgt rom:\t\t" + appointment.getRoom());
        System.out.println("Inviterte:\t\t" + RegisterSingleton.sharedInstance().getRegister().getInvitees(appointment.getAppointmentID()));
    }



    public void setAppointment(Appointment appointment) {
        this.appointment = appointment;

        if (this.appointment != null){


            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            java.util.Date date = new java.util.Date();
            System.out.println("Current Date : " + dateFormat.format(date));


            DateFormat timeFormat = new SimpleDateFormat("HH:mm");

            Date startMeeting = GeneralUtil.stringToDate(appointment.getAppointmentStart());
            Date endMeeting = GeneralUtil.stringToDate(appointment.getAppointmentEnd());

            nameTextField.setText(appointment.getAppointmentName());
            startDateTextField.setText(dateFormat.format(startMeeting));
            endDateTextField.setText(dateFormat.format(endMeeting));
            descriptionTextArea.setText(appointment.getDescription());
            startTimeTextField.setText(timeFormat.format(startMeeting));
            endTimeTextField.setText(timeFormat.format(endMeeting));


            if (appointment.getPriority() == 0){
                lowPriRadioButton.setSelected(true);
            }else if (appointment.getPriority() == 1){
                mediumPriRadioButton.setSelected(true);

            }else{
                highPriRadioButton.setSelected(true);
            }

            if(appointment.getRoom().getRoomID() == 1){
                System.out.println(appointment.getAlternativeLocation());
                meetingRoomButton.setText(appointment.getAlternativeLocation());
            }else{
                System.out.println("eh");

                meetingRoomButton.setText(appointment.getRoom().getRoomName());
            }
        }


    }
}
