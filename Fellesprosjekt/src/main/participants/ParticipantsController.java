package main.participants;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import main.RegisterSingleton;
import main.meeting.MeetingController;
import model.Appointment;
import model.Group;
import model.Person;
import util.GuiUtils;

import java.lang.reflect.Array;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

/**
 * Created by Markus Lund on 06.03.14.
 */
public class ParticipantsController implements Initializable{

    public TableColumn<Person, String> allPersonsTableColumn;
    public TableColumn<Group, String> allGroupsTableColumns;
    //public TableColumn attendingPeopleTableColumn;
    public TableColumn notAttendingPeopleTableColumn;
    public TableColumn<Person, String> invitedPeopleTableColumn;

    public TableView<Person> allPersonsTableView;
    public TableView<Group> allGroupsTableView;
    public TableView attendingPeopleTableView;
    public TableView notAttendingPeopleTableView;
    public TableView<Person> invitedPeopleTableView;

    public ListView attendingPeopleListView;
    public ListView notAttendingPeopleListView;

    public TextField externalEmailTextField;
    private ArrayList<Person> allPersons;
    private ArrayList<Group> allGroups;
    private ArrayList<Person> invitedPeople;
    private ArrayList<Person> attendingPeople;
    private ArrayList<Person> notAttendingPeople;

    private Person selectedPerson;
    private Group selectedGroup;
    private Person personToBeRemoved;
    private TableView<Person> tableViewToBeRemovedFrom;
    private ListView<Person> listViewToBeRemovedFrom;
    private ArrayList<String> externalEmails;

    private Appointment appointment;
    private MeetingController parent;
    private ArrayList<Person> removeList;



    public void setParent(MeetingController parent) {
        this.parent = parent;
    }




    public void confirmParticipantsButtonOnAction(ActionEvent actionEvent) {
        parent.setPeopleToInvite(new ArrayList<>((invitedPeopleTableView.getItems())));
        parent.setExternalEmails(externalEmails);
        parent.setRemoveList(removeList);
        GuiUtils.closeWindow(actionEvent);
    }

    public void setRemoveList(ArrayList<Person> list){
        this.removeList =list;
    }

    public void addSelectedParticipantButtonOnAction(ActionEvent actionEvent) {
        if (selectedPerson!=null){
            ObservableList<Person> currentInvitedPeople = invitedPeopleTableView.getItems();
            ObservableList<Person> currentAllPeople = allPersonsTableView.getItems();

            ArrayList<Person> tempRemoveList = new ArrayList<>();
            for (Person p : removeList){
                if (selectedPerson.getUsername().equals(p.getUsername())){
                    tempRemoveList.add(p);
                }
            }

            currentInvitedPeople.addAll(tempRemoveList);
            currentInvitedPeople.add(selectedPerson);
            currentAllPeople.remove(selectedPerson);
            invitedPeopleTableView.setItems(currentInvitedPeople);
            allPersonsTableView.setItems(currentAllPeople);
            selectedPerson = null;
            selectedGroup = null;
            personToBeRemoved = null;


        }else if(selectedGroup != null){
            ObservableList<Person> currentInvitedPeople = invitedPeopleTableView.getItems();
            ObservableList<Group> currentAllGroups = allGroupsTableView.getItems();
            ArrayList<Person> peopleToAdd = new ArrayList<>();

            boolean funnet = false;

            try{
                for (Person potensialPerson : RegisterSingleton.sharedInstance().getRegister().getMembersOfGroup(selectedGroup.getGroupID())){
                    funnet = false;
                    for (Person gammelperson : currentInvitedPeople){
                        if (potensialPerson.getUsername().equals(gammelperson.getUsername())){
                            funnet = true;
                        }
                    }
                    if(!funnet){
                        peopleToAdd.add(potensialPerson);
                    }
                }
                currentAllGroups.remove(selectedGroup);

                currentInvitedPeople.addAll(peopleToAdd);

                invitedPeopleTableView.setItems(currentInvitedPeople);
                allGroupsTableView.setItems(currentAllGroups);
                selectedPerson = null;
                personToBeRemoved = null;
                selectedGroup = null;

            }catch(Exception e) {
                System.out.println("Tom gruppe");
            }


        }
    }

