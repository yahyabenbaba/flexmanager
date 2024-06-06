package application;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

public class signupController {

    @FXML
    private TextField firstnameTextField;

    @FXML
    private TextField lastnameTextField;

    @FXML
    private TextField usernameTextField;

    @FXML
    private TextField filiereTextField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label messageLabel;

    @FXML
    private ChoiceBox<String> roleChoiceBox;

    @FXML
    private ProgressBar progressBar;

    @FXML
    private void initialize() {
        roleChoiceBox.getItems().addAll("etudiant", "professeur");

        // Add listeners to update the progress bar
        addFieldListeners();
    }

    private void addFieldListeners() {
        ChangeListener<String> listener = new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                updateProgressBar();
            }
        };

        firstnameTextField.textProperty().addListener(listener);
        lastnameTextField.textProperty().addListener(listener);
        usernameTextField.textProperty().addListener(listener);
        filiereTextField.textProperty().addListener(listener);
        passwordField.textProperty().addListener(listener);
        roleChoiceBox.valueProperty().addListener((obs, oldVal, newVal) -> updateProgressBar());
    }

    private void updateProgressBar() {
        int filledFields = 0;
        if (!firstnameTextField.getText().isEmpty()) filledFields++;
        if (!lastnameTextField.getText().isEmpty()) filledFields++;
        if (!usernameTextField.getText().isEmpty()) filledFields++;
        if (!filiereTextField.getText().isEmpty()) filledFields++;
        if (!passwordField.getText().isEmpty()) filledFields++;
        if (roleChoiceBox.getValue() != null) filledFields++;

        double progress = (double) filledFields / 6;
        progressBar.setProgress(progress);

        // Optional: Change color of progress bar based on progress
        if (progress == 1.0) {
            progressBar.setStyle("-fx-accent: green;");
        } else {
            progressBar.setStyle("-fx-accent: #cb0422;");
        }
    }

    @FXML
    private void signUp(ActionEvent event) {
        if (validateFields()) {
            if (saveUserToDatabase()) {
                messageLabel.setText("Inscription réussie!");
                messageLabel.setTextFill(javafx.scene.paint.Color.GREEN);
            }
        } else {
            messageLabel.setText("Veuillez remplir tous les champs");
            messageLabel.setTextFill(javafx.scene.paint.Color.RED);
        }
    }

    private boolean validateFields() {
        return !firstnameTextField.getText().isEmpty() &&
                !lastnameTextField.getText().isEmpty() &&
                !usernameTextField.getText().isEmpty() &&
                !filiereTextField.getText().isEmpty() &&
                !passwordField.getText().isEmpty() &&
                roleChoiceBox.getValue() != null;
    }

    private boolean saveUserToDatabase() {
        String firstname = firstnameTextField.getText();
        String lastname = lastnameTextField.getText();
        String username = usernameTextField.getText();
        String filiere = filiereTextField.getText();
        String password = passwordField.getText();
        String role = roleChoiceBox.getValue();

        String url = "jdbc:mysql://localhost:3306/flexmanager";
        String user = "root";
        String dbPassword = "";

        String querySignup = "INSERT INTO signup (firstname, lastname, username, filiere, password, role) VALUES (?, ?, ?, ?, ?, ?)";
        String queryUtilisateurs = "INSERT INTO utilisateurs (username, password, role) VALUES (?, ?, ?)";

        try (Connection connection = DriverManager.getConnection(url, user, dbPassword)) {
            try (PreparedStatement statementSignup = connection.prepareStatement(querySignup)) {
                statementSignup.setString(1, firstname);
                statementSignup.setString(2, lastname);
                statementSignup.setString(3, username);
                statementSignup.setString(4, filiere);
                statementSignup.setString(5, password);
                statementSignup.setString(6, role);

                int rowsInsertedSignup = statementSignup.executeUpdate();
                if (rowsInsertedSignup > 0) {
                    try (PreparedStatement statementUtilisateurs = connection.prepareStatement(queryUtilisateurs)) {
                        statementUtilisateurs.setString(1, username);
                        statementUtilisateurs.setString(2, password);
                        statementUtilisateurs.setString(3, role);

                        int rowsInsertedUtilisateurs = statementUtilisateurs.executeUpdate();
                        return rowsInsertedUtilisateurs > 0;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            messageLabel.setText("Erreur de base de données: " + e.getMessage());
            messageLabel.setTextFill(javafx.scene.paint.Color.RED);
        }
        return false;
    }

    @FXML
    private void handleReturn(ActionEvent event) {
        try {
            // Charger le fichier FXML de la fenêtre "Sample"
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Sample.fxml"));
            Parent root = loader.load();

            // Obtenir la scène actuelle
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Sample");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
