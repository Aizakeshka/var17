package com.example.var17;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.*;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;

import java.io.FileReader;
import java.lang.reflect.Type;
import java.util.*;

public class PvzAppController {

    @FXML private ComboBox<String> cityComboBox;
    @FXML private ComboBox<String> regionComboBox;
    @FXML private ToggleGroup typeGroup;
    @FXML private RadioButton typeAll, typeAccept, typeGive;
    @FXML private TableView<Pvz> table;
    @FXML private TableColumn<Pvz, String> colCity, colAddress, colType, colCoordinates;
    @FXML private Label foundLabel;

    private ObservableList<Pvz> allPvz = FXCollections.observableArrayList();

    private final Map<String, String> regionMap = Map.of(
            "Сибирь", "Новосибирск",
            "Урал", "Екатеринбург",
            "Центральный округ", "Москва"
    );

    @FXML
    public void initialize() {
        colCity.setCellValueFactory(data -> new ReadOnlyStringWrapper(data.getValue().getCity()));
        colAddress.setCellValueFactory(data -> new ReadOnlyStringWrapper(data.getValue().getAddress()));
        colType.setCellValueFactory(data -> new ReadOnlyStringWrapper(data.getValue().getType()));
        colCoordinates.setCellValueFactory(data -> new ReadOnlyStringWrapper(data.getValue().getCoordinates()));

        regionComboBox.getItems().addAll(regionMap.keySet());
        regionComboBox.getSelectionModel().selectFirst();

        loadJsonData();
    }

    private void loadJsonData() {
        try {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Выберите JSON файл");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JSON", "*.json"));

            var file = fileChooser.showOpenDialog(null);
            if (file == null) return;

            Gson gson = new Gson();
            Type listType = new TypeToken<List<Pvz>>() {}.getType();
            List<Pvz> data = gson.fromJson(new FileReader(file), listType);
            allPvz.addAll(data);

            Set<String> cities = new HashSet<>();
            for (Pvz p : data) cities.add(p.getCity());

            cityComboBox.getItems().addAll(cities);
            cityComboBox.getSelectionModel().selectFirst();

            applyFilter();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void applyFilter() {
        String selectedCity = cityComboBox.getValue();
        String selectedRegion = regionComboBox.getValue();
        String regionCity = regionMap.getOrDefault(selectedRegion, "");

        String selectedType = ((RadioButton) typeGroup.getSelectedToggle()).getText();

        List<Pvz> filtered = allPvz.filtered(pvz -> {
            boolean matchCity = pvz.getCity().equals(selectedCity);
            boolean matchRegion = pvz.getCity().equals(regionCity);

            boolean matchType = switch (selectedType) {
                case "Приём" -> pvz.getType().equals("Приём");
                case "Выдача" -> pvz.getType().equals("Выдача");
                default -> true;
            };

            return matchCity && matchRegion && matchType;
        });

        table.setItems(FXCollections.observableArrayList(filtered));
        foundLabel.setText("Найдено: " + filtered.size());
    }
}