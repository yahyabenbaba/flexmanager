package application;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class SampleController {

    @FXML
    private TextField usertext;

    @FXML
    private PasswordField passtext;

    @FXML
    private Label messageLabel;

    @FXML
    public void connectToDatabase(ActionEvent event) {
        String username = usertext.getText();
        String password = passtext.getText();

        if (username.isEmpty() || password.isEmpty()) {
            messageLabel.setText("Please enter both username and password.");
            messageLabel.setTextFill(javafx.scene.paint.Color.RED);
            return;
        }

        String dbUrl = "jdbc:mysql://localhost:5123/flexmanager";
        String dbUser = "root";
        String dbPassword = "";

        String query = "SELECT role FROM utilisateurs WHERE username = ? AND password = ?";

        try (Connection con = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
             PreparedStatement statement = con.prepareStatement(query)) {

            statement.setString(1, username);
            statement.setString(2, password);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                String role = resultSet.getString("role");

                if ("etudiant".equalsIgnoreCase(role)) {
                    loadStudentWindow(event, username);
                } else if ("professeur".equalsIgnoreCase(role)) {
                    // Load MainScene and pass the username
                    loadMainScene(event, username);
                } else {
                    messageLabel.setText("Role not recognized!");
                    messageLabel.setTextFill(javafx.scene.paint.Color.RED);
                }
            } else {
                messageLabel.setText("Connection failed !");
                messageLabel.setTextFill(javafx.scene.paint.Color.RED);
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            messageLabel.setText("Erreur de connexion à la base de données !");
            messageLabel.setTextFill(javafx.scene.paint.Color.RED);
        }
    }

    // Method to load MainScene and pass the username
    private void loadMainScene(ActionEvent event, String username) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("MainScene.fxml"));
            Parent root = loader.load();

            // Get controller of MainScene
            MainSceneController mainController = loader.getController();

            // Set the username
            mainController.setUsername(username);

            Stage stage = new Stage();
            stage.setTitle("Professeur");
            stage.setScene(new Scene(root));
            stage.show();

            Stage currentStage = (Stage) usertext.getScene().getWindow();
            currentStage.close();
        } catch (IOException e) {
            e.printStackTrace();
            messageLabel.setText("Failed to load the new window!");
            messageLabel.setTextFill(javafx.scene.paint.Color.RED);
        }
    }

    // Placeholder method for loading student window
    private void loadStudentWindow(ActionEvent event, String username) {
        // You need to implement this method to load the student window
        // For now, let's just call loadMainScene as a placeholder
        loadMainScene(event, username);
    }

    @FXML
    public void openSignUpForm(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("signup.fxml"));
            Stage signupStage = new Stage();
            Scene scene = new Scene(root, 670, 460);
            scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
            signupStage.setScene(scene);
            signupStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
