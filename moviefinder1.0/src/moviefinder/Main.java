package moviefinder;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.File;
import java.util.Set;


public class Main extends Application implements SearchEventProvider, UpdateEventProvider{

     private MovieManager manager;

    private ComboBox<String> categoryCombo = new ComboBox<>();
    private TextField searchField = new TextField();
    private Text updateMessage = new Text();

    private TableView<MovieAdapter> table = new TableView<MovieAdapter>();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        File[] mainDirs = SettingsLoader.loadMainDirs(primaryStage);
        File extensionFile = SettingsLoader.loadExtensionsFile(primaryStage);

        manager = new MovieManager(mainDirs,extensionFile);

        //manager.printAllMovies(); //todo remove test print
        //manager.printCategories();

        Scene scene = createScene(manager);
        primaryStage.setTitle("Movie Finder..");
        primaryStage.setScene(scene);
        scene.getStylesheets().add(Controller.class.getResource("style.css").toExternalForm());
        primaryStage.show();
    }


    ///////////////////////////////////////
    // Helper methods to layout creation //
    ///////////////////////////////////////

    private Scene createScene(MovieManager manager){
        BorderPane border = new BorderPane();

        GridPane searchArea = createSearchArea(manager);
        VBox table = createTableView();
        HBox options = createOptions();

        VBox top = new VBox();
        top.getChildren().addAll(options, searchArea);

        border.setBottom(table);
        border.setTop(top);

        Scene scene = new Scene(border,1000,575);

        return scene;
    }

    private HBox createOptions(){
        HBox hbox = new HBox();
        hbox.setAlignment(Pos.BOTTOM_RIGHT);
        hbox.setPadding(new Insets(10,10,0,0));

        Button updateButton = new Button();
        updateButton.setText("Update");
        updateButton.setOnAction(new UpdateEventHandler(this));

       /* HBox messageBox = new HBox();
        messageBox.setPadding(new Insets(10, 10, 0, 0));
        Text status = new Text();
        status.setText("Status: ");
        updateMessage.setText("Updated");
        messageBox.getChildren().addAll(status, updateMessage); */

        VBox vbox = new VBox();
        vbox.getChildren().addAll(updateButton/*,messageBox*/);

        hbox.getChildren().add(vbox);

        return hbox;
    }

    private VBox createTableView(){
        TableColumn titleCol = new TableColumn("Movie Title");
        titleCol.setMinWidth(200);
        titleCol.setCellValueFactory(new PropertyValueFactory<MovieAdapter,String>("title"));

        TableColumn locationCol = new TableColumn("Location");
        locationCol.setMinWidth(200);
        locationCol.setCellValueFactory(new PropertyValueFactory<MovieAdapter,String>("mainDir"));

        TableColumn categoryCol = new TableColumn("Category");
        categoryCol.setMinWidth(200);
        categoryCol.setCellValueFactory(new PropertyValueFactory<MovieAdapter,String>("categories"));

        table.getColumns().addAll(titleCol,locationCol,categoryCol);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        final Text tableTitle = new Text("Search Result");
        tableTitle.getStyleClass().add("heading-text");

        VBox vbox = new VBox();
        vbox.setSpacing(5);
        vbox.setPadding(new Insets(10, 10, 10, 10));
        vbox.setAlignment(Pos.CENTER);
        vbox.getChildren().addAll(tableTitle,table);

        return vbox;
    }

    private GridPane createSearchArea(MovieManager manager){
        GridPane pane = new GridPane();
        pane.setHgap(10); // horizontal space between columns
        pane.setVgap(10); // vertical space between rows
        pane.setPadding(new Insets(25, 25, 25, 25)); // padding from GridPane to boarder

        // Title
        Text sceneTitle = new Text("Movie Finder");
        sceneTitle.getStyleClass().add("heading-text");
        pane.add(sceneTitle,0,0,2,1);

        // Search options
        Label searchLabel = new Label("Search:");
        pane.add(searchLabel, 0, 1);
        pane.add(searchField, 1, 1, 2, 1);
        Label categoryLabel = new Label("Category:");
        pane.add(categoryLabel,0,2);
        categoryCombo.setPromptText("none");
        categoryCombo.getItems().add("none");
        categoryCombo.getItems().addAll(manager.getCategories());
        pane.add(categoryCombo, 1, 2, 2, 1);

        // Search button
        HBox hbox = new HBox();
        hbox.setAlignment(Pos.BOTTOM_RIGHT);
        hbox.setSpacing(10);
        Button goButton = new Button();
        goButton.setText("GO");
        goButton.setOnAction(new SearchEventHandler(this));
        hbox.getChildren().add(goButton);
        pane.add(hbox,8,2);

        return pane;
    }



    ///////////////////////
    // Interface methods //
    ///////////////////////

    public SearchInfo getEventInfo(){
        String searchString = searchField.getCharacters().toString();
        String categoryString = categoryCombo.getValue();
        SearchInfo info = new SearchInfo(searchString,categoryString);
        return info;
    }

    public MovieManager getManager(){
        return manager;
    }

    @Override
    public void update() {
        manager.update();
        categoryCombo.getItems().clear();
        categoryCombo.setPromptText("none");
        categoryCombo.getItems().add("none");
        categoryCombo.getItems().addAll(manager.getCategories());
    }

    public void addEntriesToTable(Set<Movie> movies){
        if(movies.isEmpty()){
            System.out.println("No movies match the search");
            return;
        }

        ObservableList<MovieAdapter> data = FXCollections.observableArrayList();
        for(Movie m : movies){
            MovieAdapter ma = new MovieAdapter(m);
            data.add(ma);
        }

        table.setItems(data);
    }


}