    public void removeSelectedParticipantButton(ActionEvent actionEvent) {
        if(listViewToBeRemovedFrom == null){
            if (personToBeRemoved != null){
                ObservableList<Person> currentList = tableViewToBeRemovedFrom.getItems();
                ObservableList<Person> currentAllPeople = allPersonsTableView.getItems();
                currentList.remove(personToBeRemoved);

                removeList.add(personToBeRemoved);

                currentAllPeople.add(personToBeRemoved);
                tableViewToBeRemovedFrom.setItems(currentList);
                allPersonsTableView.setItems(currentAllPeople);
                personToBeRemoved = null;
                selectedPerson = null;
                selectedGroup = null;
            }
        }else{
            if (personToBeRemoved != null){
                ObservableList<Person> currentList = listViewToBeRemovedFrom.getItems();
                ObservableList<Person> currentAllPeople = allPersonsTableView.getItems();
                currentList.remove(personToBeRemoved);

                removeList.add(personToBeRemoved);

                currentAllPeople.add(personToBeRemoved);
                listViewToBeRemovedFrom.setItems(currentList);
                allPersonsTableView.setItems(currentAllPeople);
                personToBeRemoved = null;
                selectedPerson = null;
                selectedGroup = null;
            }


        }


    }

    public void clickSelection(Event e){

        if (e.getSource() == allPersonsTableView){

            if (!((TableView)(e.getSource())).getSelectionModel().getSelectedCells().isEmpty()){
                Person p = (Person)((TableView)(e.getSource())).getSelectionModel().getSelectedItem();
                System.out.println("Selected person: " + p);
                selectedPerson = p;
                selectedGroup = null;
                personToBeRemoved = null;
                tableViewToBeRemovedFrom = null;
                listViewToBeRemovedFrom = null;
            }

        }else if (e.getSource() == allGroupsTableView){

            if (!((TableView)(e.getSource())).getSelectionModel().getSelectedCells().isEmpty()){
                Group g = (Group)((TableView)(e.getSource())).getSelectionModel().getSelectedItem();
                System.out.println("Selected group: " + RegisterSingleton.sharedInstance().getRegister().getMembersOfGroup(g.getGroupID()));
                selectedPerson = null;
                selectedGroup = g;
                personToBeRemoved = null;
                tableViewToBeRemovedFrom = null;
                listViewToBeRemovedFrom = null;
            }

        }else if(e.getSource() == invitedPeopleTableView){

            if (!((TableView)(e.getSource())).getSelectionModel().getSelectedCells().isEmpty()){
                Person p = (Person)((TableView)(e.getSource())).getSelectionModel().getSelectedItem();
                selectedGroup = null;
                selectedPerson = null;
                personToBeRemoved = p;
                tableViewToBeRemovedFrom = ((TableView<Person>)(e.getSource()));
                listViewToBeRemovedFrom = null;
//                System.out.println("Person to be removed: " + p);
            }

        }else if(e.getSource() == attendingPeopleTableView){

            if (!((TableView)(e.getSource())).getSelectionModel().getSelectedCells().isEmpty()){
                Person p = (Person)((TableView)(e.getSource())).getSelectionModel().getSelectedItem();
                selectedGroup = null;
                selectedPerson = null;
                personToBeRemoved = p;
                tableViewToBeRemovedFrom = ((TableView<Person>)(e.getSource()));
                listViewToBeRemovedFrom = null;
//                System.out.println("Person to be removed: " + p);
            }

        }else if(e.getSource() == notAttendingPeopleTableView){

            if (!((TableView)(e.getSource())).getSelectionModel().getSelectedCells().isEmpty()){
                Person p = (Person)((TableView)(e.getSource())).getSelectionModel().getSelectedItem();
                selectedGroup = null;
                selectedPerson = null;
                personToBeRemoved = p;
                tableViewToBeRemovedFrom = ((TableView<Person>)(e.getSource()));
                listViewToBeRemovedFrom = null;
//                System.out.println("Person to be removed: " + p);
            }
        }else if(e.getSource() == attendingPeopleListView){

            if (((ListView)(e.getSource())).getSelectionModel().getSelectedItem() != null){
                Person p = (Person)((ListView)(e.getSource())).getSelectionModel().getSelectedItem();
                selectedGroup = null;
                selectedPerson = null;
                personToBeRemoved = p;
                tableViewToBeRemovedFrom = null;
                listViewToBeRemovedFrom = ((ListView<Person>)(e.getSource()));

//                System.out.println("Person to be removed: " + p);
            }
        }else if (e.getSource() == notAttendingPeopleListView){
            if (((ListView)(e.getSource())).getSelectionModel().getSelectedItem() != null){
                Person p = (Person)((ListView)(e.getSource())).getSelectionModel().getSelectedItem();
                selectedGroup = null;
                selectedPerson = null;
                personToBeRemoved = p;
                tableViewToBeRemovedFrom = null;
                listViewToBeRemovedFrom = ((ListView<Person>)(e.getSource()));
        }
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //Legger til alle personer, treng for å lage nytt møte
        allPersons = RegisterSingleton.sharedInstance().getRegister().getPersons();
        ObservableList<Person> personOL = FXCollections.observableArrayList(allPersons);
        allPersonsTableColumn.setCellValueFactory(new PropertyValueFactory<Person, String>("Name"));
        allPersonsTableView.setItems(personOL);

        //Legger til alle grupper
        allGroups = RegisterSingleton.sharedInstance().getRegister().getGroups();
        ObservableList<Group> groupOL = FXCollections.observableArrayList(allGroups);
        allGroupsTableColumns.setCellValueFactory(new PropertyValueFactory<Group, String>("GroupName"));
        allGroupsTableView.setItems(groupOL);



        invitedPeopleTableColumn.setCellValueFactory(new PropertyValueFactory<Person, String>("Name"));
//        notAttendingPeopleTableColumn.setCellFactory(new PropertyValueFactory<Person, String>("Name"));

        attendingPeopleListView.setCellFactory(new Callback<ListView<Person>,
                        ListCell<Person>>() {
            @Override
            public ListCell<Person> call(ListView<Person> personListView) {
                return new PersonCell();
            }
        }
        );

        notAttendingPeopleListView.setCellFactory(new Callback<ListView<Person>,
                ListCell<Person>>() {
            @Override
            public ListCell<Person> call(ListView<Person> personListView) {
                return new PersonCell();
            }
        }
        );



    }

    public void updateTables() {

        if(appointment.getAppointmentName() != null){
            if(appointment.getAppointmentID() != 0){
                int i = appointment.getAppointmentID();
                System.out.println("Using old attendingPoeple List");
                System.out.println("Appointment ID: " + i);

                //Henter attending people
                attendingPeople = RegisterSingleton.sharedInstance().getRegister().getAttendingPeople(appointment.getAppointmentID());
                System.out.println("Attending people: " + attendingPeople);
                if(attendingPeople == null){
                    attendingPeople = new ArrayList<>();
                }

                //henter not attending people
                notAttendingPeople = RegisterSingleton.sharedInstance().getRegister().getNotAttendingPeople(appointment.getAppointmentID());
                System.out.println("Not attending people: " + notAttendingPeople);
                if(notAttendingPeople == null){
                    notAttendingPeople = new ArrayList<>();
                }
            }else{
                System.out.println("Createing new attendingPoeple List");
                attendingPeople = new ArrayList<>();
                notAttendingPeople = new ArrayList<>();

            }


            System.out.println("updating tables");
            updateAttendingTable();
            updateNotAttendingTable();
            updatePersonTable();
            updateInvitedTable();
            updateGroupTable();

        }else{
            System.out.println("No appointment is set");
        }
    }

    private void updateNotAttendingTable() {

        if (notAttendingPeople.size() > 0){
            System.out.println("There exists not attending dudes");
            ObservableList<Person> notAttendingPersonOL = FXCollections.observableArrayList(notAttendingPeople);

//            attendingPeopleTableView.setItems(attendingPersonOL);
            notAttendingPeopleListView.setItems(notAttendingPersonOL);

        }else{
            System.out.println("No not attending participants");
        }
    }

    private void updateAttendingTable() {

        if (attendingPeople.size() > 0){
            System.out.println("There exists attending dudes");
            ObservableList<Person> attendingPersonOL = FXCollections.observableArrayList(attendingPeople);

//            attendingPeopleTableView.setItems(attendingPersonOL);
            attendingPeopleListView.setItems(attendingPersonOL);

        }else{
            System.out.println("No attending participants");
        }
    }

    private void updateInvitedTable() {
        System.out.println("Updating invited table");
        ArrayList<Person> allInvitedPeople = RegisterSingleton.sharedInstance().getRegister().getInvitees(appointment.getAppointmentID());
        if (allInvitedPeople == null){
            allInvitedPeople = new ArrayList<>();
        }
        System.out.println("Alle inivterte: " + allInvitedPeople);
        invitedPeople = new ArrayList<>();
        if (attendingPeople.size() > 0){
            System.out.println("Finner inviterte som ikke deltar");
            for (Person ap : attendingPeople){
                for (Person ip : allInvitedPeople){
                    if(!ap.getUsername().equals(ip.getUsername())){
                        invitedPeople.add(ip);
                    }
                }
            }
        }else{
            for (Person ip : allInvitedPeople){
                invitedPeople.add(ip);
            }
        }

        if(invitedPeople != null){

            ObservableList<Person> personOL = FXCollections.observableArrayList(invitedPeople);
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
        ArrayList<Person> allInvitedPeople = RegisterSingleton.sharedInstance().getRegister().getInvitees(appointment.getAppointmentID());
        if (allInvitedPeople == null){
            allInvitedPeople = new ArrayList<>();
        }
        allPersons = RegisterSingleton.sharedInstance().getRegister().getPersons();
        ArrayList<Person> notInvited = new ArrayList<Person>();
        for (Person ap : allPersons){
            if(!notInvited.contains(ap) && !ap.getUsername().equals(RegisterSingleton.sharedInstance().getRegister().getUsername())){
                notInvited.add(ap);
            }
            for (Person ip : allInvitedPeople){
                if ((ap.getUsername().equals(ip.getUsername()))){
                    notInvited.remove(ap);
                }
            }
        }
//        System.out.println("Alle personer som ikke er inviterte!:");
//        for (Person p : notInvited){
//            System.out.println(p);
//        }

        ObservableList<Person> personOL = FXCollections.observableArrayList(notInvited);
        allPersonsTableColumn.setCellValueFactory(new PropertyValueFactory<Person, String>("Name"));
        allPersonsTableView.setItems(personOL);
    }

    public Appointment getAppointment() {
        return appointment;
    }

    public void setAppointment(Appointment appointment) {
        this.appointment = appointment;
//        System.out.println("Appointment is set:");
//        System.out.println(appointment);
    }

    public void addExternalEmailButtonOnAction(ActionEvent actionEvent) {
        if(!externalEmailTextField.getText().equals("")){

            if (externalEmails == null){
                externalEmails = new ArrayList<>();
                externalEmails.add(externalEmailTextField.getText());
            }else{
                externalEmails.add(externalEmailTextField.getText());
            }

            System.out.println("All emails:");
            for (String email : externalEmails){
                System.out.println(email);
            }

        }


    }

    public void setExternalEmails(ArrayList<String> ex) {
        this.externalEmails = ex;
    }

    static class PersonCell extends ListCell<Person> {
        VBox vbox = new VBox();
        Label label = new Label("(empty)");
        Pane pane = new Pane();



        public PersonCell() {
            super();
            vbox.getChildren().addAll(label, pane);
            HBox.setHgrow(pane, Priority.ALWAYS);
        }

        @Override
        protected void updateItem(Person person, boolean empty) {
            super.updateItem(person, empty);
            setText(null);
            if (empty) {
                setGraphic(null);
            } else {
                label.setText(person!=null ? person.getName() : "<null>");
                setGraphic(vbox);
            }
        }
    }
}
