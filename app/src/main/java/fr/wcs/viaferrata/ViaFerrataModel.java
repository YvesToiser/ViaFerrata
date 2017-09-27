package fr.wcs.viaferrata;

/**
 * Created by wilder on 26/09/17.
 */

public class ViaFerrataModel {

    private double latitude;
    private double longitude;
    private String nom;
    private String Ville;
    private int departement;
    //private String difficulte;
    private String departement_nom;
    private String description;

    public ViaFerrataModel() {
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getVille() {
        return Ville;
    }

    public void setVille(String ville) {
        Ville = ville;
    }

    public int getDepartement() {
        return departement;
    }

    public void setDepartement(int departement) {
        this.departement = departement;
    }

//    public String getDifficulte() {
//        return difficulte;
//    }
//
//    public void setDifficulte(String difficulte) {
//        this.difficulte = difficulte;
//    }

    public String getDepartement_nom() {
        return departement_nom;
    }

    public void setDepartement_nom(String departement_nom) {
        this.departement_nom = departement_nom;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
