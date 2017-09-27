package fr.wcs.viaferrata;

/**
 * Created by wilder on 26/09/17.
 */

public class ViaFerrataModel {

    private float mLatitude;
    private float mLongitude;
    private String mNom;
    private String mVille;
    private int mDepartement;
    private String mDifficulte;

    public ViaFerrataModel(float mLatitude, float mLongitude, String mNom, String mVille, int mDepartement, String mDifficulte) {
        this.mLatitude = mLatitude;
        this.mLongitude = mLongitude;
        this.mNom = mNom;
        this.mVille = mVille;
        this.mDepartement = mDepartement;
        this.mDifficulte = mDifficulte;
    }

    public float getmLatitude() {
        return mLatitude;
    }

    public void setmLatitude(float mLatitude) {
        this.mLatitude = mLatitude;
    }

    public float getmLongitude() {
        return mLongitude;
    }

    public void setmLongitude(float mLongitude) {
        this.mLongitude = mLongitude;
    }

    public String getmNom() {
        return mNom;
    }

    public void setmNom(String mNom) {
        this.mNom = mNom;
    }

    public String getmVille() {
        return mVille;
    }

    public void setmVille(String mVille) {
        this.mVille = mVille;
    }

    public int getmDepartement() {
        return mDepartement;
    }

    public void setmDepartement(int mDepartement) {
        this.mDepartement = mDepartement;
    }

    public String getmDifficulte() {
        return mDifficulte;
    }

    public void setmDifficulte(String mDifficulte) {
        this.mDifficulte = mDifficulte;
    }
}
