package fr.wcs.viaferrata;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by wilder on 26/09/17.
 */

public class ViaFerrataModel implements Parcelable{

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

    public ViaFerrataModel(String nom, String ville, int dptNb, String dptNom, String region, int difficulte,
                           double latitude, double longitude, String description, String altitudeDepart,
                           String altitudeArrivee, String denivele, String longueur, String prix,
                           String nbPasserelle, String nbPontSinge, String nbEchelleFilet, String nbTyrolienne,
                           String info, String horaireApproche, String horaireDuree, String horaireRetour,
                           String infoAcces) {
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

    public static final Creator<ViaFerrataModel> CREATOR = new Creator<ViaFerrataModel>() {
        @Override
        public ViaFerrataModel createFromParcel(Parcel in) {
            return new ViaFerrataModel(in);
        }

        @Override
        public ViaFerrataModel[] newArray(int size) {
            return new ViaFerrataModel[size];
        }
    };

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

    public int getRegionNumber(){
        switch (region){
            case "Occitanie": return 6;
            case "Nouvelle-Aquitaine": return 5;
            case "Auvergne-Rhône-Alpes": return 0;
            case "Normandie": return 4;
            case "Bourgogne Franche Comté": return 1;
            case "Grand-Est": return 3;
            case "PACA": return 7;
            case "Corse": return 2;
            default :  return -1;

        }
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public int getDifficulte() {
        return difficulte;
    }

    public String getDifficulteInLetters() {
        switch (difficulte){
            case 1: return "F";
            case 2: return "PD";
            case 3: return "AD";
            case 4: return "D";
            case 5: return "TD";
            case 6: return "ED";
            default : return "?";
        }
    }

    public String getDifficulteNb() {
        switch (difficulte){
            case 1: return "A";
            case 2: return "B";
            case 3: return "C";
            case 4: return "D";
            case 5: return "E";
            case 6: return "F";
            default : return "Z";
        }
    }

    public String getDifficulteInWords() {
        switch (difficulte){
            case 1: return "Facile";
            case 2: return "Peu Difficile";
            case 3: return "Assez Difficile";
            case 4: return "Difficile";
            case 5: return "Très Difficile";
            case 6: return "Extrêmement Difficile";
            default : return "Inconnue";
        }
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
        if(nbPasserelle.equals("?") || nbPasserelle.isEmpty()){
            return "N.C";
        }
        return nbPasserelle;
    }

    public void setNbPasserelle(String nbPasserelle) {
        this.nbPasserelle = nbPasserelle;
    }

    public String getNbPontSinge() {
        if(nbPontSinge.equals("?") || nbPontSinge.isEmpty()){
            return "N.C";
        }
        return nbPontSinge;
    }

    public void setNbPontSinge(String nbPontSinge) {
        this.nbPontSinge = nbPontSinge;
    }

    public String getNbEchelleFilet() {
        if(nbEchelleFilet.equals("?") || nbEchelleFilet.isEmpty()){
            return "N.C";
        }
        return nbEchelleFilet;
    }

    public void setNbEchelleFilet(String nbEchelleFilet) {
        this.nbEchelleFilet = nbEchelleFilet;
    }

    public String getNbTyrolienne() {
        if(nbTyrolienne.equals("?") || nbTyrolienne.isEmpty()){
            return "N.C";
        }
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

    @Override
    public String toString() {
        return "ViaFerrataModel{" +
                "nom='" + nom + '\'' +
                ", ville='" + ville + '\'' +
                ", dptNb=" + dptNb +
                ", dptNom='" + dptNom + '\'' +
                ", region='" + region + '\'' +
                ", difficulte=" + difficulte +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", description='" + description + '\'' +
                ", altitudeDepart='" + altitudeDepart + '\'' +
                ", altitudeArrivee='" + altitudeArrivee + '\'' +
                ", denivele='" + denivele + '\'' +
                ", longueur='" + longueur + '\'' +
                ", prix='" + prix + '\'' +
                ", nbPasserelle='" + nbPasserelle + '\'' +
                ", nbPontSinge='" + nbPontSinge + '\'' +
                ", nbEchelleFilet='" + nbEchelleFilet + '\'' +
                ", nbTyrolienne='" + nbTyrolienne + '\'' +
                ", info='" + info + '\'' +
                ", horaireApproche='" + horaireApproche + '\'' +
                ", horaireDuree='" + horaireDuree + '\'' +
                ", horaireRetour='" + horaireRetour + '\'' +
                ", infoAcces='" + infoAcces + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(nom);
        parcel.writeString(ville);
        parcel.writeInt(dptNb);
        parcel.writeString(dptNom);
        parcel.writeString(region);
        parcel.writeInt(difficulte);
        parcel.writeDouble(latitude);
        parcel.writeDouble(longitude);
        parcel.writeString(description);
        parcel.writeString(altitudeDepart);
        parcel.writeString(altitudeArrivee);
        parcel.writeString(denivele);
        parcel.writeString(longueur);
        parcel.writeString(prix);
        parcel.writeString(nbPasserelle);
        parcel.writeString(nbPontSinge);
        parcel.writeString(nbEchelleFilet);
        parcel.writeString(nbTyrolienne);
        parcel.writeString(info);
        parcel.writeString(horaireApproche);
        parcel.writeString(horaireDuree);
        parcel.writeString(horaireRetour);
        parcel.writeString(infoAcces);
    }



    // Méthode servant à lire les données du parcelable
    private ViaFerrataModel(Parcel in) {
        nom  = in.readString();
        ville  = in.readString();
        dptNb  = in.readInt();
        dptNom = in.readString();
        region = in.readString();
        difficulte = in.readInt();
        latitude = in.readDouble();
        longitude = in.readDouble();
        description = in.readString();
        altitudeDepart = in.readString();
        altitudeArrivee = in.readString();
        denivele = in.readString();
        longueur = in.readString();
        prix = in.readString();
        nbPasserelle = in.readString();
        nbPontSinge = in.readString();
        nbEchelleFilet = in.readString();
        nbTyrolienne = in.readString();
        info = in.readString();
        horaireApproche = in.readString();
        horaireDuree = in.readString();
        horaireRetour = in.readString();
        infoAcces = in.readString();

    }
}

