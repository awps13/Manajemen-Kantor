package com.example.bookflight.Controller;

import com.example.bookflight.Model.Employee;
import com.example.bookflight.Model.Manager;
import com.example.bookflight.Model.Staff;
import com.example.bookflight.services.Auth;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class StaffListController {
    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    private Label welcomeLabel;

    @FXML
    private TableView<Staff> staffListTable;

    @FXML
    private TableColumn<Staff, String> namaColumn;
    @FXML
    private TableColumn<Staff, String> usernameColumn;
    @FXML
    private TableColumn<Staff, String> roleColumn;

    @FXML
    private TextField filterTextField;

    @FXML
    private Button cariButton;

    @FXML
    private Label errorMessage;
    @FXML
    private Label detailLabel;

    public void initialize() {
        Employee currentUser = Auth.getLoggedInUser();
        welcomeLabel.setText("Welcome " + currentUser.getName());

        // Set up columns to retrieve values from Staff class
        namaColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        roleColumn.setCellValueFactory(new PropertyValueFactory<>("role"));

        // Memuat data staf awal
        loadAllStaff();

        // Tambahkan listener untuk menangani klik pada baris
        staffListTable.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) { // Menggunakan double-click untuk melihat detail
                Staff selectedStaff = staffListTable.getSelectionModel().getSelectedItem();
                if (selectedStaff != null) {
                    // Menampilkan detail di detailLabel
                    detailLabel.setText(selectedStaff.getDisplayInfo()); // Memanggil metode getDetails() dari Staff
                }
            }
        });
    }

    public void addStaffPage(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("/com/example/bookflight/AddStaff.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    private void loadAllStaff() {
        // Memuat data staf dari file JSON dan menambahkannya ke TableView
        List<Staff> staffList = readStaffData("data/employees.json");
        staffListTable.getItems().addAll(staffList);
    }

    public void cariButtonAction(ActionEvent event) {
        String filterText = filterTextField.getText();
        String[] filters = filterText.split(",");

        if (filters.length == 2) {
            String name = filters[0].trim();
            String username = filters[1].trim();

            // Panggil metode searchEmployee dengan parameter username dan role
            Employee employee = Employee.searchEmployee(name, username);
            if (employee instanceof Staff) {
                Staff staff = (Staff) employee; // Casting hanya jika objek adalah Staff
                staffListTable.getItems().clear();
                staffListTable.getItems().add(staff);
            } else {
                errorMessage.setText("Staff tidak ditemukan");
            }
        } else {
            loadAllStaff();
        }
    }

    private static List<Staff> readStaffData(String filePath) {
        List<Staff> staffList = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            JSONArray jsonArray = new JSONArray(sb.toString());

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject json = jsonArray.getJSONObject(i);
                Staff staff = new Staff(
                        json.getString("id"),
                        json.getString("name"),
                        json.getString("username"),
                        json.getString("password"),
                        json.getString("role")
                );
                staffList.add(staff);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return staffList;
    }

    public void kembaliButton(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("/com/example/bookflight/Manager.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void logoutButton(ActionEvent event) throws IOException {
        Auth.logout();

        root = FXMLLoader.load(getClass().getResource("/com/example/bookflight/Login.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
