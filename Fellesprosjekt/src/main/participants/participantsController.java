package main.participants;

import javafx.event.ActionEvent;
import javafx.scene.Node;

/**
 * Created by markuslund92 on 06.03.14.
 */
public class ParticipantsController {
    public void participantsOkButton(ActionEvent actionEvent) {
        ((Node)(actionEvent.getSource())).getScene().getWindow().hide();
    }
}
