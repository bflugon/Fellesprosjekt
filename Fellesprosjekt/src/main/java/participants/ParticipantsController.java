package participants;

import javafx.event.ActionEvent;
import util.GuiUtils;

/**
 * Created by markuslund92 on 06.03.14.
 */
public class ParticipantsController {

    public void participantsOkOnAction(ActionEvent actionEvent) {
        GuiUtils.closeWindow(actionEvent);
    }
}
