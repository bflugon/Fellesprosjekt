package main;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Created by markuslund92 on 06.03.14.
 */
public class GuiUtils {

    public static void createView(String position, String title, Class c) throws Exception{
        Stage newStage = new Stage();
        Parent root = FXMLLoader.load(c.getResource(position));
        newStage.setTitle(title);
        newStage.setScene(new Scene(root));
        newStage.show();
    }

}
