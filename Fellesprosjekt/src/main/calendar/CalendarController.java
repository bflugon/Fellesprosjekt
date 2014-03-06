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
    }

    public void makeMeeting(ActionEvent actionEvent) throws Exception{



        Stage newStage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("../meeting/meeting.fxml"));
        newStage.setTitle("Hello World");
        newStage.setScene(new Scene(root));
        newStage.show();




    }
}
