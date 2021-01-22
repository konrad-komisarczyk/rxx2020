package pl.edu.pw.gis.mykpyk.domain;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "Heroes")
public class Hero {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "userId", nullable = false)
    private Long userId;

    @NotNull
    @Column(name = "health", nullable = false)
    private Integer health;

    @NotNull
    @Column(name = "maxHealth", nullable = false)
    private Integer maxHealth;

    @NotNull
    @Column(name = "strength", nullable = false)
    private Integer strength;

    @NotNull
    @Column(name = "defense", nullable = false)
    private Integer defense;

    @NotNull
    @Column(name = "speed", nullable = false)
    private Integer speed;

    @NotNull
    @Column(name = "avatarLink", nullable = false)
    private String avatarLink;

    @NotNull
    @Column(name = "exp", nullable = false)
    private Integer exp;

    @NotNull
    @Column(name = "level", nullable = false)
    private Integer level;

    @NotNull
    @Column(name = "fieldOfView", nullable = false)
    private Integer fieldOfView; // in meters - how big area to be see on the map (fieldOfView to every direction from the player)

    @NotNull
    @Column(name = "talentPoints", nullable = false)
    private Integer talentPoints;

    @NotNull
    @Column(name = "lastLon", nullable = false)
    private Double lastLon; // in meters - how big area to be see on the map (fieldOfView to every direction from the player)

    @NotNull
    @Column(name = "lastLat", nullable = false)
    private Double lastLat;

    public Hero(Long userId) { // new player, new game
        this.userId = userId;
        exp = 0;
        level = 1;
        avatarLink = "TODO"; //TODO correct
        health = 100;
        maxHealth = 100;
        strength = 20;
        speed = 5;
        defense = 0;
        fieldOfView = 5;
        talentPoints = 5;
        lastLon = 0.;
        lastLat = 0.;
    }

    public Hero() {
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getHealth() {
        return health;
    }

    public void setHealth(Integer health) {
        this.health = health;
    }

    public Integer getMaxHealth() {
        return maxHealth;
    }

    public void setMaxHealth(Integer maxHealth) {
        this.maxHealth = maxHealth;
    }

    public Integer getStrength() {
        return strength;
    }

    public void setStrength(Integer strength) {
        this.strength = strength;
    }

    public Integer getDefense() {
        return defense;
    }

    public void setDefense(Integer defense) {
        this.defense = defense;
    }

    public Integer getSpeed() {
        return speed;
    }

    public void setSpeed(Integer speed) {
        this.speed = speed;
    }

    public String getAvatarLink() {
        return avatarLink;
    }

    public void setAvatarLink(String avatarLink) {
        this.avatarLink = avatarLink;
    }

    public Integer getExp() {
        return exp;
    }

    public void setExp(Integer exp) {
        this.exp = exp;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Integer getFieldOfView() {
        return fieldOfView;
    }

    public void setFieldOfView(Integer fieldOfView) {
        this.fieldOfView = fieldOfView;
    }

    public Integer getTalentPoints() {
        return talentPoints;
    }

    public void setTalentPoints(Integer talentPoints) {
        this.talentPoints = talentPoints;
    }

    public Double getLastLon() {
        return lastLon;
    }

    public void setLastLon(Double lastLon) {
        this.lastLon = lastLon;
    }

    public Double getLastLat() {
        return lastLat;
    }

    public void setLastLat(Double lastLat) {
        this.lastLat = lastLat;
    }

}
