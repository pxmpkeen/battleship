package entities;

import enums.ItemType;
import enums.SubType;

import java.util.UUID;

public abstract class Item {
    UUID id;
    ItemType type;
    SubType subType;
    boolean hit;
    boolean checked;

    public void setType(ItemType type) {
        this.type = type;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked() {
        this.checked = true;
    }

    public void setSubType(SubType subType) {
        this.subType = subType;
    }

    public SubType getSubType() {
        return subType;
    }

    public ItemType getType() {
        return type;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public static UUID createId() {
        return UUID.randomUUID();
    }

    public void assignId() {
        this.id = UUID.randomUUID();
    }

    public boolean isHit() {
        return hit;
    }

    public void hit() {
        this.hit = !this.hit;
    }
}
