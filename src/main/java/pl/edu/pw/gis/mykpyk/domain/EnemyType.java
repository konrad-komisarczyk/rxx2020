package pl.edu.pw.gis.mykpyk.domain;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "EnemyTypes")
public class EnemyType {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Column(name = "minUserLvl", nullable = false)
    private Integer minUserLvl;

    @NotNull
    @Column(name = "exp", nullable = false)
    private Integer exp;

    @NotNull
    @Column(name = "details", nullable = false)
    private String details;

    @NotNull
    @Column(name = "health", nullable = false)
    private Integer health;

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
    @Column(name = "spawnKey", nullable = true)
    private String spawnKey;

    @NotNull
    @Column(name = "spawnValue", nullable = true)
    private String spawnValue;

    @NotNull
    @Column(name = "image", nullable = false)
    private String image;

    @NotNull
    @Column(name = "spawnIntensity", nullable = false)
    private Double spawnIntensity;



    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getMinUserLvl() {
        return minUserLvl;
    }

    public void setMinUserLvl(Integer minUserLvl) {
        this.minUserLvl = minUserLvl;
    }

    public Integer getExp() {
        return exp;
    }

    public void setExp(Integer exp) {
        this.exp = exp;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public Integer getHealth() {
        return health;
    }

    public void setHealth(Integer health) {
        this.health = health;
    }

    public Integer getStrength() {
        return strength;
    }

    public void setStrength(Integer strength) {
        this.strength = strength;
    }

    public String getSpawnKey() {
        return spawnKey;
    }

    public void setSpawnKey(String spawnKey) {
        this.spawnKey = spawnKey;
    }

    public String getSpawnValue() {
        return spawnValue;
    }

    public void setSpawnValue(String spawnValue) {
        this.spawnValue = spawnValue;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Double getSpawnIntensity() {
        return spawnIntensity;
    }

    public void setSpawnIntensity(Double spawnIntensity) {
        this.spawnIntensity = spawnIntensity;
    }
}
