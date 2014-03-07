package main.calendar;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.cell.ComboBoxListCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.control.Button;

import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Callback;
import util.GuiUtils;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class CalendarController implements Initializable{

    public ListView<String> listViewMonday;

    ObservableList<String> test1;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        test1 = FXCollections.observableArrayList("Bamse", "Double", "Suite", "Family App", "Double", "Suite", "Family App", "Double", "Suite", "Family App", "Double", "Suite", "Family App", "Double", "Suite", "Family App", "Double", "Suite", "Family App", "Double", "Suite", "Family App", "Double", "Suite", "Family App");
        System.out.println(listViewMonday);
        listViewMonday.setItems(test1);
        //listViewMonday.setCellFactory(ComboBoxListCell.forListView(test1));
        //Legger til comment
        //Legger til enda et comment
        //

        listViewMonday.setCellFactory(new Callback<ListView<String>,
                        ListCell<String>>() {
            @Override
            public ListCell<String> call(ListView<String> list) {
                return new CalenderCell();
            }
        }
        );
    }

    static class CalenderCell extends ListCell<String> {
        VBox vbox = new VBox();
        Label label = new Label("(empty)");
        Pane pane = new Pane();
        Label clockLabel = new Label("(empty)");

        String lastItem;

        public CalenderCell() {
            super();
            vbox.getChildren().addAll(label, pane, clockLabel);
            HBox.setHgrow(pane, Priority.ALWAYS);
        }

        @Override
        protected void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);
            setText(null);
            if (empty) {
                setGraphic(null);
            } else {
                label.setText(item!=null ? item : "<null>");
                clockLabel.setText("12.30");
                clockLabel.setTextFill(Color.GRAY);
                setGraphic(vbox);
            }
        }
    }

    public void makeMeeting(ActionEvent actionEvent) throws Exception{
        GuiUtils.createView("../meeting/meeting.fxml", "Lag møte", this.getClass());
    }

    public void importButtonOnAction(ActionEvent actionEvent) throws  Exception{
        GuiUtils.createView("../importCalendars/importCalendars.fxml", "Importer", this.getClass());
    }

    //LEGGES EN MER FORNUFTIG PLASS SENERE ^^


}
