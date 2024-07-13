package dtos;

import enums.Direction;

public class ItemDTO {
    int x, y;
    int length;
    boolean horizontal;

    public ItemDTO(int x, int y, int length, boolean horizontal) {
        this.x = x;
        this.y = y;
        this.length = length;
        this.horizontal = horizontal;
    }

    public static ItemDTO from(ItemDTO that) {
        return new ItemDTO(that.x, that.y, that.length, that.horizontal);
    }

    public ItemDTO sail(Direction direction) {
        ItemDTO newObject = ItemDTO.from(this);
        switch (direction) {
            case UP -> newObject.y -= 1;
            case LEFT -> newObject.x -= 1;
            case DOWN -> newObject.y += 1;
            case RIGHT -> newObject.x += 1;
            case RADIAL -> newObject.horizontal = !newObject.horizontal;
        }

        return newObject;
    }

    public boolean isOutOfBoard(int limitX, int limitY) {
        return this.x < 0 || this.y < 0 || this.x >= limitX || this.y >= limitY;
    }

    public boolean isHorizontal() {
        return horizontal;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getLength() {
        return length;
    }

    public int getVerticalLength() {
        return isHorizontal() ? 1 : this.length;
    }

    public int getHorizontalLength() {
        return isHorizontal() ? this.length : 1;
    }

    @Override
    public String toString() {
        return "ItemDTO{" +
                "x=" + x +
                ", y=" + y +
                ", length=" + length +
                ", horizontal=" + horizontal +
                ", hoLen=" + getHorizontalLength() +
                ", verLen=" + getVerticalLength() +
                '}';
    }
}
