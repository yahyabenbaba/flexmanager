package application;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class FatiController {

    @FXML
    private Label nometudiant;

    @FXML
    private Label prenometudiant;

    @FXML
    private Label filiereetudiant;

    @FXML
    private Label usernameetudiant;

    @FXML
    private Label messageLabel;

    private String username;

    public void setEtudiantData(String firstname, String lastname, String username, String filiere) {
        this.username = username;
        nometudiant.setText(firstname);
        prenometudiant.setText(lastname);
        usernameetudiant.setText(username);
        filiereetudiant.setText(filiere);
    }



    @FXML
    public void loadData(ActionEvent event) {
        String dbUrl = "jdbc:mysql://localhost:3306/flexmanager";
        String dbUser = "root";
        String dbPassword = "";

        String query = "SELECT firstname, lastname, filiere FROM signup WHERE username = ?";

        try (Connection con = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
             PreparedStatement statement = con.prepareStatement(query)) {

            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                String firstname = resultSet.getString("firstname");
                String lastname = resultSet.getString("lastname");
                String filiere = resultSet.getString("filiere");

                nometudiant.setText(firstname);
                prenometudiant.setText(lastname);
                filiereetudiant.setText(filiere);
            } else {
                messageLabel.setText("User not found!");
                messageLabel.setTextFill(javafx.scene.paint.Color.RED);
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            messageLabel.setText("Erreur de connexion à la base de données !");
            messageLabel.setTextFill(javafx.scene.paint.Color.RED);
        }
    }

    @FXML
    public void handleLogout(ActionEvent event) {
        Stage stage = (Stage) nometudiant.getScene().getWindow();
        stage.close();
    }

    @FXML
    public void voirCoursClicked(ActionEvent event) throws IOException {
        Parent pageEtudiant = FXMLLoader.load(getClass().getResource("mescours.fxml"));
        Scene pageEtudiantScene = new Scene(pageEtudiant);
        Stage appStage = (Stage)((Node)event.getSource()).getScene().getWindow();
        appStage.setScene(pageEtudiantScene);
        appStage.show();
    }

}
