package main.importCalendars;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import main.RegisterSingleton;
import model.Person;
import util.GuiUtils;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

/**
 * Created by markuslund92 on 06.03.14.
 */
public class ImportCalendarsController implements Initializable {

    public ListView<Person> listViewCalendarCandidates;
    public ListView<Person> listViewCalendarSelected;
    ObservableList<Person> candidatePersonsList;
    ObservableList<Person> selectedPersonsList;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        candidatePersonsList = getAllPersons(); //FXCollections.observableArrayList("Single", "Double", "Suite", "Family App", "Double", "Suite", "Family App");
        //getAllPersons();
        listViewCalendarCandidates.setItems(candidatePersonsList);
        selectedPersonsList = FXCollections.observableArrayList();
        listViewCalendarSelected.setItems(selectedPersonsList);
    }


    public void okButtonPressed(ActionEvent actionEvent) {
        ArrayList<Person> people = new ArrayList<>(selectedPersonsList);
        RegisterSingleton.sharedInstance().getRegister().updateCalendar(people);
        GuiUtils.closeWindow(actionEvent);
    }

    public void addCalendar(ActionEvent ae) {

        if (listViewCalendarCandidates.getSelectionModel().getSelectedItem() != null) {
            Person selected = listViewCalendarCandidates.getSelectionModel().getSelectedItem();
            if ( candidatePersonsList.contains(selected) && !selectedPersonsList.contains(selected) ) {
                selectedPersonsList.add(selected);
                candidatePersonsList.remove(selected);
                listViewCalendarCandidates.getSelectionModel().clearSelection();
                // decided that people will be removed from candidate list when put in the selected list
            }
        }
    }


    public void removeCalendar(ActionEvent ae) {

        if ( listViewCalendarSelected.getSelectionModel().getSelectedItem() != null ) {
            Person selected = listViewCalendarSelected.getSelectionModel().getSelectedItem();
            if ( selectedPersonsList.contains(selected) && !candidatePersonsList.contains(selected) )
                selectedPersonsList.remove(selected);
                listViewCalendarSelected.getSelectionModel().clearSelection();
            candidatePersonsList.add(selected);
        }
    }


    private ObservableList<Person> getAllPersons() {
        ArrayList<Person> people =  RegisterSingleton.sharedInstance().getRegister().getPersons();
        return FXCollections.observableArrayList(people);
        //return FXCollections.observableArrayList("Single", "Double", "Suite", "Family App", "Yoda", "kake");
    }



}
