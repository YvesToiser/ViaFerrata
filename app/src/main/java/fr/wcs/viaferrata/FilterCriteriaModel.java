package fr.wcs.viaferrata;

/**
 * Created by wilder on 02/10/17.
 */

public class FilterCriteriaModel {

    private boolean filtreFavori;
    private boolean filtreFait;
    private boolean filtreGeo;
    private boolean filtreNiveau;
    private int [] newFilterDifficulties;
    private int [] filtreZones;

    public FilterCriteriaModel(boolean filtreFavori, boolean filtreFait, boolean filtreGeo, boolean filtreNiveau, int[] newFilterDifficulties, int[] filtreZones) {
        this.filtreFavori = filtreFavori;
        this.filtreFait = filtreFait;
        this.filtreGeo = filtreGeo;
        this.filtreNiveau = filtreNiveau;
        this.newFilterDifficulties = newFilterDifficulties;
        this.filtreZones = filtreZones;
    }

    public FilterCriteriaModel() {
    }

    public boolean isFiltreFavori() {
        return filtreFavori;
    }

    public void setFiltreFavori(boolean filtreFavori) {
        this.filtreFavori = filtreFavori;
    }

    public boolean isFiltreFait() {
        return filtreFait;
    }

    public void setFiltreFait(boolean filtreFait) {
        this.filtreFait = filtreFait;
    }

    public boolean isFiltreGeo() {
        return filtreGeo;
    }

    public void setFiltreGeo(boolean filtreGeo) {
        this.filtreGeo = filtreGeo;
    }

    public boolean isFiltreNiveau() {
        return filtreNiveau;
    }

    public void setFiltreNiveau(boolean filtreNiveau) {
        this.filtreNiveau = filtreNiveau;
    }

    public int[] getNewFilterDifficulties() {
        return newFilterDifficulties;
    }

    public void setNewFilterDifficulties(int[] newFilterDifficulties) {
        this.newFilterDifficulties = newFilterDifficulties;
    }

    public int[] getFiltreZones() {
        return filtreZones;
    }

    public void setFiltreZones(int[] filtreZones) {
        this.filtreZones = filtreZones;
    }
}
