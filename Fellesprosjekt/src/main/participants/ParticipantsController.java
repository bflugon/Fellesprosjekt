package main.participants;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import main.Register;
import main.RegisterSingleton;
import model.Appointment;
import model.Group;
import model.MeetingRoom;
import model.Person;
import util.GuiUtils;
import javafx.event.ActionEvent;
import javafx.scene.Node;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

/**
 * Created by markuslund92 on 06.03.14.
 */
public class ParticipantsController implements Initializable{

    public TableColumn allPersonsTableColumn;
    public TableColumn allGroupsTableColumns;
    public TableColumn attendingPeopleTableColumn;
    public TableColumn notAttendingPeopleTableColumn;
    public TableColumn invitedPeopleTableColumn;

    public TableView allPersonsTableView;
    public TableView allGroupsTableView;
    public TableView attendingPeopleTableView;
    public TableView notAttendingPeopleTableView;
    public TableView invitedPeopleTableView;

    public TextField externalEmailTextField;
    private ArrayList<Person> allPersons;
    private ArrayList<Group> allGroups;
    private ArrayList<Person> invitedPeople;
    private ArrayList<Person> attendingPeople;

    private Person selectedPerson;
    private Group selectedGroup;
    private Person personToBeRemoved;
    private TableView listToBeRemovedFrom;


    private Appointment appointment;



    public void participantsOkOnAction(ActionEvent actionEvent) {
        GuiUtils.closeWindow(actionEvent);
    }

    public void confirmParticipantsButtonOnAction(ActionEvent actionEvent) {
    }

    public void addSelectedParticipantButtonOnAction(ActionEvent actionEvent) {
        ObservableList<Person> currentInvitedPeople = invitedPeopleTableView.getItems();
        ObservableList<Person> currentAllPeople = allPersonsTableView.getItems();
        currentInvitedPeople.add(selectedPerson);
        currentAllPeople.remove(selectedPerson);
        invitedPeopleTableView.setItems(currentInvitedPeople);
        allPersonsTableView.setItems(currentAllPeople);
    }

    public void removeSelectedParticipantButton(ActionEvent actionEvent) {
        ObservableList<Person> currentList = listToBeRemovedFrom.getItems();
        ObservableList<Person> currentAllPeople = allPersonsTableView.getItems();
        currentList.remove(personToBeRemoved);
        currentAllPeople.add(personToBeRemoved);
        listToBeRemovedFrom.setItems(currentList);
        allPersonsTableView.setItems(currentAllPeople);

    }

