package main.participants;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
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


    private Appointment appointment;



    public void participantsOkOnAction(ActionEvent actionEvent) {
        GuiUtils.closeWindow(actionEvent);
    }

    public void confirmParticipantsButtonOnAction(ActionEvent actionEvent) {
    }

    public void addSelectedParticipantButtonOnAction(ActionEvent actionEvent) {
    }

    public void removeSelectedParticipantButton(ActionEvent actionEvent) {

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        allPersons = RegisterSingleton.sharedInstance().getRegister().getPersons();
        allGroups = RegisterSingleton.sharedInstance().getRegister().getGroups();

        System.out.println("Alle personer:");
        for (Person g : allPersons){
            System.out.println(g.getName());
        }

        System.out.println("Alle grupper:");
        for (Group g : allGroups){
            System.out.println(g.getGroupName());
        }

        updatePersonTable();


        updateGroupTable();


    }

    private void updateGroupTable() {
        ObservableList<Group> groupOL = FXCollections.observableArrayList(allGroups);
        allGroupsTableColumns.setCellValueFactory(new PropertyValueFactory<Group, String>("GroupName"));
        allGroupsTableView.setItems(groupOL);
    }

    private void updatePersonTable() {
        ObservableList<Person> personOL = FXCollections.observableArrayList(allPersons);
        allPersonsTableColumn.setCellValueFactory(new PropertyValueFactory<Person, String>("Name"));
        allPersonsTableView.setItems(personOL);
    }

    public Appointment getAppointment() {
        return appointment;
    }

    public void setAppointment(Appointment appointment) {
        this.appointment = appointment;
    }

    public void addExternalEmailButtonOnAction(ActionEvent actionEvent) {

    }
}
