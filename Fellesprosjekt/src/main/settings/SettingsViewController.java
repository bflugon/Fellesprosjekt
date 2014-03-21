package main.settings;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import main.Register;
import main.RegisterSingleton;
import main.calendar.CalendarController;
import model.MeetingRoom;
import util.GuiUtils;

import java.net.URL;
import java.util.Calendar;
import java.util.ResourceBundle;

/**
 * Created by kristian on 18.03.14.
 */
public class SettingsViewController implements Initializable {

    public CheckBox alarmCheckBox;
    public CheckBox hideMeetingsCheckBox;

    public TextField hoursBeforeLabel;


    public CalendarController parentController;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if (RegisterSingleton.sharedInstance().getRegister().getHasAlarm() != null){
            alarmCheckBox.setSelected(RegisterSingleton.sharedInstance().getRegister().getHasAlarm() );
        }

        if (RegisterSingleton.sharedInstance().getRegister().getHidesNotAttendingMeetings()  != null){
            hideMeetingsCheckBox.setSelected(RegisterSingleton.sharedInstance().getRegister().getHidesNotAttendingMeetings());
        }

        if (RegisterSingleton.sharedInstance().getRegister().getHoursBeforeAlarm() > 0 ){
            hoursBeforeLabel.setText(Integer.toString(RegisterSingleton.sharedInstance().getRegister().getHoursBeforeAlarm()));
        }

    }

    public void setParentController(CalendarController parentController) {
        this.parentController = parentController;
    }


    public void okButtonPressed(ActionEvent event) {
        RegisterSingleton.sharedInstance().getRegister().getAlertAppointments().clear();
        if (alarmCheckBox.isSelected() == true){
            RegisterSingleton.sharedInstance().getRegister().setHasAlarm(true);
        }else{
            RegisterSingleton.sharedInstance().getRegister().setHasAlarm(false);
        }

        if (hideMeetingsCheckBox.isSelected() == true){
            RegisterSingleton.sharedInstance().getRegister().setHidesNotAttendingMeetings(true);
        }else{
            RegisterSingleton.sharedInstance().getRegister().setHidesNotAttendingMeetings(false);
        }

        if (hoursBeforeLabel.getText().length() > 0 &&
                Integer.parseInt(hoursBeforeLabel.getText()) > 0){
            RegisterSingleton.sharedInstance().getRegister().setHoursBeforeAlarm(Integer.parseInt(hoursBeforeLabel.getText()));
        }
        parentController.updateCalendarView();

        GuiUtils.closeWindow(event);
    }



}