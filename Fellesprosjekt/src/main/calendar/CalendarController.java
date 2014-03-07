package main.calendar;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.cell.ComboBoxListCell;
import javafx.stage.Stage;
import main.GuiUtils;

import java.net.URL;
import java.util.ResourceBundle;

public class CalendarController implements Initializable{

    public ListView<String> listViewMonday;
    ObservableList<String> test1;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        test1 = FXCollections.observableArrayList("Bamse", "Double", "Suite", "Family App", "Double", "Suite", "Family App", "Double", "Suite", "Family App", "Double", "Suite", "Family App", "Double", "Suite", "Family App", "Double", "Suite", "Family App", "Double", "Suite", "Family App", "Double", "Suite", "Family App");
        System.out.println(listViewMonday);
        listViewMonday.setItems(test1);
        listViewMonday.setCellFactory(ComboBoxListCell.forListView(test1));
        //Legger til comment
        //Legger til enda et comment
        //Lolololo
    }

    public void makeMeeting(ActionEvent actionEvent) throws Exception{
        GuiUtils.createView("../meeting/meeting.fxml", "Lag møte", this.getClass());
    }

    public void importButtonOnAction(ActionEvent actionEvent) throws  Exception{
        GuiUtils.createView("../importCalendars/importCalendars.fxml", "Importer", this.getClass());
    }

    //LEGGES EN MER FORNUFTIG PLASS SENERE ^^


}
