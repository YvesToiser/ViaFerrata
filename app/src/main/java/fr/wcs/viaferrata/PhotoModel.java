package fr.wcs.viaferrata;

/**
 * Created by wilderjm on 18/10/17.
 */

public class PhotoModel {
    private String viaName;
    private String photoUri;

    public PhotoModel() {
    }

    public PhotoModel(String viaName, String photoUri) {
        this.viaName = viaName;
        this.photoUri = photoUri;
    }

    public String getViaName() {
        return viaName;
    }

    public void setViaName(String viaName) {
        this.viaName = viaName;
    }

    public String getPhotoUri() {
        return photoUri;
    }

    public void setPhotoUri(String photoUri) {
        this.photoUri = photoUri;
    }
}
