package fr.wcs.viaferrata;

/**
 * Created by wilder on 26/09/17.
 */

public class ViaFerrataModel {

    private String nom;
    private String ville;
    private int dptNb;
    private String dptNom;
    private String region;
    private int difficulte;
    private double latitude;
    private double longitude;
    private String description;
    private String altitudeDepart;
    private String altitudeArrivee;
    private String denivele;
    private String longueur;
    private String prix;
    private String nbPasserelle;
    private String nbPontSinge;
    private String nbEchelleFilet;
    private String nbTyrolienne;
    private String info;
    private String horaireApproche;
    private String horaireDuree;
    private String horaireRetour;
    private String infoAcces;

    public ViaFerrataModel() {
    }

    public ViaFerrataModel(String nom, String ville, int dptNb, String dptNom, String region, int difficulte, double latitude, double longitude, String description, String altitudeDepart, String altitudeArrivee, String denivele, String longueur, String prix, String nbPasserelle, String nbPontSinge, String nbEchelleFilet, String nbTyrolienne, String info, String horaireApproche, String horaireDuree, String horaireRetour, String infoAcces) {
        this.nom = nom;
        this.ville = ville;
        this.dptNb = dptNb;
        this.dptNom = dptNom;
        this.region = region;
        this.difficulte = difficulte;
        this.latitude = latitude;
        this.longitude = longitude;
        this.description = description;
        this.altitudeDepart = altitudeDepart;
        this.altitudeArrivee = altitudeArrivee;
        this.denivele = denivele;
        this.longueur = longueur;
        this.prix = prix;
        this.nbPasserelle = nbPasserelle;
        this.nbPontSinge = nbPontSinge;
        this.nbEchelleFilet = nbEchelleFilet;
        this.nbTyrolienne = nbTyrolienne;
        this.info = info;
        this.horaireApproche = horaireApproche;
        this.horaireDuree = horaireDuree;
        this.horaireRetour = horaireRetour;
        this.infoAcces = infoAcces;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getVille() {
        return ville;
    }

    public void setVille(String ville) {
        this.ville = ville;
    }

    public int getDptNb() {
        return dptNb;
    }

    public void setDptNb(int dptNb) {
        this.dptNb = dptNb;
    }

    public String getDptNom() {
        return dptNom;
    }

    public void setDptNom(String dptNom) {
        this.dptNom = dptNom;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public int getDifficulte() {
        return difficulte;
    }

    public void setDifficulte(int difficulte) {
        this.difficulte = difficulte;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAltitudeDepart() {
        return altitudeDepart;
    }

    public void setAltitudeDepart(String altitudeDepart) {
        this.altitudeDepart = altitudeDepart;
    }

    public String getAltitudeArrivee() {
        return altitudeArrivee;
    }

    public void setAltitudeArrivee(String altitudeArrivee) {
        this.altitudeArrivee = altitudeArrivee;
    }

    public String getDenivele() {
        return denivele;
    }

    public void setDenivele(String denivele) {
        this.denivele = denivele;
    }

    public String getLongueur() {
        return longueur;
    }

    public void setLongueur(String longueur) {
        this.longueur = longueur;
    }

    public String getPrix() {
        return prix;
    }

    public void setPrix(String prix) {
        this.prix = prix;
    }

    public String getNbPasserelle() {
        return nbPasserelle;
    }

    public void setNbPasserelle(String nbPasserelle) {
        this.nbPasserelle = nbPasserelle;
    }

    public String getNbPontSinge() {
        return nbPontSinge;
    }

    public void setNbPontSinge(String nbPontSinge) {
        this.nbPontSinge = nbPontSinge;
    }

    public String getNbEchelleFilet() {
        return nbEchelleFilet;
    }

    public void setNbEchelleFilet(String nbEchelleFilet) {
        this.nbEchelleFilet = nbEchelleFilet;
    }

    public String getNbTyrolienne() {
        return nbTyrolienne;
    }

    public void setNbTyrolienne(String nbTyrolienne) {
        this.nbTyrolienne = nbTyrolienne;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getHoraireApproche() {
        return horaireApproche;
    }

    public void setHoraireApproche(String horaireApproche) {
        this.horaireApproche = horaireApproche;
    }

    public String getHoraireDuree() {
        return horaireDuree;
    }

    public void setHoraireDuree(String horaireDuree) {
        this.horaireDuree = horaireDuree;
    }

    public String getHoraireRetour() {
        return horaireRetour;
    }

    public void setHoraireRetour(String horaireRetour) {
        this.horaireRetour = horaireRetour;
    }

    public String getInfoAcces() {
        return infoAcces;
    }

    public void setInfoAcces(String infoAcces) {
        this.infoAcces = infoAcces;
    }
}