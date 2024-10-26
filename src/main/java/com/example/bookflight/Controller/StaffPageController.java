package com.example.bookflight.Controller;

import com.example.bookflight.Model.Employee;
import com.example.bookflight.Model.Task;
import com.example.bookflight.services.Auth;
import javafx.event.ActionEvent;
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

public class StaffPageController {
    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    private TableView<Task> taskTableView;
    @FXML
    private TableColumn<Task, String> taskNameColumn;
    @FXML
    private TableColumn<Task, String> taskDescriptionColumn;
    @FXML
    private TableColumn<Task, String> taskDueDateColumn;
    @FXML
    private TableColumn<Task, String> statusColumn;

    @FXML
    private Label welcomeLabel;

    // ID pengguna yang sedang login
    private String userId;

    public void initialize() {
        Employee currentUser = Auth.getLoggedInUser();
        userId = currentUser.getId();
        welcomeLabel.setText("Welcome " + currentUser.getName());

        // Mengatur properti kolom tabel
        taskNameColumn.setCellValueFactory(new PropertyValueFactory<>("taskName"));
        taskDescriptionColumn.setCellValueFactory(new PropertyValueFactory<>("taskDescription"));
        taskDueDateColumn.setCellValueFactory(new PropertyValueFactory<>("dueDate"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

        // Membaca data tugas dari file JSON berdasarkan ID pengguna
        List<Task> tasks = readTaskData("data/tasks.json", userId);

        // Menambahkan data ke dalam TableView
        taskTableView.getItems().addAll(tasks);
    }

    public void addAssignment(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("/com/example/bookflight/AddAssignment.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public static List<Task> readTaskData(String filePath, String userId) {
        List<Task> taskList = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            JSONArray jsonArray = new JSONArray(sb.toString());

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject json = jsonArray.getJSONObject(i);
                String employeesId = json.getString("employeesId");

                // Memasukkan hanya tugas yang sesuai dengan ID pengguna
                if (employeesId.equals(userId)) {
                    Task task = new Task(
                            json.getString("id"),
                            json.getString("employeesId"),
                            json.getString("taskName"),
                            json.getString("taskDescription"),
                            json.getString("dueDate"),
                            json.getString("status")
                    );
                    taskList.add(task);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return taskList;
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
