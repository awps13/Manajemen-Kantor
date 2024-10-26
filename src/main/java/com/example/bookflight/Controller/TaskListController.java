package com.example.bookflight.Controller;

import com.example.bookflight.Model.Employee;
import com.example.bookflight.Model.Staff;
import com.example.bookflight.Model.Task;
import com.example.bookflight.services.Auth;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class TaskListController {
    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    private Label welcomeLabel;

    @FXML
    private TableView<Task> taksListTableView;

    @FXML
    private TableColumn<Task, String> namaTaskColumn;
    @FXML
    private TableColumn<Task, String> namaStaffColumn;
    @FXML
    private TableColumn<Task, String> deskripsiTaskColumn;
    @FXML
    private TableColumn<Task, String> tenggatColumn;
    @FXML
    private TableColumn<Task, String> statusColumn;

    @FXML
    private Label detailLabel;

    // Method to print the logged-in user information
    public void initialize() {
        Employee currentUser = Auth.getLoggedInUser();

        welcomeLabel.setText("Welcome " + currentUser.getName());

        // Set up columns to retrieve values from Staff class
        namaTaskColumn.setCellValueFactory(new PropertyValueFactory<>("taskName"));
        namaStaffColumn.setCellValueFactory(new PropertyValueFactory<>("employeesId"));
        deskripsiTaskColumn.setCellValueFactory(new PropertyValueFactory<>("taskDescription"));
        tenggatColumn.setCellValueFactory(new PropertyValueFactory<>("dueDate"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

        // Load data from JSON file
        List<Task> tasksList = readTasksData("data/tasks.json");
        taksListTableView.getItems().addAll(tasksList);

        // Tambahkan listener untuk menangani klik pada baris
        taksListTableView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) { // Menggunakan double-click untuk melihat detail
                Task selectedTask = taksListTableView.getSelectionModel().getSelectedItem();
                if (selectedTask != null) {
                    // Menampilkan detail di detailLabel
                    detailLabel.setText(selectedTask.getDisplayInfo());
                }
            }
        });
    }

    public void addTaskPage(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("/com/example/bookflight/AddTask.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public static List<Task> readTasksData(String filePath) {
        List<Task> tasksList = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            JSONArray jsonArray = new JSONArray(sb.toString());

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject json = jsonArray.getJSONObject(i);

                Task task = new Task(
                        json.getString("id"),
                        json.getString("employeesId"),
                        json.getString("taskName"),
                        json.getString("taskDescription"),
                        json.getString("dueDate"),
                        json.getString("status")
                );
                tasksList.add(task);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return tasksList;
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
