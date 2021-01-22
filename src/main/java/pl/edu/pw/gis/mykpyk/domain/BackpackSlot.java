package pl.edu.pw.gis.mykpyk.domain;

import javax.persistence.*;

@Entity
@Table(name = "BackpackSlots")
public class BackpackSlot {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    private Hero hero;

    @ManyToOne
    private Item item;

}
