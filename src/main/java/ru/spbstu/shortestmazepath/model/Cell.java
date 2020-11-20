package ru.spbstu.shortestmazepath.model;

public class Cell {
    public final int x;
    public final int y;
    public final Type type;

    public Cell(int x, int y, Type type) {
        this.x = x;
        this.y = y;
        this.type = type;
    }

    public enum Type {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Cell cell = (Cell) o;

        if (x != cell.x) return false;
        return y == cell.y;
    }

    @Override
    public int hashCode() {
        int result = x;
        result = 31 * result + y;
        return result;
    }

    @Override
    public String toString() {
        return type.toString();
    }

    /**
     * Calculating Euclidean distance between two cells.
     * d(c1, c2) = sqrt((c1.x - c2.x) ^ 2 + (c1.y - c2.y) ^ 2)
     *
     * @param other the second cell.
     * @return Euclidean distance between cells.
     */
    public double distanceTo(Cell other) {
        int dx = x - other.x;
        int dy = y - other.y;
        return Math.sqrt(dx * dx + dy * dy);
    }
}
