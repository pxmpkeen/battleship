package entities;

import enums.ItemType;
import enums.SubType;

public class Distance extends Item {
    public Distance() {
        hit = false;
        assignId();
        setType(ItemType.DISTANCE);
        setSubType(SubType.NONE);
    }

    @Override
    public String toString() {
        String cls = "\033[0m";
        String bg = "\033[46m";
        String color = "\033[0;30m";

        if (this.isHit()) {
            bg = "\033[45m";
        }

        return String.format("%s%s %c %s", color, bg, hit ? '+' : '*', cls);
    }
}
