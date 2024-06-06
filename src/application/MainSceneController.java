package application;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MainSceneController {

    @FXML
    private Button btnAdd;

    @FXML
    private Button btnDelete;

    @FXML
    private Button btnUpdate;

    @FXML
    private TableColumn<Cours, String> coursColumn;

    @FXML
    private TableColumn<Cours, String> profColumn;

    @FXML
    private TableColumn<Cours, String> filiereColumn;

    @FXML
    private TableColumn<Cours, String> detailsColumn;

    @FXML
    private TableView<Cours> table;

    @FXML
    private TextField txtFiliere;

    @FXML
    private Label usernameLabel;

    @FXML
    private TextField txtCours;

    @FXML
    private TextField txtDetails;

    private DatabaseConnection databaseConnection;
    private Connection connection;

    @FXML
    public void initialize() {
        databaseConnection = new DatabaseConnection();
        try {
            connection = databaseConnection.getConnection();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        // Initialize table columns
        coursColumn.setCellValueFactory(new PropertyValueFactory<>("cours"));
        profColumn.setCellValueFactory(new PropertyValueFactory<>("prof"));
        filiereColumn.setCellValueFactory(new PropertyValueFactory<>("filiere"));
        detailsColumn.setCellValueFactory(new PropertyValueFactory<>("detail"));

        // Load initial data into the table
        loadCoursData();

        // Add listener for row selection
        table.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Cours>() {
            @Override
            public void changed(ObservableValue<? extends Cours> observable, Cours oldValue, Cours newValue) {
                if (newValue != null) {
                    txtCours.setText(newValue.getCours());
                    txtFiliere.setText(newValue.getFiliere());
                    txtDetails.setText(newValue.getDetail());
                }
            }
        });
    }

    @FXML
    public void add(ActionEvent event) {
        String cours = txtCours.getText();
        String prof = usernameLabel.getText();
        String filiere = txtFiliere.getText();
        String detail = txtDetails.getText();

        try {
            String query = "INSERT INTO etudiant (prof, cours, filiere, detail) VALUES (?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, prof);
            preparedStatement.setString(2, cours);
            preparedStatement.setString(3, filiere);
            preparedStatement.setString(4, detail);
            preparedStatement.executeUpdate();

            // Refresh the table after adding
            loadCoursData();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @FXML
    public void update(ActionEvent event) {
        // Get selected course from the table
        Cours selectedCourse = table.getSelectionModel().getSelectedItem();

        if (selectedCourse != null) {
            String cours = txtCours.getText();
            String prof = usernameLabel.getText();
            String filiere = txtFiliere.getText();
            String detail = txtDetails.getText();

            try {
                String query = "UPDATE etudiant SET prof=?, cours=?, filiere=?, detail=? WHERE cours=? AND prof=? AND filiere=? AND detail=?";
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                preparedStatement.setString(1, prof);
                preparedStatement.setString(2, cours);
                preparedStatement.setString(3, filiere);
                preparedStatement.setString(4, detail);
                preparedStatement.setString(5, selectedCourse.getCours());
                preparedStatement.setString(6, selectedCourse.getProf());
                preparedStatement.setString(7, selectedCourse.getFiliere());
                preparedStatement.setString(8, selectedCourse.getDetail());
                preparedStatement.executeUpdate();

                // Refresh the table after updating
                loadCoursData();

                // Clear input fields after updating
                txtCours.clear();
                txtFiliere.clear();
                txtDetails.clear();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }

    @FXML
    public void delete(ActionEvent event) {
        // Get selected course from the table
        Cours selectedCourse = table.getSelectionModel().getSelectedItem();

        if (selectedCourse != null) {
            try {
                String query = "DELETE FROM etudiant WHERE cours=? AND prof=? AND filiere=? AND detail=?";
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                preparedStatement.setString(1, selectedCourse.getCours());
                preparedStatement.setString(2, selectedCourse.getProf());
                preparedStatement.setString(3, selectedCourse.getFiliere());
                preparedStatement.setString(4, selectedCourse.getDetail());
                preparedStatement.executeUpdate();

                // Refresh the table after deleting
                loadCoursData();

                // Clear input fields after deleting (optional)
                txtCours.clear();
                txtFiliere.clear();
                txtDetails.clear();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }

    private void loadCoursData() {
        try {
            // Clear existing items in the table
            table.getItems().clear();

            // Fetch course data for the current professor from the database
            String query = "SELECT * FROM etudiant WHERE prof=?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, usernameLabel.getText());
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String cours = resultSet.getString("cours");
                String prof = resultSet.getString("prof");
                String filiere = resultSet.getString("filiere");
                String detail = resultSet.getString("detail");

                // Create Course object
                Cours course = new Cours(id, cours, prof, filiere, detail);

                // Add course to the table
                table.getItems().add(course);
            }

            // Close resources
            resultSet.close();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleLogout(MouseEvent event) throws IOException {
        // Load the login.fxml file
        Parent loginParent = FXMLLoader.load(getClass().getResource("Sample.fxml"));
        Scene loginScene = new Scene(loginParent);

        // Get the current stage
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        // Set the new scene
        stage.setScene(loginScene);
        stage.show();
    }

    public void setUsername(String username) {
        usernameLabel.setText(username);
    }
}
