package com.example.bookflight.Controller;

import com.example.bookflight.Model.Staff;
import com.example.bookflight.services.Auth;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.*;
import java.util.UUID;

public class AddStaffController {
    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    private TextField nameTextField, usernameTextField;
    @FXML
    private PasswordField passwordTextField;
    @FXML
    private Label nameErrorMessage, usernameErrorMessage, passwordErrorMessage;

    public void addStaff(ActionEvent event) throws IOException {
        String id = UUID.randomUUID().toString();
        String name = nameTextField.getText();
        String username = usernameTextField.getText();
        String password = passwordTextField.getText();
        String role = "staff";

        // Validation
        if(name.isEmpty()) {
            nameErrorMessage.setText("Masukkan nama staff");
            return;
        }
        if(username.isEmpty()) {
            usernameErrorMessage.setText("Masukkan username staff");
            return;
        }
        if(password.isEmpty()) {
            passwordErrorMessage.setText("Masukkan password staff");
            return;
        }

        // Buat objek Staff baru
        Staff newStaff = new Staff(id, name, username, password, role);

        // Simpan staff ke file JSON
        newStaff.save(newStaff);

        root = FXMLLoader.load(getClass().getResource("/com/example/bookflight/StaffList.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void kembaliButton(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("/com/example/bookflight/StaffList.fxml"));
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
