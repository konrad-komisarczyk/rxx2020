package pl.edu.pw.gis.mykpyk.displays;

import pl.edu.pw.gis.mykpyk.domain.BackpackSlot;
import pl.edu.pw.gis.mykpyk.domain.ItemRepository;

import javax.inject.Inject;

public class BackpackSlotToDisplay {

    private Long id;
    private Integer itemId;
    private Integer position;
    private String image;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getItemId() {
        return itemId;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public BackpackSlotToDisplay(Long id, Integer itemId, Integer position, String image) {
        this.id = id;
        this.itemId = itemId;
        this.position = position;
        this.image = image;
    }
}
