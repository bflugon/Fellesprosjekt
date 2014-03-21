package main.calendar;

//import com.javafx.tools.doclets.formats.html.SourceToHTMLConverter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Callback;
import main.Register;
import main.RegisterSingleton;
import main.importCalendars.ImportCalendarsController;
import main.meeting.MeetingController;
import main.meetingRequest.MeetingRequestViewController;
import main.settings.SettingsViewController;
import model.Appointment;
import model.Person;
import util.GeneralUtil;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;

public class CalendarController implements Initializable{

    public ListView<Appointment> listViewMonday;
    public ListView<Appointment> listViewTuesday;
    public ListView<Appointment> listViewWednesday;
    public ListView<Appointment> listViewThursday;
    public ListView<Appointment> listViewFriday;
    public ListView<Appointment> listViewSaturday;
    public ListView<Appointment> listViewSunday;
    public ComboBox<Appointment> alarmComboBox;

    ObservableList<Appointment> appointmentsMonday;
    ObservableList<Appointment> appointmentsTuesday;
    ObservableList<Appointment> appointmentsWednesday;
    ObservableList<Appointment> appointmentsThursday;
    ObservableList<Appointment> appointmentsFriday;
    ObservableList<Appointment> appointmentsSaturday;
    ObservableList<Appointment> appointmentsSunday;


    ObservableList<Person> otherUsersAppointmentsToShow;
    ObservableList<Person> allCalendarUsers;

    ObservableList<ObservableList<Appointment>> weekAppointments;


    private Calendar weekMondayDate;
    private Calendar weekSundayDate;
    private Calendar dateToday;
    private Calendar dateOfMondaySelectedWeek;
    public Label nameLabel;
    public Label weekNumberLabel;
    private int  dayNumberOfWeek;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        nameLabel.setText(RegisterSingleton.sharedInstance().getRegister().getPersonByUsername(RegisterSingleton.sharedInstance().getRegister().getUsername()).getName());

        appointmentsMonday = FXCollections.observableArrayList();
        appointmentsTuesday = FXCollections.observableArrayList();
        appointmentsWednesday = FXCollections.observableArrayList();
        appointmentsThursday = FXCollections.observableArrayList();
        appointmentsFriday = FXCollections.observableArrayList();
        appointmentsSaturday = FXCollections.observableArrayList();
        appointmentsSunday = FXCollections.observableArrayList();

        weekAppointments = FXCollections.observableArrayList();
        weekAppointments.addAll(appointmentsMonday,appointmentsTuesday,appointmentsWednesday,appointmentsThursday,appointmentsFriday,appointmentsSaturday,appointmentsSunday);

        //listViewMonday.setCellFactory(ComboBoxListCell.forListView(test1));
        //Legger til comment
        //Legger til enda et comment
        listViewMonday.setCellFactory(new Callback<ListView<Appointment>,
                ListCell<Appointment>>() {
            @Override
            public ListCell<Appointment> call(ListView<Appointment> list) {
                return new CalenderCell();
            }
        }
        );
        listViewTuesday.setCellFactory(new Callback<ListView<Appointment>,
                ListCell<Appointment>>() {
            @Override
            public ListCell<Appointment> call(ListView<Appointment> list) {
                return new CalenderCell();
            }
        }
        );
        listViewWednesday.setCellFactory(new Callback<ListView<Appointment>,
                ListCell<Appointment>>() {
            @Override
            public ListCell<Appointment> call(ListView<Appointment> list) {
                return new CalenderCell();
            }
        }
        );
        listViewThursday.setCellFactory(new Callback<ListView<Appointment>,
                ListCell<Appointment>>() {
            @Override
            public ListCell<Appointment> call(ListView<Appointment> list) {
                return new CalenderCell();
            }
        }
        );
        listViewFriday.setCellFactory(new Callback<ListView<Appointment>,
                ListCell<Appointment>>() {
            @Override
            public ListCell<Appointment> call(ListView<Appointment> list) {
                return new CalenderCell();
            }
        }
        );
        listViewSaturday.setCellFactory(new Callback<ListView<Appointment>,
                ListCell<Appointment>>() {
            @Override
            public ListCell<Appointment> call(ListView<Appointment> list) {
                return new CalenderCell();
            }
        }
        );
        listViewSunday.setCellFactory(new Callback<ListView<Appointment>,
                ListCell<Appointment>>() {
            @Override
            public ListCell<Appointment> call(ListView<Appointment> list) {
                return new CalenderCell();
            }
        }
        );


