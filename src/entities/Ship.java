package entities;

import enums.ItemType;
import enums.SubType;

import java.util.UUID;

public class Ship extends Item {
    public Ship(SubType type) {
        assignId();
        setType(ItemType.SHIP);
        setSubType(type);
    }

    public Ship(SubType type, UUID id) {
        hit = false;
        setId(id);
        setType(ItemType.SHIP);
        setSubType(type);
    }

    @Override
    public String toString() {

        String cls = "\033[0m";
        String bg = "\033[47m";
        String color = "\033[0;30m";

        if (this.isHit()) {
            bg = "\033[43m";
        }

        return String.format("%s%s %c %s", color, bg, hit ? '#' : 'X', cls);
    }
}
