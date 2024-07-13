package dtos;

import enums.SubType;
import org.jetbrains.annotations.NotNull;

public class SubTypeDTO {
    SubType type;
    static int maxLength = 5;
    int count, length;

    public SubTypeDTO(SubType type) {
        this.type = type;
        length = typeToLength(type);
        count = maxLength - length;
    }

    public SubType getType() {
        return type;
    }

    public int getCount() {
        return count;
    }

    public int getLength() {
        return length;
    }

    int typeToLength(@NotNull SubType type) {
        return switch (type) {
            case BATTLESHIP -> 4;
            case CRUISER -> 3;
            case DESTROYER -> 2;
            case LAUNCHER -> 1;
            case NONE -> 0;
        };
    }
}
