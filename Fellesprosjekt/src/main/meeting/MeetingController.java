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
import main.participants.ParticipantsController;
import main.roomFinder.RoomFinderController;
import model.Appointment;
import model.MeetingRoom;
import model.Person;
import util.GeneralUtil;
import util.GuiUtils;

import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;

public class MeetingController implements Initializable {


    Appointment appointment;

    //Deklarerer GUIelementer
    public Button meetingRoomButton;
    public Button chooseParticipantsButton;
    public TextField startTimeTextField;
    public TextField startDateTextField;
    public TextField endTimeTextField;
    public TextField endDateTextField;
    public TextField nameTextField;
    public TextField minSizeTextField;
    public RadioButton lowPriRadioButton;
    public RadioButton mediumPriRadioButton;
    public RadioButton highPriRadioButton;
    public TextArea descriptionTextArea;
    public ToggleGroup priorityToggleGroup;

    private boolean isEditable;
    private int minSize;
    private ArrayList<String> externalEmails;


    private ArrayList<Person> peopleToInvite;
    private CalendarController parent;
    private ArrayList<Person> removeList;
    private String redBorderStyling = "-fx-border-color: RED; -fx-border-width: 2px;";

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        appointment = new Appointment(0, null, null, null, null, 0, null, null,null, null);
    }

    public void setMinSizeTextField(int i){
        minSizeTextField.setText(Integer.toString(i));
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


        if (!isValidTime(startTimeTextField.getText())) {

            System.out.println("Invalid startTime format: " + startTimeTextField.getText());
            startTimeTextField.setStyle(redBorderStyling);

        }
        else if (!isValidDate(startDateTextField.getText())) {

            System.out.println("Invalid startDate format: " + startDateTextField.getText());
            startDateTextField.setStyle(redBorderStyling);

        }
        else if (!isValidTime(endTimeTextField.getText())) {

            System.out.println("Invalid endTime format: " + endTimeTextField.getText());
            endTimeTextField.setStyle(redBorderStyling);


        }
        else if (!isValidDate(endDateTextField.getText())) {

            System.out.println("Invalid endDate format: " + endDateTextField.getText());
            endDateTextField.setStyle(redBorderStyling);
        }else if (EndBeforeStartCheck()){
            System.out.println("EndBeforeStartCheck is true");
            endDateTextField.setStyle(redBorderStyling);
            endTimeTextField.setStyle(redBorderStyling);
        }else{

            String startTimeString = startDateTextField.getText() + " " + startTimeTextField.getText() + ":00";
            String endTimeString = endDateTextField.getText() + " " + endTimeTextField.getText() + ":00";

            Date startTime = GeneralUtil.stringToDate(startTimeString);
            Date endTime = GeneralUtil.stringToDate(endTimeString);

            appointment.setAppointmentStart(startTime);
            appointment.setAppointmentEnd(endTime);

            meetingRoomButton.setStyle("-fx-border-width: 0px;");
            Stage newStage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../roomFinder/roomFinder.fxml"));
            Parent root = (Parent)fxmlLoader.load();
            RoomFinderController roomFinderController = fxmlLoader.<RoomFinderController>getController();
            roomFinderController.setAppointment(appointment);
            roomFinderController.setAvailibleRooms(getAvailableRooms(appointment));
            roomFinderController.setParentController(this);

            try{
                minSize = Integer.parseInt(minSizeTextField.getText());

            }catch (Exception e){
//            e.printStackTrace();
                System.out.println("not an int");
                minSize = 0;
            }
            roomFinderController.setMinCapacity(minSize);

            //Sender med riktig rom hvis det eksisterer
            if (appointment.getRoom() != null){
                if (appointment.getRoom().getRoomID() == 1){
                    roomFinderController.setRoom(appointment.getAlternativeLocation());
                }else{
                    roomFinderController.setRoom(appointment.getRoom().getRoomName());
                }
            }

            //Sender med antall personer for å finne størrelse på rom

            newStage.setTitle("Velg rom");
            newStage.setScene(new Scene(root));
            newStage.show();

        }


    }


    public void setParent(CalendarController parent) {
        this.parent = parent;
    }


    public void chooseParticipants(ActionEvent actionEvent) throws Exception{

        chooseParticipantsButton.setStyle("-fx-border-width: 0px;");
        Stage newStage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../participants/participants.fxml"));
        Parent root = (Parent)fxmlLoader.load();
        ParticipantsController participantsController = fxmlLoader.<ParticipantsController>getController();
        participantsController.setParent(this);
        participantsController.setAppointment(appointment);
        participantsController.updateTables();
        participantsController.setExternalEmails(externalEmails);

        newStage.setTitle("Velg deltakere");
        newStage.setScene(new Scene(root));
        newStage.show();
    }

    public void setEditable(boolean isEditable) {
        this.isEditable = isEditable;
    }


    public void exitOnSave(ActionEvent actionEvent) {



        if (nameTextField.getText().equals("")) {
            nameTextField.setPromptText("FYLL INN NAVN!");
            //nameTextField.setEffect(redDropShadow);
            nameTextField.setStyle(redBorderStyling);
        }
        else if (!isValidTime(startTimeTextField.getText())) {

            System.out.println("Invalid startTime format: " + startTimeTextField.getText());
            startTimeTextField.setStyle(redBorderStyling);

        }
        else if (!isValidDate(startDateTextField.getText())) {

            System.out.println("Invalid startDate format: " + startDateTextField.getText());
            startDateTextField.setStyle(redBorderStyling);

        }
        else if (!isValidTime(endTimeTextField.getText())) {

            System.out.println("Invalid endTime format: " + endTimeTextField.getText());
            endTimeTextField.setStyle(redBorderStyling);


        }
        else if (!isValidDate(endDateTextField.getText())) {

            System.out.println("Invalid endDate format: " + endDateTextField.getText());
            endDateTextField.setStyle(redBorderStyling);
        }else if (EndBeforeStartCheck()){
            System.out.println("EndBeforeStartCheck is true");
            endDateTextField.setStyle(redBorderStyling);
            endTimeTextField.setStyle(redBorderStyling);
        }
        else {
            if (appointment.getRoom() == null) {
                System.out.println("No room selected");
                meetingRoomButton.setStyle(redBorderStyling);

            }
            else {

                appointment.setAppointmentName(nameTextField.getText());
                appointment.setDescription(descriptionTextArea.getText());

                String startTimeString = startDateTextField.getText() + " " + startTimeTextField.getText() + ":00";
                String endTimeString = endDateTextField.getText() + " " + endTimeTextField.getText() + ":00";

                Date startTime = GeneralUtil.stringToDate(startTimeString);
                Date endTime = GeneralUtil.stringToDate(endTimeString);

                appointment.setAppointmentStart(startTime);
                appointment.setAppointmentEnd(endTime);

                ArrayList<Appointment> avtaler = RegisterSingleton.sharedInstance().getRegister().getRoomAppointments(appointment.getRoom().getRoomID());
                if (avtaler == null || appointment.getRoom().getRoomID() == 1){
                    avtaler = new ArrayList<>();
                }

                //isRoomAvailable(room, appointment)

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


                if (isEditable) {
                    RegisterSingleton.sharedInstance().getRegister().editAppointment(appointment, appointment.getRoom());
                }
                else {
                    appointment = RegisterSingleton.sharedInstance().getRegister().addAppointment(appointment.getAppointmentName(), startTimeString, endTimeString, appointment.getDescription(), appointment.getPriority(), RegisterSingleton.sharedInstance().getRegister().getUsername(), appointment.getRoom(), appointment.getAlternativeLocation());

                    //Finner aller personer
                    //                    ArrayList<Person> allPersons = RegisterSingleton.sharedInstance().getRegister().getPersons();
                    //                    for (Person p : allPersons){
                    //                        RegisterSingleton.sharedInstance().getRegister().invitePerson(p, appointment);
                    //                    }

                    String meetingName = nameTextField.getText();
                }

                String meetingName = nameTextField.getText();


                //Inviterer alle personer
                ArrayList<Person> alreadyInvited = RegisterSingleton.sharedInstance().getRegister().getInvitees(appointment.getAppointmentID());
                if (alreadyInvited != null){
                    ArrayList<Person> finalInviteList = new ArrayList<>();
                    if (peopleToInvite == null){
                        peopleToInvite = new ArrayList<>();
                    }
                    for (Person pti : peopleToInvite){
                        finalInviteList.add(pti);
                        for (Person ai : alreadyInvited){
                            if(ai.getUsername().equals(pti.getUsername())){
                                finalInviteList.remove(pti);
                            }
                        }
                        for (Person p : finalInviteList) {
                            RegisterSingleton.sharedInstance().getRegister().invitePerson(p, appointment);
                            System.out.println(p.getName() + "\t was invited to meeting: " + appointment.getAppointmentName());
                        }


                    }
                }
                    else {
                        for (Person p : peopleToInvite) {
                            RegisterSingleton.sharedInstance().getRegister().invitePerson(p, appointment);
                            System.out.println(p.getName() + "\t was invited to meeting: " + appointment.getAppointmentName());
                        }
                    }

                if (externalEmails != null){
                    for (String email : externalEmails){

                        System.out.println("Inviterte epost: " + email);
                        System.out.println("Appointment: " + this.appointment);

                        RegisterSingleton.sharedInstance().getRegister().sendEmail(email, this.appointment);

                    }
                }


//                Slett fjernede personer, ikke implementert i register.
//                if(removeList != null){
//                    for (Person p : removeList){
//                        RegisterSingleton.sharedInstance().getRegister().
//                    }
//
//                }




                    parent.updateCalendarView();
                    GuiUtils.closeWindow(actionEvent);
            }
        }
    }

    private boolean EndBeforeStartCheck() {
        String[] endDate = endDateTextField.getText().split("-");
        String[] endTime = endTimeTextField.getText().split(":");
        String[] startDate = startDateTextField.getText().split("-");
        String[] startTime = startTimeTextField.getText().split(":");

        if (Integer.parseInt(endDate[0]) < Integer.parseInt(startDate[0])){
            System.out.println("Feil med år");
            return true;
        }else if(Integer.parseInt(endDate[1]) < Integer.parseInt(startDate[1])){
            System.out.println("Feil med måned");
            return true;
        }else if(Integer.parseInt(endDate[2]) < Integer.parseInt(startDate[2])){
            System.out.println("Feil med dag");
            return true;
        }else if(Integer.parseInt(endTime[0]) < Integer.parseInt(startTime[0])){
            System.out.println("Feil med time");
            return true;
        }else if(Integer.parseInt(endTime[1]) < Integer.parseInt(startTime[1])){
            System.out.println("Feil med minutter");
            return true;
        }
        return false;

    }

    private boolean isValidDate(String s){
        if (s.length() == 10 && Character.isDigit(s.charAt(0)) && Character.isDigit(s.charAt(1))
                && s.charAt(4) == '-' && s.charAt(7) == '-'
                && Character.isDigit(s.charAt(2)) && Character.isDigit(s.charAt(3))
                && Character.isDigit(s.charAt(5)) && Character.isDigit(s.charAt(6))
                && Character.isDigit(s.charAt(8)) && Character.isDigit(s.charAt(9))){
            if (Character.isDigit(s.charAt(0)) && Character.isDigit(s.charAt(1)) && Character.isDigit(s.charAt(2)) && Character.isDigit(s.charAt(3))) {
                String dateString = String.valueOf(s.charAt(5)) + String.valueOf(s.charAt(6));
                int date = Integer.parseInt(dateString);
                if (date < 13) {
                    if (date == 1 || date == 3 || date == 5 || date == 6 | date == 8 || date == 10 || date == 12) {
                        dateString = String.valueOf(s.charAt(8)) + String.valueOf(s.charAt(9));
                        date = Integer.parseInt(dateString);
                        if (date > 0 && date < 32) {
                            return true;
                        }
                    } else if (date == 2) {
                        dateString = String.valueOf(s.charAt(8)) + String.valueOf(s.charAt(9));
                        date = Integer.parseInt(dateString);
                        if (date > 0) {
                            if (date < 29 && date > 0) {
                                return true;
                            } else if (date < 30 && date > 0) {
                                dateString = String.valueOf(s.charAt(0)) + String.valueOf(s.charAt(1)) + String.valueOf(s.charAt(2)) + String.valueOf(s.charAt(3));
                                date = Integer.parseInt(dateString);
                                if (date % 4 == 0) {
                                    return true;
                                }
                            }
                        }
                    }
                }
                else {
                    dateString = String.valueOf(s.charAt(8)) + String.valueOf(s.charAt(9));
                    date = Integer.parseInt(dateString);
                    if (date < 31 && date > 0) {
                        return true;
                    }
                }
            }

        }
        return false;
    }

    private boolean isValidTime(String s){
        if (s.length() == 5 && Character.isDigit(s.charAt(0)) && Character.isDigit(s.charAt(1)) && s.charAt(2) == ':' && Character.isDigit(s.charAt(3)) && Character.isDigit(s.charAt(4))){
            String time = String.valueOf(s.charAt(0) + String.valueOf(s.charAt(1)));
            int time2 = Integer.parseInt(time);
            if (time2 < 25 && time2 > -1 ) {
                time = String.valueOf(s.charAt(3) + String.valueOf(s.charAt(4)));
                time2 = Integer.parseInt(time);
                if (time2 < 60 && time2 > -1) {
                    return true;
                }
            }
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

    public void deleteButtonOnAction(ActionEvent actionEvent) {
        GuiUtils.closeWindow(actionEvent);
        RegisterSingleton.sharedInstance().getRegister().deleteAppointment(appointment);
        System.out.println("Appointment deleted");
        parent.updateCalendarView();
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

    private ArrayList<MeetingRoom> getAvailableRooms(Appointment appointment) {
        ArrayList<MeetingRoom> available = new ArrayList<MeetingRoom>();
        ArrayList<MeetingRoom> rooms = RegisterSingleton.sharedInstance().getRegister().getRooms();
        if (appointment == null){
            return rooms;
        }else{
            for (MeetingRoom room : rooms) {
                if (isRoomAvailable(room,appointment)){
                    available.add(room);
                }
            }
            return available;

        }

    }



    private boolean isRoomAvailable(MeetingRoom room, Appointment appointment) {
        ArrayList<Appointment> avtaler = RegisterSingleton.sharedInstance().getRegister().getRoomAppointments(room.getRoomID());
        if (avtaler == null){
            avtaler = new ArrayList<>();
        }
        for (Appointment avtale : avtaler) {
            int sjekk1 = GeneralUtil.stringToDate(avtale.getAppointmentStart()).compareTo(GeneralUtil.stringToDate(appointment.getAppointmentStart()));
            int sjekk2 = GeneralUtil.stringToDate(avtale.getAppointmentStart()).compareTo(GeneralUtil.stringToDate(appointment.getAppointmentEnd()));
            int sjekk3 = GeneralUtil.stringToDate(avtale.getAppointmentEnd()).compareTo(GeneralUtil.stringToDate(appointment.getAppointmentStart()));
            int sjekk4 = GeneralUtil.stringToDate(avtale.getAppointmentEnd()).compareTo(GeneralUtil.stringToDate(appointment.getAppointmentEnd()));
            //if sjekk1 < 0 = avtalen starter etter starten på andre
            //if sjekk2 < 0 = avtalen slutter etter starten på andre
            //if sjekk3 < 0 = avtalen starter etter slutten på andre
            //if sjekk4 < 0 = avtalen slutter etter slutten på andre
            //Testcommit
            if ( (sjekk1 <= 0 && sjekk3 >= 0) || (sjekk2 <= 0 && sjekk4 >= 0)) {
                return false;
            }
        }
        return true;
    }

    public void setPeopleToInvite(ArrayList<Person> peopleToInvite) {
        this.peopleToInvite = peopleToInvite;
    }

    public void setExternalEmails(ArrayList<String> ex) {
        this.externalEmails = ex;
    }

    public void setRemoveList(ArrayList<Person> removeList) {
        this.removeList = removeList;
    }
}
