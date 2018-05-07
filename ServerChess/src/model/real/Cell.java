package model.real;

import model.abstract_.Figure;

import java.io.Serializable;

/**
 * Created by kot on 28.03.18.
 */
public class Cell implements Serializable {
    private static final long serialVersionUID = 1L;
    private int x;
    private int y;
    private Figure figure;
    private boolean color;

    public Cell(int x, int y, boolean color) {
        this.x = x;
        this.y = y;
        this.color = color;
    }

    public Cell(int x, int y, Figure figure, boolean color) {
        this.x = x;
        this.y = y;
        this.figure = figure;
        this.color = color;
    }

    public Figure getFigure() {
        return figure;
    }

    public void setFigure(Figure figure) {
        this.figure = figure;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public boolean isColor() {
        return color;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Cell cell = (Cell) o;

        if (x != cell.x) return false;
        if (y != cell.y) return false;
        if (color != cell.color) return false;
        return figure != null ? figure.equals(cell.figure) : cell.figure == null;

    }

    @Override
    public int hashCode() {
        int result = x;
        result = 31 * result + y;
        result = 31 * result + (figure != null ? figure.hashCode() : 0);
        result = 31 * result + (color ? 1 : 0);
        return result;
    }
}
