package main;

public class Controller implements Initializable {

    public ListView<String> listViewMonday;
    ObservableList<String> test1;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        test1 = FXCollections.observableArrayList("Single", "Double", "Suite", "Family App", "Double", "Suite", "Family App", "Double", "Suite", "Family App", "Double", "Suite", "Family App", "Double", "Suite", "Family App", "Double", "Suite", "Family App", "Double", "Suite", "Family App", "Double", "Suite", "Family App");
        System.out.println(listViewMonday);
        listViewMonday.setItems(test1);
        listViewMonday.setCellFactory(ComboBoxListCell.forListView(test1));
    }
}
