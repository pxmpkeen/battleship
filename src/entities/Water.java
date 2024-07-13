package entities;

import enums.ItemType;
import enums.SubType;

public class Water extends Item {
    public Water() {
        hit = false;
        setType(ItemType.WATER);
        setSubType(SubType.NONE);
    }
    @Override
    public String toString() {
        String cls = "\033[0m";
        String bg = "\033[40m";

        if (isChecked() && !isHit()) {
            bg = "\033[42m";
        } else if (isChecked() && isHit()) {
            bg = "\033[41m";
        }

        return String.format("%s %c %s", bg, hit ? '~' : '-', cls);
    }
}
