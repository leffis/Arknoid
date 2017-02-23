package com.example.randy.arknoid;

public enum ItemType {
    BASIC_BALL(1),
    BASIC_PADDLE(2),
    BASIC_RECBRICK(3),
    SQUIRTLE_BRICK(4),
    FIRE_BALL(5),
    ONE_MORE_BALL(6),
    BARREL_BRICK(7),
    BARREL(8),
    BARREL_LINE(9),
    UPPER_EDGE(10),
    DOWN_EDGE(11),
    STEEL_BRICK(12),
    WOOD_BRICK(13),
    PAPER_BRICK(14),
    ADD_HP(15),
    SLOWER_PADDLE(16),
    FASTER_PADDLE(17),
    SLOWER_BALL(18),
    FASTER_BALL(19),
    LONGER_PADDLE(20),
    SHORTER_PADDLE(21),
    BIGGER_BALL(22),
    SMALLER_BALL(23);

    private int value;

    ItemType(int _value) {
        value = _value;
    }

    public int intvalue() { return value; }

    public static ItemType int2itemtype(int _value) {
        switch (_value) {
            case 1:     return BASIC_BALL;
            case 2:     return BASIC_PADDLE;
            case 3:     return BASIC_RECBRICK;
            case 4:     return SQUIRTLE_BRICK;
            case 5:     return FIRE_BALL;
            case 6:     return ONE_MORE_BALL;
            case 7:     return BARREL_BRICK;
            case 8:     return BARREL;
            case 9:     return BARREL_LINE;
            case 10:    return UPPER_EDGE;
            case 11:    return DOWN_EDGE;
            case 12:    return STEEL_BRICK;
            case 13:    return WOOD_BRICK;
            case 14:    return PAPER_BRICK;
            case 15:    return ADD_HP;
            case 16:    return SLOWER_PADDLE;
            case 17:    return FASTER_PADDLE;
            case 18:    return SLOWER_BALL;
            case 19:    return FASTER_BALL;
            case 20:    return LONGER_PADDLE;
            case 21:    return SHORTER_PADDLE;
            case 22:    return BIGGER_BALL;
            case 23:    return SMALLER_BALL;
            default:    return null;
        }
    }


}
