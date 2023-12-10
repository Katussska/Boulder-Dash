package mcboulderdash.types;

public enum Direction {
    UP(-1), DOWN(1), LEFT(-1), RIGHT(1);

    final int direction;

    Direction(int d) {
        direction = d;
    }

    public int getValue() {
        return direction;
    }
}
