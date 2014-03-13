package meeting;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import roomFinder.RoomFinderController;
import model.Appointment;
import util.GuiUtils;

import java.net.URL;
import java.util.ResourceBundle;

public class MeetingController implements Initializable {

    Appointment appointment;
    public Button meetingRoomButton;
    public TextField startTimeTextField;
    public TextField startDateTextField;
    public TextField endTimeTextField;
    public TextField endDateTextField;
    public TextField nameTextField;
    public RadioButton lowPriRadioButton;
    public RadioButton mediumPriRadioButton;
    public RadioButton highPriRadioButton;
    public TextArea descriptionTextArea;




    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        appointment = new Appointment(0, null, null, null, null, 0, null, null,null, null);
    }

    public void nameTextFieldType(){
        nameTextField.setStyle("-fx-border-width: 0px;");
    }
    public void startTimeTextFieldType(){
        startTimeTextField.setStyle("-fx-border-width: 0px;");
    }
    public void startDateTextFieldType(){
        startDateTextField.setStyle("-fx-border-width: 0px;");
    }
    public void endTimeTextFieldType(){
        endTimeTextField.setStyle("-fx-border-width: 0px;");
    }
    public void endDateTextFieldType(){
        endDateTextField.setStyle("-fx-border-width: 0px;");
    }

    public void chooseRoom(ActionEvent actionEvent) throws Exception{

        Stage newStage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("src/main/roomFinder/roomFinder.fxml"));
        Parent root = (Parent)fxmlLoader.load();
        RoomFinderController roomFinderController = fxmlLoader.<RoomFinderController>getController();
        roomFinderController.setAppointment(appointment);
        roomFinderController.setParentController(this);
        if (appointment.getRoom() != null){
            if (appointment.getRoom().getRoomID() == 1){
                roomFinderController.setRoom(appointment.getAlternativeRoomName());
            }else{
                roomFinderController.setRoom(appointment.getRoom().getRoomName());
            }
        }

        newStage.setTitle("Velg rom");
        newStage.setScene(new Scene(root));
        newStage.show();
    }

    public void chooseParticipants(ActionEvent actionEvent) throws Exception{
        GuiUtils.createView("src/main/participants/participants.fxml", "Velg deltaker", this.getClass());
    }

    public void exitOnSave(ActionEvent actionEvent) {
        String redBorderStyling = "-fx-border-color: RED; -fx-border-width: 2px;";

//        System.out.println("Valid date:");
//        System.out.println(isValidDate(startDateTextField.getText()));
//
//        System.out.println("Valid time:");
//        System.out.println(isValidTime(startTimeTextField.getText()));

        if (nameTextField.getText().equals("")){
            nameTextField.setPromptText("FYLL INN NAVN!");
            //nameTextField.setEffect(redDropShadow);
            nameTextField.setStyle(redBorderStyling);
        }

        else if(!isValidTime(startTimeTextField.getText())){

            System.out.println("Invalid startTime format: " + startTimeTextField.getText());
            startTimeTextField.setStyle(redBorderStyling);

        }else if(!isValidDate(startDateTextField.getText())){

            System.out.println("Invalid startDate format: " + startDateTextField.getText());
            startDateTextField.setStyle(redBorderStyling);

        }else if(!isValidTime(endTimeTextField.getText())){

            System.out.println("Invalid endTime format: " + endTimeTextField.getText());
            endTimeTextField.setStyle(redBorderStyling);


        }else if(!isValidDate(endDateTextField.getText())){

            System.out.println("Invalid endDate format: " + endDateTextField.getText());
            endDateTextField.setStyle(redBorderStyling);

        }

        else{
            String meetingName = nameTextField.getText();
//            String start =


            GuiUtils.closeWindow(actionEvent);
        }
    }

    private boolean isValidDate(String s){
        if (s.length() > 10 || s.length() < 10){
            return false;
        }
        if ( Character.isDigit(s.charAt(0)) && Character.isDigit(s.charAt(1))
                && s.charAt(2) == '.' && s.charAt(5) == '.'
                && Character.isDigit(s.charAt(3)) && Character.isDigit(s.charAt(4))
                && Character.isDigit(s.charAt(6)) && Character.isDigit(s.charAt(7))
                && Character.isDigit(s.charAt(8)) && Character.isDigit(s.charAt(9)) ){
            return true;
        }
        return false;
    }

    private boolean isValidTime(String s){
        if (s.length() > 5 || s.length() < 5){
            return false;
        }
        if (Character.isDigit(s.charAt(0)) && Character.isDigit(s.charAt(1)) && s.charAt(2) == ':' && Character.isDigit(s.charAt(3)) && Character.isDigit(s.charAt(4))){
            return true;
        }
        return false;
    }



    public void updateView() {
        if (appointment.getAlternativeRoomName() == null){
            meetingRoomButton.setText(appointment.getRoom().getRoomName());
        }else{
            meetingRoomButton.setText(appointment.getAlternativeRoomName());
        }
    }
}
