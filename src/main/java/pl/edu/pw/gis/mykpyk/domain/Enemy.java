package pl.edu.pw.gis.mykpyk.domain;

import javax.persistence.*;
import javax.validation.constraints.NotNull;


@Entity
@Table(name = "Enemies")
public class Enemy {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "enemyTypeId", nullable = false)
    private Integer enemyTypeId;

    @NotNull
    @Column(name = "lng", nullable = false)
    private Double lng;

    @NotNull
    @Column(name = "lat", nullable = false)
    private Double lat;

    public Enemy(@NotNull Integer enemyTypeId, @NotNull Double lng, @NotNull Double lat) {
        this.enemyTypeId = enemyTypeId;
        this.lng = lng;
        this.lat = lat;
    }

    public Enemy() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getLng() {
        return lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Integer getEnemyTypeId() {
        return enemyTypeId;
    }

    public void setEnemyTypeId(Integer enemyTypeId) {
        this.enemyTypeId = enemyTypeId;
    }

}
