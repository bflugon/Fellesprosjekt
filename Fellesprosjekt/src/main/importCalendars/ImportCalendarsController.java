package main.importCalendars;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import main.RegisterSingleton;
import main.calendar.CalendarController;
import model.Person;
import util.GuiUtils;
import javafx.event.ActionEvent;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

/**
 * Created by markuslund92 on 06.03.14.
 */
public class ImportCalendarsController implements Initializable {

    public ListView<Person> listViewCalendarCandidates;
    public ListView<Person> listViewCalendarSelected;




    CalendarController parentController;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void setParentController(CalendarController parentController) {
//        this.parentController = parentController;
//
//        for(Person person : parentController.getOtherUsersAppointmentsToShow()){
//            if (parentController.getAllCalendarUsers().contains(person)){
//
//            }
//        }
//
//
//        listViewCalendarCandidates.setItems(candidatePersonsList);
//        selectedPersonsList = FXCollections.observableArrayList();
//        listViewCalendarSelected.setItems(selectedPersonsList);
    }



    public void okButtonPressed(ActionEvent actionEvent) {
        GuiUtils.closeWindow(actionEvent);
    }

    public void addCalendar(ActionEvent ae) {
//
//        if (listViewCalendarCandidates.getSelectionModel().getSelectedItem() != null) {
//            String selected = listViewCalendarCandidates.getSelectionModel().getSelectedItem();
//            if ( candidatePersonsList.contains(selected) && !selectedPersonsList.contains(selected) ) {
//                selectedPersonsList.add(selected);
//                candidatePersonsList.remove(selected);
//                listViewCalendarCandidates.getSelectionModel().clearSelection();
//                // decided that people will be removed from candidate list when put in the selected list
//            }
//        }
    }


    public void removeCalendar(ActionEvent ae) {

//        if ( listViewCalendarSelected.getSelectionModel().getSelectedItem() != null ) {
//            String selected = listViewCalendarSelected.getSelectionModel().getSelectedItem();
//            if ( selectedPersonsList.contains(selected) && !candidatePersonsList.contains(selected) )
//                selectedPersonsList.remove(selected);
//                listViewCalendarSelected.getSelectionModel().clearSelection();
//            candidatePersonsList.add(selected);
//        }
    }


    private ArrayList<Person> getAllPersons() {
        return RegisterSingleton.sharedInstance().getRegister().getPersons();
    }



}
