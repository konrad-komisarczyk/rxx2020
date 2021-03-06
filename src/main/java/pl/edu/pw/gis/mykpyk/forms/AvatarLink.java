package pl.edu.pw.gis.mykpyk.forms;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class AvatarLink {
    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    private String link;
    private String login;

    @JsonCreator
    public AvatarLink(@JsonProperty("link") String link, @JsonProperty("login") String login) {
        this.link = link;
        this.login = login;
    }

}
