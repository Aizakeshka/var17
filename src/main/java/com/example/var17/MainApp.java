package com.example.var17;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.List;

public class MainApp extends Application {
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Фильтр ПВЗ");

        TableView<Pvz> table = new TableView<>();

        TableColumn<Pvz, String> cityCol = new TableColumn<>("Город");
        cityCol.setCellValueFactory(new PropertyValueFactory<>("city"));

        TableColumn<Pvz, String> addressCol = new TableColumn<>("Адрес");
        addressCol.setCellValueFactory(new PropertyValueFactory<>("address"));

        TableColumn<Pvz, String> typeCol = new TableColumn<>("Тип");
        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));

        table.getColumns().addAll(cityCol, addressCol, typeCol);

        ObservableList<Pvz> pvzList = FXCollections.observableArrayList();
        try {
            URL url = new URL("https://mrartur0074.github.io/first/simplified_branches.json");
            InputStreamReader reader = new InputStreamReader(url.openStream());

            Gson gson = new Gson();
            Type listType = new TypeToken<List<PvzJson>>(){}.getType();
            List<PvzJson> jsonList = gson.fromJson(reader, listType);

            for (PvzJson pvzJson : jsonList) {
                pvzList.add(new Pvz(pvzJson));
            }
        } catch (Exception e) {
            showError("Ошибка загрузки данных: " + e.getMessage());
        }

        FilteredList<Pvz> filteredData = new FilteredList<>(pvzList);
        table.setItems(filteredData);

        Label countLabel = new Label("Всего ПВЗ: " + pvzList.size());

        ComboBox<String> cityFilter = new ComboBox<>();
        cityFilter.setPromptText("Выберите город");
        cityFilter.getItems().add("Все города");
        pvzList.stream().map(Pvz::getCity).distinct().sorted().forEach(cityFilter.getItems()::add);

        ComboBox<String> typeFilter = new ComboBox<>();
        typeFilter.setPromptText("Выберите тип");
        typeFilter.getItems().addAll("Все типы", "Приём", "Выдача", "Оба");

        Button filterButton = new Button("Применить фильтры");
        filterButton.setOnAction(e -> {
            filteredData.setPredicate(pvz -> {
                boolean cityMatch = cityFilter.getValue() == null
                        || cityFilter.getValue().equals("Все города")
                        || pvz.getCity().equals(cityFilter.getValue());

                boolean typeMatch = typeFilter.getValue() == null
                        || typeFilter.getValue().equals("Все типы")
                        || pvz.getType().equals(typeFilter.getValue());

                return cityMatch && typeMatch;
            });
            countLabel.setText("Найдено ПВЗ: " + filteredData.size());
        });

        HBox filterBox = new HBox(10);
        filterBox.getChildren().addAll(
                new Label("Город:"), cityFilter,
                new Label("Тип:"), typeFilter,
                filterButton
        );
        filterBox.setPadding(new Insets(10));

        VBox root = new VBox(10);
        root.getChildren().addAll(filterBox, countLabel, table);
        root.setPadding(new Insets(10));

        primaryStage.setScene(new Scene(root, 600, 400));
        primaryStage.show();
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Ошибка");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch();
    }
}