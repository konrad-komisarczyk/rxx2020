package pl.edu.pw.gis.mykpyk.controllers;

public class EnemyToDisplay {
    private String name;
    private Integer enemyTypeId;
    private Long id;
    private Double lon;
    private Double lat;
    private String image;

    public EnemyToDisplay(Long id, String name, Integer enemyTypeId, Double lon, Double lat, String image) {
        this.name = name;
        this.enemyTypeId = enemyTypeId;
        this.id = id;
        this.lon = lon;
        this.lat = lat;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public Integer getEnemyTypeId() {
        return enemyTypeId;
    }

    public Long getId() {
        return id;
    }

    public Double getLon() {
        return lon;
    }

    public Double getLat() {
        return lat;
    }

    public String getImage() {
        return image;
    }
}
