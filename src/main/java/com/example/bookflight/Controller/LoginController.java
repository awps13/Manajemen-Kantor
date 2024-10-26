package com.example.bookflight.Controller;

import com.example.bookflight.Model.Manager;
import com.example.bookflight.services.Auth;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController {
    @FXML
    private TextField usernameTextField;
    @FXML
    private PasswordField passwordTextField;
    @FXML
    private Button loginButton;
    @FXML
    private Label errorLabel;

    private Stage stage;
    private Scene scene;
    private Parent root;

    public void login(ActionEvent event) throws IOException {
        Auth auth = new Auth();

        String username = this.usernameTextField.getText();
        String password = this.passwordTextField.getText();

        // Validation
        if(username.trim().isEmpty() || password.trim().isEmpty()) {
            errorLabel.setText("Masukkan data dengan benar");
            return;
        }

        if (!auth.login(username, password)) {
            errorLabel.setText("Username atau password anda salah");
            return;
        }

        // Redirect to main menu base on role
        if (Auth.getLoggedInUser() instanceof Manager) {
            root = FXMLLoader.load(getClass().getResource("/com/example/bookflight/Manager.fxml"));
            stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } else {
            root = FXMLLoader.load(getClass().getResource("/com/example/bookflight/Staff.fxml"));
            stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }
    }
}
