package main.roomFinder;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by markuslund92 on 06.03.14.
 */
public class RoomFinderController implements Initializable {

    public TableColumn roomTableColumn;
    public TableColumn capacityTableColumn;
    public TableView roomFinderTableView;
    ObservableList<TestRoom> romData;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        //Henter alle rom
        romData = fetchRoomData();

        capacityTableColumn.setSortType(TableColumn.SortType.ASCENDING);

        roomTableColumn.setCellValueFactory(new PropertyValueFactory<TestRoom, String>("roomName"));
        capacityTableColumn.setCellValueFactory(new PropertyValueFactory<TestRoom, String>("capacity"));

        roomFinderTableView.setItems(romData);
        roomFinderTableView.getSortOrder().add(capacityTableColumn);

    }

    public void closeOnOk(ActionEvent actionEvent) {
        ObservableList<TablePosition> c;
        c = roomFinderTableView.getSelectionModel().getSelectedCells();
        int rowIndex = c.get(0).getRow();
        System.out.println(romData.get(rowIndex).toString());

        ((Node)(actionEvent.getSource())).getScene().getWindow().hide();

    }

    private ObservableList<TestRoom> fetchRoomData() {
        return FXCollections.observableArrayList(
                new TestRoom("Rom 1", 5),
                new TestRoom("Rom 3", 4),
                new TestRoom("Rom 4", 4),
                new TestRoom("Rom 5", 7),
                new TestRoom("Rom 6", 10),
                new TestRoom("Rom 7", 10),
                new TestRoom("Rom 8", 10),
                new TestRoom("Rom 9", 10),
                new TestRoom("Rom 10", 20),
                new TestRoom("Rom 11", 30),
                new TestRoom("Rom 12", 35),
                new TestRoom("Rom 13", 25),
                new TestRoom("Rom 14", 50),
                new TestRoom("Rom 15", 12),
                new TestRoom("Rom 16", 7),
                new TestRoom("Rom 17", 7),
                new TestRoom("Rom 2", 12)
        );
    }

    public static class TestRoom {

        private String roomName;
        private int capacity;

        private TestRoom(String roomName, int capacity) {
            this.roomName = roomName;
            this.capacity = capacity;
        }

        public String getRoomName() {
            return roomName;
        }

        public void setRoomName(String name) {
            this.roomName = name;
        }

        public int getCapacity() {
            return this.capacity;
        }

        public void setCapacity(int cap) {
            this.capacity = cap;
        }

        public String toString(){
            return this.roomName;
        }

    }

}
