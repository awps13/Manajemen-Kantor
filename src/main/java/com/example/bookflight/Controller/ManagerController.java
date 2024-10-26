package com.example.bookflight.Controller;

import com.example.bookflight.Model.Employee;
import com.example.bookflight.services.Auth;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

public class ManagerController {
    @FXML
    private Label myLabel;

    private Stage stage;
    private Scene scene;
    private Parent root;

    public void initialize() {
        Employee currentUser = Auth.getLoggedInUser();

        myLabel.setText("Welcome: " + currentUser.getName());
    }

    public void taskPage(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("/com/example/bookflight/TaskList.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void staffPage(ActionEvent event) throws IOException {
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
