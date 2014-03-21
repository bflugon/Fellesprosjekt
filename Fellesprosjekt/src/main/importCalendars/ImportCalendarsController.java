package main.importCalendars;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;
import main.Register;
import main.RegisterSingleton;
import main.calendar.CalendarController;
import model.Appointment;
import model.Person;
import util.GeneralUtil;
import util.GuiUtils;
import javafx.event.ActionEvent;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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
        this.parentController = parentController;
        listViewCalendarCandidates.setItems(RegisterSingleton.sharedInstance().getRegister().getAvailableAdditionalPersonCalendars());
        listViewCalendarSelected.setItems(RegisterSingleton.sharedInstance().getRegister().getSelectedAdditionalPersonCalendars());

        listViewCalendarCandidates.setCellFactory(
                new Callback<ListView<Person>, ListCell<Person>>() {
                    @Override
                    public ListCell<Person> call(ListView<Person> param) {
                        final ListCell<Person> cell = new ListCell<Person>() {
                            {
                                super.setPrefWidth(100);
                            }

                            @Override
                            public void updateItem(Person item,
                                                   boolean empty) {
                                super.updateItem(item, empty);
                                if (item != null) {
                                    setText(item.getName());

                                } else {
                                    setText(null);
                                }
                            }
                        };
                        return cell;
                    }
                });

        listViewCalendarSelected.setCellFactory(
                new Callback<ListView<Person>, ListCell<Person>>() {
                    @Override public ListCell<Person> call(ListView<Person> param) {
                        final ListCell<Person> cell = new ListCell<Person>() {
                            {
                                super.setPrefWidth(100);
                            }
                            @Override public void updateItem(Person item,
                                                             boolean empty) {
                                super.updateItem(item, empty);
                                if (item != null) {
                                    setText(item.getName());

                                }
                                else {
                                    setText(null);
                                }
                            }
                        };
                        return cell;
                    }
                });


    }



    public void okButtonPressed(ActionEvent actionEvent) {
        parentController.updateCalendarView();
        GuiUtils.closeWindow(actionEvent);
    }

    public void addCalendar(ActionEvent ae) {

        if (listViewCalendarCandidates.getSelectionModel().getSelectedItem() != null) {
            Person selected = listViewCalendarCandidates.getSelectionModel().getSelectedItem();

//
//            if ( RegisterSingleton.sharedInstance().getRegister().getAvailableAdditionalPersonCalendars().contains(selected)
//                    && !RegisterSingleton.sharedInstance().getRegister().getSelectedAdditionalPersonCalendars().contains(selected) ) {
//
//                // decided that people will be removed from candidate list when put in the selected list
//            }

            RegisterSingleton.sharedInstance().getRegister().getSelectedAdditionalPersonCalendars().add(selected);
            RegisterSingleton.sharedInstance().getRegister().getAvailableAdditionalPersonCalendars().remove(selected);
            listViewCalendarCandidates.getSelectionModel().clearSelection();
        }
    }


    public void removeCalendar(ActionEvent ae) {
        System.out.println("Remove called");
        if (listViewCalendarSelected.getSelectionModel().getSelectedItem() != null ) {
            System.out.println("Remove called success");
            Person selected = listViewCalendarSelected.getSelectionModel().getSelectedItem();
            RegisterSingleton.sharedInstance().getRegister().getAvailableAdditionalPersonCalendars().add(selected);
            RegisterSingleton.sharedInstance().getRegister().getSelectedAdditionalPersonCalendars().remove(selected);
            listViewCalendarSelected.getSelectionModel().clearSelection();
        }
    }



}
