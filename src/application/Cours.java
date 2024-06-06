package application;

import javafx.beans.property.*;

public class Cours {
    private final IntegerProperty id;
    private final StringProperty cours; // Change lastName to cours
    private final StringProperty prof; // Change firstName to prof
    private final StringProperty filiere;
    private final StringProperty detail; // Change phone to detail

    public Cours(int id, String cours, String prof, String filiere, String detail) {
        this.id = new SimpleIntegerProperty(id);
        this.cours = new SimpleStringProperty(cours);
        this.prof = new SimpleStringProperty(prof);
        this.filiere = new SimpleStringProperty(filiere);
        this.detail = new SimpleStringProperty(detail);
    }

    public IntegerProperty idProperty() {
        return id;
    }

    public StringProperty coursProperty() {
        return cours;
    }

    public StringProperty profProperty() {
        return prof;
    }

    public StringProperty filiereProperty() {
        return filiere;
    }

    public StringProperty detailProperty() {
        return detail;
    }

    public int getId() {
        return id.get();
    }

    public String getCours() {
        return cours.get();
    }

    public String getProf() {
        return prof.get();
    }

    public String getFiliere() {
        return filiere.get();
    }

    public String getDetail() {
        return detail.get();
    }
}