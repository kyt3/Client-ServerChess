package model.real;

/**
 * Created by kot on 28.03.18.
 */
public class ChessBoard {
    private Cell[][] cells;
    private static ChessBoard instance = null;

    private ChessBoard(Cell[][] cells) {
        this.cells = cells;
    }

    public static ChessBoard getInstance(Cell[][] cells) {
        if (instance == null) {
            instance = new ChessBoard(cells);
            return instance;
        }
        return instance;
    }

    public static ChessBoard getInstance() {
        return instance;
    }

    public Cell[][] getCells() {
        return cells;
    }
}
