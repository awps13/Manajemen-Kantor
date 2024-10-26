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
import javafx.scene.control.ChoiceBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class AddAssignmentController {
    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    private ChoiceBox<String> taskChoiceBox;

    private File selectedFile;
    private List<Task> tasks;  // Daftar tugas yang akan diisi dari JSON
    private String employeeId;  // ID pengguna yang login (gantikan sesuai)

    @FXML
    public void initialize() {
        Employee currentUser = Auth.getLoggedInUser();
        employeeId = currentUser.getId();

        // Membaca data tugas dari JSON
        tasks = readTaskData("data/tasks.json", employeeId);

        // Mengisi ChoiceBox dengan nama tugas
        for (Task task : tasks) {
            taskChoiceBox.getItems().add(task.getTaskName());
        }
    }

    public void handleChooseFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Pilih File Assignment");

        // Menampilkan dialog untuk memilih file
        selectedFile = fileChooser.showOpenDialog(new Stage());

        if (selectedFile != null) {
            System.out.println("File yang dipilih: " + selectedFile.getName());
            copyFileToResources(selectedFile); // Menyalin file ke folder resources
        }
    }

    private void copyFileToResources(File sourceFile) {
        try {
            String resourcesPath = "data/fileassignment/"; // Ganti sesuai dengan path folder resources Anda
            File destinationFile = new File(resourcesPath + sourceFile.getName());
            Files.copy(sourceFile.toPath(), destinationFile.toPath());
            System.out.println("File berhasil disalin ke: " + destinationFile.getPath());
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Gagal menyalin file.");
        }
    }

    public void handleSubmitAssignment() {
        // Memilih tugas yang dipilih
        String selectedTaskName = taskChoiceBox.getValue();
        Task selectedTask = null;
        for (Task task : tasks) {
            if (task.getTaskName().equals(selectedTaskName)) {
                selectedTask = task;
                break;
            }
        }

        if (selectedTask == null || selectedFile == null) {
            // Tampilkan pesan kesalahan jika tugas atau file tidak dipilih
            System.out.println("Pilih tugas dan file sebelum mengumpulkan assignment.");
            return;
        }

        String assignmentFileName = selectedFile.getName();

        // Simpan assignment dan perbarui status task
        System.out.println(selectedTask.getDueDate());
        addAssignment(
                employeeId,
                selectedTask.getId(),
                assignmentFileName,
                selectedTask.getDueDate()
        );
    }

    public static void addAssignment(String employeeId, String taskId, String assignmentFile, String dueDate) {
        try {
            // Membaca data dari assignments.json
            String assignmentContent = new String(Files.readAllBytes(Paths.get("data/assignments.json")));
            JSONArray assignmentArray = assignmentContent.isEmpty() ? new JSONArray() : new JSONArray(assignmentContent);

            // Membuat data assignment baru
            JSONObject newAssignment = new JSONObject();
            newAssignment.put("id", UUID.randomUUID().toString());
            newAssignment.put("employeeId", employeeId);
            newAssignment.put("taskId", taskId);
            newAssignment.put("assignmentFile", assignmentFile);
            newAssignment.put("assignDate", LocalDate.now().toString());

            // Menambahkan assignment baru ke array
            assignmentArray.put(newAssignment);

            // Menyimpan kembali ke file
            FileWriter fileWriter = new FileWriter("data/assignments.json");
            fileWriter.write(assignmentArray.toString(4));
            fileWriter.close();

            // Perbarui status tugas
            updateTaskStatus(taskId, dueDate);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void updateTaskStatus(String taskId, String dueDate) {
        try {
            String taskContent = new String(Files.readAllBytes(Paths.get("data/tasks.json")));
            JSONArray taskArray = new JSONArray(taskContent);

            for (int i = 0; i < taskArray.length(); i++) {
                JSONObject task = taskArray.getJSONObject(i);

                if (task.getString("id").equals(taskId)) {
                    LocalDate due = LocalDate.parse(dueDate);
                    LocalDate currentDate = LocalDate.now();

                    // Update status
                    if (currentDate.isAfter(due)) {
                        task.put("status", "terlambat");
                    } else {
                        task.put("status", "selesai");
                    }

                    // Menyimpan perubahan ke tasks.json
                    FileWriter fileWriter = new FileWriter("data/tasks.json");
                    fileWriter.write(taskArray.toString(4));
                    fileWriter.close();
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
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

    public void kembaliButton(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("/com/example/bookflight/Staff.fxml"));
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
