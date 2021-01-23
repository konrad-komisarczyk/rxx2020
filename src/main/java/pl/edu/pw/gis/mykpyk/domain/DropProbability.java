package pl.edu.pw.gis.mykpyk.domain;


import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "DropProbability")
public class DropProbability {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "probability", nullable = false)
    private Double probability;

    @NotNull
    @Column(name = "enemyTypeId", nullable = false)
    private Integer enemyTypeId;

    @NotNull
    @Column(name = "itemId", nullable = false)
    private Integer itemId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getProbability() {
        return probability;
    }

    public void setProbability(Double probability) {
        this.probability = probability;
    }

    public Integer getEnemyTypeId() {
        return enemyTypeId;
    }

    public void setEnemyTypeId(Integer enemyTypeId) {
        this.enemyTypeId = enemyTypeId;
    }

    public Integer getItemId() {
        return itemId;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }
}
