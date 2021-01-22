package pl.edu.pw.gis.mykpyk.domain;

import io.reactivex.annotations.NonNull;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "Users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    public User(@NotNull String login, @NotNull String password) {
        this.login = login;
        this.password = password;
    }

    @NotNull
    @Column(name = "login", nullable = false)
    private String login;

    @NotNull
    @Column(name = "password", nullable = false)
    private String password;

    public User() {

    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
