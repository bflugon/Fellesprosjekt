package main.roomFinder;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import main.meeting.MeetingController;
import model.Appointment;
import model.MeetingRoom;
import util.GuiUtils;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by markuslund92 on 06.03.14.
 */
public class RoomFinderController implements Initializable {

    public TableColumn roomTableColumn;
    public TableColumn capacityTableColumn;
    public TableView roomFinderTableView;
    public TextField alternativeRoomTextField;
    public MeetingRoom selectedRoom;
    public Label chosenRoomLabel;
    public int minCapacity = 0;
    ObservableList<MeetingRoom> romData;
    private Appointment appointment;
    private MeetingController parentController;

    //Denne må hentes fra register.
    public MeetingRoom defaultMeetingRoom = new MeetingRoom(1, "Default", 999);



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        romData = fetchRoomData();
        capacityTableColumn.setSortType(TableColumn.SortType.ASCENDING);
        roomTableColumn.setCellValueFactory(new PropertyValueFactory<MeetingRoom, String>("roomName"));
        capacityTableColumn.setCellValueFactory(new PropertyValueFactory<MeetingRoom, String>("capacity"));
        roomFinderTableView.setItems(romData);
        roomFinderTableView.getSortOrder().add(capacityTableColumn);
    }

    public void clickSelection(){

        if (!roomFinderTableView.getSelectionModel().getSelectedCells().isEmpty()){

            ObservableList<TablePosition> c;
            c = roomFinderTableView.getSelectionModel().getSelectedCells();
            int rowIndex = c.get(0).getRow();
            MeetingRoom room = romData.get(rowIndex);

            appointment.setAlternativeRoomName(null);
            appointment.setRoom(room);
            selectedRoom = room;
            chosenRoomLabel.setText(appointment.getRoom().getRoomName());

        }

    }

    public void alternativeRoomKeyReleased() {

        selectedRoom = defaultMeetingRoom;
        chosenRoomLabel.setText(alternativeRoomTextField.getText());
        appointment.setRoom(selectedRoom);
        appointment.setAlternativeRoomName(alternativeRoomTextField.getText());

    }

    public void closeOnOk(ActionEvent actionEvent) {

        if (chosenRoomLabel != null && selectedRoom != null){

            parentController.updateView();
            
        }
        GuiUtils.closeWindow(actionEvent);

    }

    private ObservableList<MeetingRoom> fetchRoomData() {
        ObservableList<MeetingRoom> approvedRooms = FXCollections.observableArrayList();
        ObservableList<MeetingRoom> allRooms = FXCollections.observableArrayList(
                new MeetingRoom(12,"Rom 1", 12),
                new MeetingRoom(2,"Rom 2", 5),
                new MeetingRoom(3,"Rom 3", 4),
                new MeetingRoom(4,"Rom 4", 4),
                new MeetingRoom(5,"Rom 5", 7),
                new MeetingRoom(6,"Rom 6", 10),
                new MeetingRoom(7,"Rom 7", 10),
                new MeetingRoom(8,"Rom 8", 10),
                new MeetingRoom(9,"Rom 9", 10),
                new MeetingRoom(10,"Rom 10", 20),
                new MeetingRoom(11,"Rom 17", 7)
        );

        for (MeetingRoom r : allRooms){
            if (r.getCapacity() > this.minCapacity){
                approvedRooms.add(r);
            }
        }
        return approvedRooms;
    }

    public void setAppointment(Appointment appointment) {
        this.appointment = appointment;
    }

    public void setParentController(MeetingController m){
        parentController = m;
    }

    public void setRoom(String s){
        if (appointment.getRoom().getRoomID() == 1){
            this.alternativeRoomTextField.setText(s);
        }
            this.chosenRoomLabel.setText(s);
    }

    public void setMinCapacity(int i){
        this.minCapacity = i;
    }
}