    public void clickSelection(Event e){

        if (e.getSource() == allPersonsTableView){

            if (!((TableView)(e.getSource())).getSelectionModel().getSelectedCells().isEmpty()){
                Person p = (Person)((TableView)(e.getSource())).getSelectionModel().getSelectedItem();
                System.out.println("Selected person: " + p);
                selectedPerson = p;
                selectedGroup = null;
                personToBeRemoved = null;
                listToBeRemovedFrom = null;
            }

        }else if (e.getSource() == allGroupsTableView){

            if (!((TableView)(e.getSource())).getSelectionModel().getSelectedCells().isEmpty()){
                Group g = (Group)((TableView)(e.getSource())).getSelectionModel().getSelectedItem();
                System.out.println("Selected group: " + g);
                selectedPerson = null;
                selectedGroup = g;
                personToBeRemoved = null;
                listToBeRemovedFrom = null;
            }

        }else if(e.getSource() == invitedPeopleTableView){

            if (!((TableView)(e.getSource())).getSelectionModel().getSelectedCells().isEmpty()){
                Person p = (Person)((TableView)(e.getSource())).getSelectionModel().getSelectedItem();
                selectedGroup = null;
                selectedPerson = null;
                personToBeRemoved = p;
                listToBeRemovedFrom = ((TableView)(e.getSource()));
                System.out.println("Person to be removed: " + p);
            }

        }else if(e.getSource() == attendingPeopleTableView){

            if (!((TableView)(e.getSource())).getSelectionModel().getSelectedCells().isEmpty()){
                Person p = (Person)((TableView)(e.getSource())).getSelectionModel().getSelectedItem();
                selectedGroup = null;
                selectedPerson = null;
                personToBeRemoved = p;
                listToBeRemovedFrom = ((TableView)(e.getSource()));
                System.out.println("Person to be removed: " + p);
            }

        }else if(e.getSource() == notAttendingPeopleTableView){

            if (!((TableView)(e.getSource())).getSelectionModel().getSelectedCells().isEmpty()){
                Person p = (Person)((TableView)(e.getSource())).getSelectionModel().getSelectedItem();
                selectedGroup = null;
                selectedPerson = null;
                personToBeRemoved = p;
                listToBeRemovedFrom = ((TableView)(e.getSource()));
                System.out.println("Person to be removed: " + p);
            }
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        allPersons = RegisterSingleton.sharedInstance().getRegister().getPersons();
        allGroups = RegisterSingleton.sharedInstance().getRegister().getGroups();

        ObservableList<Person> personOL = FXCollections.observableArrayList(allPersons);
        allPersonsTableColumn.setCellValueFactory(new PropertyValueFactory<Person, String>("Name"));
        allPersonsTableView.setItems(personOL);

        ObservableList<Group> groupOL = FXCollections.observableArrayList(allGroups);
        allGroupsTableColumns.setCellValueFactory(new PropertyValueFactory<Group, String>("GroupName"));
        allGroupsTableView.setItems(groupOL);


        updateGroupTable();
    }

    public void updateTables() {
        if(!(appointment.getAppointmentName() == null)){
            updateAttendingTable();
            updateNotAttendingTable();
            updateInvitedTable();
            updatePersonTable();
            updateGroupTable();

        }else{
            System.out.println("No appointment is set");
        }
    }

    private void updateNotAttendingTable() {
        //Not yet implemented, needs a way to fetch not attending people.
    }

    private void updateAttendingTable() {
        attendingPeople = RegisterSingleton.sharedInstance().getRegister().getAttendingPeople(appointment.getAppointmentID());

        if (attendingPeople != null){
            System.out.println("alle deltagere:");
            for (Person p : attendingPeople){
                System.out.println(p);
            }

            ObservableList<Person> attendingPersonOL = FXCollections.observableArrayList(attendingPeople);
            attendingPeopleTableColumn.setCellValueFactory(new PropertyValueFactory<Person, String>("Name"));
            attendingPeopleTableView.setItems(attendingPersonOL);
        }else{
            System.out.println("No attending participants");
        }
    }

    private void updateInvitedTable() {
        ArrayList<Person> allInvitedPeople = RegisterSingleton.sharedInstance().getRegister().getInvitees(appointment.getAppointmentID());
        invitedPeople = new ArrayList<>();
        for (Person ap : attendingPeople){
            for (Person ip : allInvitedPeople){
                if(!ap.getUsername().equals(ip.getUsername())){
                    invitedPeople.add(ip);
                }
            }
        }


        if(invitedPeople != null){
            System.out.println("alle inviterte:");
            for (Person p : invitedPeople){
                System.out.println(p);
            }

            ObservableList<Person> personOL = FXCollections.observableArrayList(invitedPeople);
            invitedPeopleTableColumn.setCellValueFactory(new PropertyValueFactory<Person, String>("Name"));
            invitedPeopleTableView.setItems(personOL);
        }else{
            System.out.println("No invited people");
        }
    }

    private void updateGroupTable() {
        ObservableList<Group> groupOL = FXCollections.observableArrayList(allGroups);
        allGroupsTableColumns.setCellValueFactory(new PropertyValueFactory<Group, String>("GroupName"));
        allGroupsTableView.setItems(groupOL);
    }

    private void updatePersonTable() {

        ArrayList<Person> notInvited = new ArrayList<Person>();
        for (Person p : allPersons){
            if(!invitedPeople.contains(p)){
                notInvited.add(p);
            }
        }

        ObservableList<Person> personOL = FXCollections.observableArrayList(notInvited);
        allPersonsTableColumn.setCellValueFactory(new PropertyValueFactory<Person, String>("Name"));
        allPersonsTableView.setItems(personOL);
    }

    public Appointment getAppointment() {
        return appointment;
    }

    public void setAppointment(Appointment appointment) {
        this.appointment = appointment;
        System.out.println("Appointment is set:");
        System.out.println(appointment);
    }

    public void addExternalEmailButtonOnAction(ActionEvent actionEvent) {

    }
}
