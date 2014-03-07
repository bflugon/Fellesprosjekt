package main.participants;

import main.GuiUtils;
import javafx.event.ActionEvent;
import javafx.scene.Node;

/**
 * Created by markuslund92 on 06.03.14.
 */
public class ParticipantsController {

    public void participantsOkOnAction(ActionEvent actionEvent) {
        GuiUtils.closeWindow(actionEvent);
    }
}
