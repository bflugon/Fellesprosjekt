package util;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class GuiUtils {

    public static void createView(String position, String title, Class c) throws Exception{
        Stage newStage = new Stage();
        Parent root = FXMLLoader.load(c.getResource(position));
        newStage.setTitle(title);
        newStage.setScene(new Scene(root));
        newStage.show();
    }
    
    public static void closeWindow(ActionEvent actionEvent) {
    	((Node) (actionEvent.getSource())).getScene().getWindow().hide();
    }

}
