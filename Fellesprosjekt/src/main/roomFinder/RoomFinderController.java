package main.roomFinder;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import main.RegisterSingleton;
import main.meeting.MeetingController;
import model.Appointment;
import model.MeetingRoom;
import util.GuiUtils;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

/**
 * Created by markuslund92 on 06.03.14.
 */
public class RoomFinderController implements Initializable {

    //GUI-elementer
    public TableColumn roomTableColumn;
    public TableColumn capacityTableColumn;
    public TableView roomFinderTableView;
    public TextField alternativeRoomTextField;
    public Label chosenRoomLabel;


    public MeetingRoom selectedRoom;
    public int minCapacity;
    ObservableList<MeetingRoom> romData;
    private Appointment appointment;
    private MeetingController parentController;

    //Denne må hentes fra register.
    public MeetingRoom defaultMeetingRoom;
    private ArrayList<MeetingRoom> availibleRooms;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        defaultMeetingRoom = RegisterSingleton.sharedInstance().getRegister().getRoom(1);


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

            appointment.setAlternativeLocation(null);
            appointment.setRoom(room);
            selectedRoom = room;
            chosenRoomLabel.setText(appointment.getRoom().getRoomName());
        }

    }

    public void alternativeRoomKeyReleased() {

        selectedRoom = defaultMeetingRoom;
        chosenRoomLabel.setText(alternativeRoomTextField.getText());
        appointment.setRoom(selectedRoom);
        appointment.setAlternativeLocation(alternativeRoomTextField.getText());

    }

    public void closeOnOk(ActionEvent actionEvent) {

        if (chosenRoomLabel != null && selectedRoom != null){

            parentController.updateView();
            
        }
//        System.out.println("Selected room: " + appointment.getRoom());
//        System.out.println("Alternative room name: " + appointment.getAlternativeLocation());
        GuiUtils.closeWindow(actionEvent);

    }

    private ObservableList<MeetingRoom> fetchRoomData() {


        ObservableList<MeetingRoom> approvedRooms = FXCollections.observableArrayList();

        ArrayList<MeetingRoom> allRooms;

        if(appointment != null){
//            System.out.println("Bruker disse availible rooms: " + availibleRooms);
            allRooms = availibleRooms;

        }else{
            allRooms = RegisterSingleton.sharedInstance().getRegister().getRooms();
        }


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

        //Oppdaterer tableView
        romData = fetchRoomData();
        roomFinderTableView.setItems(romData);
    }

    public void alternativeRoomOnAction(ActionEvent actionEvent) {
        closeOnOk(actionEvent);
    }

    public void setAvailibleRooms(ArrayList<MeetingRoom> available) {
        this.availibleRooms = available;

    }
}
