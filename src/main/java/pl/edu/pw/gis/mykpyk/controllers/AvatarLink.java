package pl.edu.pw.gis.mykpyk.controllers;

public class AvatarLink {
    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    private String link;

    public AvatarLink(String link) {
        this.link = link;
    }
}
