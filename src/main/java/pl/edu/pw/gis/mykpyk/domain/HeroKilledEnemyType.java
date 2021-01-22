package pl.edu.pw.gis.mykpyk.domain;

import javax.persistence.*;

@Entity
@Table(name = "HeroKilledEnemyTypes")
public class HeroKilledEnemyType {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    private Hero hero;

    @ManyToOne
    private EnemyType enemyType;
}
