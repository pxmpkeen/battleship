package services;

import dtos.ItemDTO;
import dtos.SubTypeDTO;
import entities.Distance;
import entities.Item;
import entities.Ship;
import entities.Water;
import enums.Direction;
import enums.ItemType;
import enums.SubType;
import org.jetbrains.annotations.NotNull;
import utils.Helper;

import java.util.*;

public class Sea {
    Item[][] board;
    static int boardHeight = 10;
    static int boardLength = boardHeight;
    public HashMap<UUID, Integer> hp;
    public HashSet<UUID> ships;

    public Sea() {
        initBoard();
    }

    void initBoard() {
        this.board = new Item[boardHeight][boardLength];
        for (int i = 0; i < boardHeight; i++) {
            for (int j = 0; j < boardLength; j++) {
                this.board[i][j] = new Water();
            }
        }
        hp = new HashMap<>();
        ships = new HashSet<>();
    }

    public void fillBoard() {
        SubTypeDTO[] dtos = new SubTypeDTO[] {
                new SubTypeDTO(SubType.BATTLESHIP),
                new SubTypeDTO(SubType.CRUISER),
                new SubTypeDTO(SubType.DESTROYER),
                new SubTypeDTO(SubType.LAUNCHER),
        };

        for (SubTypeDTO dto : dtos) {
            int length = dto.getLength();
            SubType type = dto.getType();

            for (int i = 0; i < dto.getCount(); i++) {
                System.out.printf("Locate %s ship with the length of %d\n", type, length);

                boolean editing = true;
                UUID id = Item.createId();
                ItemDTO object = findSpace(length);

                locateShips(object, id);
                ships.add(id);

                System.out.println(this);

                while (editing) {
                    System.out.println("W - Up, A - Left, S - Down, D - Right, R - rotate, Y - locate");
                    try {
                        switch (new Scanner(System.in).nextLine().toLowerCase().charAt(0)) {
                            case 'y':
                                setBorder(object);
                                editing = false;
                                break;
                            case 'w':
                                object = sail(object, Direction.UP, id);
                                break;
                            case 'a':
                                object = sail(object, Direction.LEFT, id);
                                break;
                            case 's':
                                object = sail(object, Direction.DOWN, id);
                                break;
                            case 'd':
                                object = sail(object, Direction.RIGHT, id);
                                break;
                            case 'r':
                                object = sail(object, Direction.RADIAL, id);
                                break;
                            case 'o':
                                Scanner location = new Scanner(System.in);
                                flood(object);
                                System.out.println("Give location: X Y Horizontality (1 - true, 0 - false), like: 0 0 1");
                                object = sail(new ItemDTO(location.nextInt(), location.nextInt(), length, location.nextInt() == 0 ), Direction.RADIAL, id);
                                Helper.numClear(2);
                                break;
                            default: throw new Exception("Impossible key");
                        }
                        Helper.numClear(14);
                        System.out.println(this);
                    } catch (Exception e) {
                        Helper.numClear(14);
                        System.out.println(this);
                    }
                }
                Helper.numClear(14);
            }
        }
        prepare();
    }

    void flood(ItemDTO object) {
        if (object.isHorizontal()) {
            for (int i = object.getX(); i < object.getLength() + object.getX(); i++) {
                board[object.getY()][i] = new Water();
            }
        } else {
            for (int i = object.getY(); i < object.getLength() + object.getY(); i++) {
                board[i][object.getX()] = new Water();
            }
        }
    }

    ItemDTO sail(ItemDTO object, Direction direction, UUID id) throws Exception {
        ItemDTO sailedObject = object.sail(direction);

        if (checkSpace(object, sailedObject, direction)) {
            flood(object);
            locateShips(sailedObject, id);
            return sailedObject;
        } else {
            throw new Exception("This move is impossible");
        }
    }

    void locateShips(ItemDTO object, UUID id) {
        SubType type = lengthToType(object.getLength());

        if (object.isHorizontal()) {
            for (int i = object.getX(); i < object.getLength() + object.getX(); i++) {
                board[object.getY()][i] = new Ship(type, id);
            }
        } else {
            for (int i = object.getY(); i < object.getLength() + object.getY(); i++) {
                board[i][object.getX()] = new Ship(type, id);
            }
        }
    }

    SubType lengthToType(int length) {
        return switch (length) {
            case 4 -> SubType.BATTLESHIP;
            case 3 -> SubType.CRUISER;
            case 2 -> SubType.DESTROYER;
            case 1 -> SubType.LAUNCHER;
            default -> SubType.NONE;
        };
    }

    boolean checkSpace(@NotNull ItemDTO object, ItemDTO sailedObject, Direction direction) {
        if (sailedObject.isOutOfBoard(boardLength, boardHeight)) {
            return false;
        }

        int count = 0;

        if (direction == Direction.RADIAL) {
            int rotateCount = getRotateCount(sailedObject);
            return rotateCount == sailedObject.getLength() - 1;
        }
        if (object.isHorizontal()) {
            switch (direction) {
                case UP, DOWN -> {
                    for (int i = sailedObject.getX(); i < sailedObject.getLength() + sailedObject.getX(); i++) {
                        if (Objects.requireNonNull(board[sailedObject.getY()][i].getType()) == ItemType.WATER) {
                            count++;
                        }
                    }
                }
                case LEFT -> {
                    if (Objects.requireNonNull(board[sailedObject.getY()][sailedObject.getX()].getType()) == ItemType.WATER) {
                        return true;
                    }
                }
                case RIGHT -> {
                    if (Objects.requireNonNull(board[sailedObject.getY()][sailedObject.getX() + sailedObject.getLength() - 1].getType()) == ItemType.WATER) {
                        return true;
                    }
                }
            }
        }
        else {
            switch (direction) {
                case LEFT, RIGHT -> {
                    for (int i = sailedObject.getY(); i < sailedObject.getLength() + sailedObject.getY(); i++) {
                        if (Objects.requireNonNull(board[i][sailedObject.getX()].getType()) == ItemType.WATER) {
                            count++;
                        }
                    }
                }
                case UP -> {
                    if (Objects.requireNonNull(board[sailedObject.getY()][sailedObject.getX()].getType()) == ItemType.WATER) {
                        return true;
                    }
                }
                case DOWN -> {
                    if (Objects.requireNonNull(board[sailedObject.getY() + sailedObject.getLength() - 1][sailedObject.getX()].getType()) == ItemType.WATER) {
                        return true;
                    }
                }
            }
        }

        return count == object.getLength();
    }

