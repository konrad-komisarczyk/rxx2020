package pl.edu.pw.gis.mykpyk.domain;

import javax.persistence.*;
import javax.validation.constraints.NotNull;


@Entity
@Table(name = "Items")
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Column(name = "type", nullable = false)
    private String type; // weapon, armor, consumable, neutral

    @NotNull
    @Column(name = "damage", nullable = true)
    private Integer damage;

    @NotNull
    @Column(name = "protection", nullable = true)
    private Integer protection;

    @NotNull
    @Column(name = "healing", nullable = true)
    private Integer healing;

    @NotNull
    @Column(name = "description", nullable = false)
    private String description;

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getDamage() {
        return damage;
    }

    public void setDamage(Integer damage) {
        this.damage = damage;
    }

    public Integer getProtection() {
        return protection;
    }

    public void setProtection(Integer protection) {
        this.protection = protection;
    }

    public Integer getHealing() {
        return healing;
    }

    public void setHealing(Integer healing) {
        this.healing = healing;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
