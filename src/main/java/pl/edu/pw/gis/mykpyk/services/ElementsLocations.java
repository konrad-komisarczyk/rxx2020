package pl.edu.pw.gis.mykpyk.services;

import java.util.ArrayList;

public class ElementsLocations {
    private ArrayList<Double> lats;
    private ArrayList<Double> lngs;

    public ElementsLocations(ArrayList<Double> lats, ArrayList<Double> lngs) {
        this.lats = lats;
        this.lngs = lngs;
    }

    public ArrayList<Double> getLats() {
        return lats;
    }

    public void setLats(ArrayList<Double> lats) {
        this.lats = lats;
    }

    public ArrayList<Double> getLngs() {
        return lngs;
    }

    public void setLngs(ArrayList<Double> lngs) {
        this.lngs = lngs;
    }

    @Override
    public String toString() {
        return "lats: " + lats +
                "\nlngs: " + lngs;
    }
}
