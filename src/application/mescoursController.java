package application;

import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import javafx.application.Platform;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class mescoursController {

	@FXML
	private TableColumn<CustomCourse, String> coursColumn;

	@FXML
	private TableColumn<CustomCourse, String> profColumn;

	@FXML
	private TableView<CustomCourse> table;

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
		coursColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getCours()));
		profColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getProf()));

		// Load initial data into the table
		loadCoursData();
	}

	private void loadCoursData() {
		try {
			// Clear existing items in the table
			table.getItems().clear();

			// Fetch prof (cours) and nom (prof) data from the database
			String query = "SELECT cours, prof FROM etudiant"; // Select the required columns
			PreparedStatement preparedStatement = connection.prepareStatement(query);
			ResultSet resultSet = preparedStatement.executeQuery();

			List<CustomCourse> coursesList = new ArrayList<>();

			while (resultSet.next()) {
				String cours = resultSet.getString("cours");
				String prof = resultSet.getString("prof");

				// Create a CustomCourse object
				CustomCourse course = new CustomCourse(cours, prof);

				// Add the CustomCourse object to the list
				coursesList.add(course);
			}

			// Add all CustomCourse objects to the table
			table.getItems().addAll(coursesList);

			// Close resources
			resultSet.close();
			preparedStatement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// Custom class to represent a row of data (prof, cours)
	public static class CustomCourse {
		private final SimpleStringProperty cours;
		private final SimpleStringProperty prof;

		public CustomCourse(String cours, String prof) {
			this.cours = new SimpleStringProperty(cours);
			this.prof = new SimpleStringProperty(prof);
		}

		public String getCours() {
			return cours.get();
		}

		public String getProf() {
			return prof.get();
		}
	}

	@FXML
	public void deconnecter(ActionEvent event) throws IOException {
		// Fermez la fenêtre principale
		Stage appStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		appStage.close();

		// Terminez l'application
		Platform.exit();
	}
}