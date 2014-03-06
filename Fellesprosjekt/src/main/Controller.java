package main;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.cell.ComboBoxListCell;

import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    public ListView<String> listViewMonday;
    ObservableList<String> test1;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        test1 = FXCollections.observableArrayList("Single", "Double", "Suite", "Family App", "Double", "Suite", "Family App", "Double", "Suite", "Family App", "Double", "Suite", "Family App", "Double", "Suite", "Family App", "Double", "Suite", "Family App", "Double", "Suite", "Family App", "Double", "Suite", "Family App");
        System.out.println(listViewMonday);
        listViewMonday.setItems(test1);
        listViewMonday.setCellFactory(ComboBoxListCell.forListView(test1));
    }
}
