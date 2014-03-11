package main.roomFinder;

import util.GuiUtils;
import javafx.event.ActionEvent;
import javafx.scene.Node;

/**
 * Created by markuslund92 on 06.03.14.
 */
public class RoomFinderController {
    public void closeOnOk(ActionEvent actionEvent) {
        GuiUtils.closeWindow(actionEvent);
    }
}