        alarmComboBox.setCellFactory(
                new Callback<ListView<Appointment>, ListCell<Appointment>>() {
                    @Override public ListCell<Appointment> call(ListView<Appointment> param) {
                        final ListCell<Appointment> cell = new ListCell<Appointment>() {
                            {
                                super.setPrefWidth(100);
                            }
                            @Override public void updateItem(Appointment item,
                                                             boolean empty) {
                                super.updateItem(item, empty);
                                if (item != null) {

                                    Calendar appointmentStart = GeneralUtil.dateToCalendar(GeneralUtil.stringToDate(item.getAppointmentStart()));
                                    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm dd-MM");
                                    String startDateText = sdf.format(appointmentStart.getTime());

                                    setText(startDateText + " / " + item.getAppointmentName() );

                                }
                                else {
                                    setText(null);
                                }
                            }
                        };
                        return cell;
                    }
                });

        dateToday = Calendar.getInstance();
        dateOfMondaySelectedWeek = findCurrentWeekMondayDate();
        dayNumberOfWeek = getDayNumberOfWeek();
        weekMondayDate = findWeekMondayDate();
        weekSundayDate = findWeekSundayDate();
        alarmComboBox.setItems(RegisterSingleton.sharedInstance().getRegister().getAlertAppointments());
        updateCalendarView();

    }


    public ObservableList<Person> getOtherUsersAppointmentsToShow() {
        if (otherUsersAppointmentsToShow == null){
            return otherUsersAppointmentsToShow = FXCollections.observableArrayList();
        }
        return otherUsersAppointmentsToShow;
    }

    public ObservableList<Person> getAllCalendarUsers() {
        return allCalendarUsers = FXCollections.observableArrayList(RegisterSingleton.sharedInstance().getRegister().getPersons());
    }

    public void updateCalendarView(){

        weekNumberLabel.setText(Integer.toString(findWeekNumber()));
        updateDayListViews();
        updateAlarms();


    }


    public void updateAlarms(){
        if (RegisterSingleton.sharedInstance().getRegister().getHasAlarm() != null &&
            RegisterSingleton.sharedInstance().getRegister().getHasAlarm() == true){

            for (Appointment appointment : RegisterSingleton.sharedInstance().getRegister().getAppointments()){

                if (appointmentShouldBeAddedToAlertList(appointment)){
                    System.out.println("Adds appointment to alerts");
                    if (!RegisterSingleton.sharedInstance().getRegister().getAlertAppointments().contains(appointment)){
                        RegisterSingleton.sharedInstance().getRegister().getAlertAppointments().add(appointment);
                    }
                }

                if (appointmentShouldBeRemovedFromAlertList(appointment)){
                    System.out.println("Remove appointment from  alerts");
                    if (RegisterSingleton.sharedInstance().getRegister().getAlertAppointments().contains(appointment)){
                        RegisterSingleton.sharedInstance().getRegister().getAlertAppointments().remove(appointment);
                    }
                }
            }

            System.out.println("Alert appointments:");
            System.out.println(RegisterSingleton.sharedInstance().getRegister().getAlertAppointments().size());

        }
        alarmComboBox.setPromptText("Varsler (" + RegisterSingleton.sharedInstance().getRegister().getAlertAppointments().size() + ")");


    }

    public boolean appointmentShouldBeRemovedFromAlertList(Appointment appointment){
        Calendar dateNow = Calendar.getInstance();
        if (dateNow.getTime().compareTo(GeneralUtil.stringToDate(appointment.getAppointmentEnd())) == 1){
            return true;
        }
        return false;
    }


    public boolean appointmentShouldBeAddedToAlertList(Appointment appointment){
        Calendar hoursEarlierDate = Calendar.getInstance();
        hoursEarlierDate.setTime(GeneralUtil.stringToDate(appointment.getAppointmentStart()));
        int hoursBefore = RegisterSingleton.sharedInstance().getRegister().getHoursBeforeAlarm() * -1;
        hoursEarlierDate.add(Calendar.HOUR_OF_DAY, hoursBefore);


        Calendar dateNow = Calendar.getInstance();

        System.out.println("Date of hours earlier" + hoursEarlierDate.getTime());
        System.out.println("Date of dateNow " + dateNow.getTime());
        System.out.println("Date of appointment end " + GeneralUtil.stringToDate(appointment.getAppointmentEnd()));

        System.out.println("Name " + appointment.getAppointmentName());


        System.out.println(dateNow.getTime().compareTo(hoursEarlierDate.getTime()) );
        System.out.println( dateNow.getTime().compareTo(GeneralUtil.stringToDate(appointment.getAppointmentEnd())));


        if (dateNow.getTime().compareTo(hoursEarlierDate.getTime()) > -1 &&
            dateNow.getTime().compareTo(GeneralUtil.stringToDate(appointment.getAppointmentEnd())) < 1){
            return true;
        }
        return false;
    }



    public void updateDayListViews(){
        appointmentsMonday.clear();
        appointmentsTuesday.clear();
        appointmentsWednesday.clear();
        appointmentsThursday.clear();
        appointmentsFriday.clear();
        appointmentsSaturday.clear();
        appointmentsSunday.clear();

        for (Appointment appointment : RegisterSingleton.sharedInstance().getRegister().getAppointments()){
            addAppointmentsToView(appointment);
        }


        for (ObservableList<Appointment> dayAppointments : weekAppointments){
            Collections.sort(dayAppointments, new Comparator<Appointment>() {
                public int compare(Appointment a1, Appointment a2) {
                    return  GeneralUtil.stringToDate(a1.getAppointmentStart()).compareTo(GeneralUtil.stringToDate(a2.getAppointmentStart()));
                }
            });
        }



        listViewMonday.setItems(appointmentsMonday);
        listViewTuesday.setItems(appointmentsTuesday);
        listViewWednesday.setItems(appointmentsWednesday);
        listViewThursday.setItems(appointmentsThursday);
        listViewFriday.setItems(appointmentsFriday);
        listViewSaturday.setItems(appointmentsSaturday);
        listViewSunday.setItems(appointmentsSunday);
    }

    public void addAppointmentsToView(Appointment appointment){
        if (appointmentIsThisWeek(appointment)){
            switch (GeneralUtil.dateToCalendar(GeneralUtil.stringToDate(appointment.getAppointmentStart())).get(Calendar.DAY_OF_WEEK)) {
                case 1:  appointmentsSunday.add(appointment);
                    break;
                case 2:  appointmentsMonday.add(appointment);
                    break;
                case 3:  appointmentsTuesday.add(appointment);
                    break;
                case 4:  appointmentsWednesday.add(appointment);
                    break;
                case 5:  appointmentsThursday.add(appointment);
                    break;
                case 6:  appointmentsFriday.add(appointment);
                    break;
                case 7:  appointmentsSaturday.add(appointment);
                    break;
                default:
                    break;
            }


        }
    }

    public boolean appointmentIsThisWeek(Appointment appointment){
        Calendar appointmentDate = GeneralUtil.dateToCalendar(GeneralUtil.stringToDate(appointment.getAppointmentStart()));
/*
        System.out.println("Date of monday " + weekMondayDate.getTime());
        System.out.println("Date of ****** " + appointmentDate.getTime());
        System.out.println("Date of sunday " + weekSundayDate.getTime());

        System.out.println(appointmentDate.compareTo(findWeekMondayDate()));
        System.out.println(appointmentDate.compareTo(findWeekSundayDate()));
*/


        if (appointmentDate.compareTo(this.findWeekMondayDate()) > -1
                && appointmentDate.compareTo(this.findWeekSundayDate()) < 1 ){
            return true;
        }else{
            return false;
        }
    }

    public void meetingClicked(MouseEvent event) throws Exception{
        System.out.println(event.getSource());
        ListView<Appointment> listViewOfSelectedCell = (ListView<Appointment>)event.getSource();



        if (!listViewOfSelectedCell.getSelectionModel().getSelectedItems().isEmpty()){

            System.out.println(listViewOfSelectedCell.getSelectionModel().getSelectedItem());

            Appointment selectedAppointment = listViewOfSelectedCell.getSelectionModel().getSelectedItem();
            if (selectedAppointment.getOwnerName().equals(RegisterSingleton.sharedInstance().getRegister().getUsername())){
                System.out.println("User made meeting");

                Stage newStage = new Stage();
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../meeting/meeting.fxml"));
                Parent root = (Parent)fxmlLoader.load();
                MeetingController meetingController = fxmlLoader.<MeetingController>getController();
                meetingController.setAppointment(selectedAppointment);
                meetingController.setMinSizeTextField(RegisterSingleton.sharedInstance().getRegister().getInvitees(selectedAppointment.getAppointmentID()).size());
                meetingController.setEditable(true);
                meetingController.setParent(this);
                newStage.setScene(new Scene(root));
                newStage.show();

            }else{

                Stage newStage = new Stage();
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../meetingRequest/meetingRequestView.fxml"));
                Parent root = (Parent)fxmlLoader.load();
                MeetingRequestViewController meeetingRequestController = fxmlLoader.<MeetingRequestViewController>getController();
                meeetingRequestController.setAppointment(selectedAppointment);
                meeetingRequestController.setParentController(this);
                newStage.setScene(new Scene(root));
                newStage.show();

            }

        }


    }


    public int getDayNumberOfWeek(){
        return dateToday.get(Calendar.DAY_OF_WEEK);
    }

    //Calendar logic

    public int findWeekNumber(){

        return dateOfMondaySelectedWeek.get(Calendar.WEEK_OF_YEAR);

    }


    public Calendar findCurrentWeekMondayDate(){

        weekMondayDate = Calendar.getInstance();
        weekMondayDate.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        weekMondayDate.set(Calendar.HOUR_OF_DAY, 0);
        weekMondayDate.set(Calendar.MINUTE, 0);
        weekMondayDate.set(Calendar.SECOND, 0);
        weekMondayDate.set(Calendar.MILLISECOND, 0);

        // System.out.println("Date of monday " + weekMondayDate.getTime());
        return weekMondayDate;
    }

    public Calendar findWeekMondayDate(){

        weekMondayDate = Calendar.getInstance();
       // System.out.println("Date of monday ++ " + dateOfMondaySelectedWeek.getTime());
        weekMondayDate.setTime(dateOfMondaySelectedWeek.getTime());
        weekMondayDate.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        weekMondayDate.set(Calendar.HOUR_OF_DAY, 0);
        weekMondayDate.set(Calendar.MINUTE, 0);
        weekMondayDate.set(Calendar.SECOND, 0);
        weekMondayDate.set(Calendar.MILLISECOND, 0);

        // System.out.println("Date of monday " + weekMondayDate.getTime());
        return weekMondayDate;
    }
    public Calendar findWeekSundayDate(){
        weekSundayDate = Calendar.getInstance();
        weekSundayDate.setTime(dateOfMondaySelectedWeek.getTime());
        weekSundayDate.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        weekSundayDate.set(Calendar.HOUR_OF_DAY, 0);
        weekSundayDate.set(Calendar.MINUTE, 0);
        weekSundayDate.set(Calendar.SECOND, 0);
        weekSundayDate.set(Calendar.MILLISECOND, 0);
      //  System.out.println("Date of sunday " + weekSundayDate.getTime());
        return weekSundayDate;
    }

    public boolean isAppointmentThisWeek(Appointment appointment){
        return true;
    }

    //Button actions
    public void makeMeeting(ActionEvent actionEvent) throws Exception{

        Stage newStage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../meeting/meeting.fxml"));
        Parent root = (Parent)fxmlLoader.load();
        MeetingController meetingController = fxmlLoader.<MeetingController>getController();
        meetingController.setParent(this);
        newStage.setScene(new Scene(root));
        newStage.show();
    }

    public void importButtonOnAction(ActionEvent actionEvent) throws  Exception{
        Stage newStage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../importCalendars/importCalendars.fxml"));
        Parent root = (Parent)fxmlLoader.load();
        ImportCalendarsController importCalendarsController = fxmlLoader.<ImportCalendarsController>getController();
        importCalendarsController.setParentController(this);
        newStage.setScene(new Scene(root));
        newStage.show();
        newStage.setTitle("Importer");

    }

    public void settingsButtonPressed(ActionEvent actionEvent) throws  Exception{

        Stage newStage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../settings/settings.fxml"));
        Parent root = (Parent)fxmlLoader.load();
        SettingsViewController settingsViewController = fxmlLoader.<SettingsViewController>getController();
        settingsViewController.setParentController(this);
        newStage.setScene(new Scene(root));
        newStage.show();
        newStage.setTitle("Settings");

    }

    public void previousWeekButtonPressed(ActionEvent actionEvent) {
        dateOfMondaySelectedWeek.add(Calendar.WEEK_OF_YEAR, -1);
        System.out.println("Date of previous monday " + dateOfMondaySelectedWeek.getTime());
        updateCalendarView();
    }

    public void nextWeekButtonPressed(ActionEvent actionEvent) {
        dateOfMondaySelectedWeek.add(Calendar.WEEK_OF_YEAR, 1);
        updateCalendarView();

    }

    //Calendar cell class

    static class CalenderCell extends ListCell<Appointment> {
        VBox vbox = new VBox();
        Label label = new Label("(empty)");
        Pane pane = new Pane();
        Label clockLabel = new Label("(empty)");

        String lastItem;

        public CalenderCell() {
            super();
            vbox.getChildren().addAll(label, pane, clockLabel);
            HBox.setHgrow(pane, Priority.ALWAYS);
        }

        @Override
        protected void updateItem(Appointment appointment, boolean empty) {

            super.updateItem(appointment, empty);
            setText(null);
            if (empty) {
                setGraphic(null);
            } else {
                label.setText(appointment!=null ? appointment.getAppointmentName() : "<null>");
                Calendar appointmentStart = GeneralUtil.dateToCalendar(GeneralUtil.stringToDate(appointment.getAppointmentStart()));
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
                String startDateText = sdf.format(appointmentStart.getTime());
                clockLabel.setText(startDateText);
                clockLabel.setTextFill(Color.GRAY);


                /*ArrayList<Person> attendingPeople = RegisterSingleton.sharedInstance().getRegister().getAttendingPeople(appointment.getAppointmentID());
                System.out.println(attendingPeople);*/

                /*if(attendingPeople != null){
                    for (Person person : attendingPeople){
                        if (person != null){
                            System.out.println(person.getName() + RegisterSingleton.sharedInstance().getRegister().getUsername());
                            if (person.getName().equals(RegisterSingleton.sharedInstance().getRegister().getUsername())){
                                label.setTextFill(Color.BLACK);
                            }
                        }else{
                            label.setTextFill(Color.RED);

                        }


                    }
                }*/



                setGraphic(vbox);
            }
        }
    }

}