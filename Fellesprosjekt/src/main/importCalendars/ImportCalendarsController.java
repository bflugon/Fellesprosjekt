package main.importCalendars;

import util.GuiUtils;
import javafx.event.ActionEvent;
import javafx.scene.Node;

/**
 * Created by markuslund92 on 06.03.14.
 */
public class ImportCalendarsController {
    
	public void importCalendarsOkOnAction(ActionEvent actionEvent) {
        GuiUtils.closeWindow(actionEvent);
    }
}
