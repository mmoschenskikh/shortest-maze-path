package ru.spbstu.shortestmazepath.model;

public enum MazeCell {
    PATH, WALL, START, END;

    @Override
    public String toString() {
        switch (this) {
            case PATH:
                return " ";
            case WALL:
                return "#";
            case START:
                return "S";
            case END:
                return "F";
            default:
                throw new IllegalStateException("Unexpected value: " + this);
        }
    }
}
