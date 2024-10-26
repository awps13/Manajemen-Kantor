package com.example.bookflight.Controller;

import com.example.bookflight.Model.Employee;
import com.example.bookflight.services.Auth;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class AddTaskController {
    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    private ChoiceBox<String> employeeChoiceBox;
    @FXML
    private TextField taskNameTextField;
    @FXML
    private TextArea descriptionTextArea;
    @FXML
    private DatePicker dueDatePicker;
    @FXML
    private Label namaPekerjaErrorMessage, namaTaskErrorMessage, descriptionErrorMessage, tenggatErrorMessage;

    private List<Employee> employees;

    public void initialize() {
        // Membaca data karyawan dari file JSON
        employees = readEmployeeData("data/employees.json");

        // Menambahkan nama karyawan ke ChoiceBox
        for (Employee employee : employees) {
            employeeChoiceBox.getItems().add(employee.getName());
        }
    }

    public void addTaskButton(ActionEvent event) throws IOException {
        // Mengambil data dari UI
        String id = UUID.randomUUID().toString();
        String selectedEmployeeName = employeeChoiceBox.getValue();
        String taskName = taskNameTextField.getText();
        String description = descriptionTextArea.getText();
        String dueDate = dueDatePicker.getValue().toString(); // Mengambil tanggal sebagai string

        // Validation
        if(selectedEmployeeName == null) {
            namaPekerjaErrorMessage.setText("(Masukkan nama staff)");
            return;
        }
        if(taskName.isEmpty()) {
            namaTaskErrorMessage.setText("(Masukkan nama task)");
            return;
        }
        if(description.isEmpty()) {
            descriptionErrorMessage.setText("(Masukkan deskripsi task)");
            return;
        }
        if(dueDate.isEmpty()) {
            tenggatErrorMessage.setText("(Masukkan tenggat task)");
            return;
        }

        // Mencari ID karyawan berdasarkan nama
        String employeeId = null;
        for (Employee employee : employees) {
            if (employee.getName().equals(selectedEmployeeName)) {
                employeeId = employee.getId();
                break;
            }
        }

        // Menyimpan data ke file JSON
        if (employeeId != null) {
            addTaskData("data/tasks.json", id, employeeId, taskName, description, dueDate);

            root = FXMLLoader.load(getClass().getResource("/com/example/bookflight/TaskList.fxml"));
            stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }
    }

    public static void addTaskData(String filePath, String id, String employeeId, String taskName, String taskDescription, String dueDate) {
        JSONArray jsonArray;

        try {
            // Membaca konten file JSON
            String fileContent = new String(Files.readAllBytes(Paths.get(filePath)));
            if (fileContent.isEmpty()) {
                jsonArray = new JSONArray();
            } else {
                jsonArray = new JSONArray(fileContent);
            }

            // Membuat objek JSON untuk data baru
            JSONObject json = new JSONObject();
            json.put("id", id);
            json.put("employeesId", employeeId);
            json.put("taskName", taskName);
            json.put("taskDescription", taskDescription);
            json.put("dueDate", dueDate);
            json.put("status", "pengerjaan");

            // Menambahkan objek JSON ke dalam array
            jsonArray.put(json);

            // Menyimpan kembali ke dalam file JSON
            try (FileWriter fileWriter = new FileWriter(filePath)) {
                fileWriter.write(jsonArray.toString(4)); // Indentasi 4 spasi
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<Employee> readEmployeeData(String filePath) {
        List<Employee> employeeList = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            JSONArray jsonArray = new JSONArray(sb.toString());

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject json = jsonArray.getJSONObject(i);
                Employee employee = new Employee(
                        json.getString("id"),
                        json.getString("name"),
                        json.getString("username"),
                        json.getString("password"),
                        json.getString("role")
                );
                employeeList.add(employee);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return employeeList;
    }

    public void kembaliButton(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("/com/example/bookflight/TaskList.fxml"));
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