    private int getRotateCount(ItemDTO sailedObject) {
        int rotateCount = 0;
        if (sailedObject.isHorizontal()) {
            for (int i = sailedObject.getX() + 1; i < sailedObject.getLength() + sailedObject.getX(); i++) {
                if (Objects.requireNonNull(board[sailedObject.getY()][i].getType()) == ItemType.WATER) {
                    rotateCount++;
                }
            }
        } else {
            for (int i = sailedObject.getY() + 1; i < sailedObject.getLength() + sailedObject.getY(); i++) {
                if (Objects.requireNonNull(board[i][sailedObject.getX()].getType()) == ItemType.WATER) {
                    rotateCount++;
                }
            }
        }
        return rotateCount;
    }

    void setBorder(ItemDTO object) {
        int num;

        System.out.println(object);

        for (int i = 0; i < object.getVerticalLength(); i++) {
            if (object.getX() - 1 != -1) {
                board[object.getY() + i][object.getX() - 1] = new Distance();
            }

            if (object.getX() + object.getHorizontalLength() != boardLength) {
                num = object.isHorizontal() ? object.getLength() : 1;
                board[object.getY() + i][object.getX() + num] = new Distance();
            }

        }

        for (int i = 0; i < object.getHorizontalLength(); i++) {
            if (object.getY() - 1 != -1) {
                board[object.getY() - 1][object.getX() + i] = new Distance();
            }


            if (object.getY() + object.getVerticalLength() != boardHeight) {
                num = !object.isHorizontal() ? object.getLength() : 1;
                board[object.getY() + num][object.getX() + i] = new Distance();
            }
        }

        if (object.getX() - 1 != -1 && object.getY() - 1 != -1) board[object.getY() - 1][object.getX() - 1] = new Distance();
        if (object.getX() + object.getHorizontalLength() != boardLength && object.getY() - 1 != -1) board[object.getY() - 1][object.getX() + object.getHorizontalLength()] = new Distance();
        if (object.getX() - 1 != -1 && object.getY() + object.getVerticalLength() != boardHeight) board[object.getY() + object.getVerticalLength()][object.getX() - 1] = new Distance();
        if (object.getX() + object.getHorizontalLength() != boardLength && object.getY() + object.getVerticalLength() != boardHeight) board[object.getY() + object.getVerticalLength()][object.getX() + object.getHorizontalLength()] = new Distance();
    }

    ItemDTO findSpace(int length) {
        int countHor = 0, countVer = 0;

        for (int i = 0; i < boardHeight; i++) {
            for (int j = 0; j < boardLength; j++) {
                switch (board[i][j].getType()) {
                    case WATER -> countHor++;
                    case SHIP, DISTANCE -> countHor = 0;
                }
                switch (board[j][i].getType()) {
                    case WATER -> countVer++;
                    case SHIP, DISTANCE -> countVer = 0;
                }

                if (countHor == length) {
                    return new ItemDTO(j - length + 1, i, length, true);
                } else if (countVer == length) {
                    return new ItemDTO(i, j - length + 1, length, false);
                }
            }
            countHor = 0;
            countVer = 0;
        }
        return null;
    }

    void prepare() {
        for (int i = 0; i < boardHeight; i++) {
            for (int j = 0; j < boardLength; j++) {
                if (hp.containsKey(board[i][j].getId())) {
                    hp.put(board[i][j].getId(), hp.get(board[i][j].getId()) + 1);
                }
                else {
                    hp.put(board[i][j].getId(), 1);
                }
            }
        }
    }

    public boolean hit(int x, int y) {
        if (hp.containsKey(board[y][x].getId())) {
            hp.put(board[y][x].getId(), hp.get(board[y][x].getId()) - 1);
            board[y][x].hit();
            if (hp.get(board[y][x].getId()) == 0) {
                hp.remove(board[y][x].getId());
            }
            return board[y][x].getType() == ItemType.SHIP;
        }

        return false;
    }

    public boolean finish() {
        boolean finish = true;

        for (UUID id : ships) {
            if (hp.containsKey(id)) {
                finish = false;
                break;
            }
        }
        return finish;
    }

    public void fillOne(int x, int y) {
        board[y][x].hit();
    }

    public void check(int x, int y) {
        board[y][x].setChecked();
    }

    @Override
    public String toString() {
        StringBuilder string = new StringBuilder();

        string.append("   ");
        for (int i = 0; i < boardLength; i++) {
            string.append(String.format(" %d ", i));
        }
        string.append('\n');

        char letterCoordinate = 'A';
        for (Item[] row : board) {
            string.append(String.format(" %c ", letterCoordinate++));
            for (Item entry : row) {
                string.append(entry.toString());
            }
            string.append('\n');
        }

        return string.toString();
    }
}
