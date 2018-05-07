package model;

import model.abstract_.Figure;
import model.real.*;

/**
 * Created by kot on 28.03.18.
 */
public class Player {
    private boolean color;

    public Player(boolean color) {
        this.color = color;
    }

    public boolean isColor() {
        return color;
    }

    public void move(Cell startCell, Cell endCell) {

        Logic.setPreviousMovedFigure(startCell.getFigure());
        Logic.setPreviousStartCell(startCell);
        Logic.setPreviousEndCell(endCell);

        //для короля и для ладьи нужно следить за ходом
        if (startCell.getFigure() instanceof King) {
            King king = (King) startCell.getFigure();
            king.setWasFirstMove(true);
        }

        if (startCell.getFigure() instanceof Rook) {
            Rook rook = (Rook) startCell.getFigure();
            rook.setWasFirstMove(true);

        }

        endCell.setFigure(startCell.getFigure());
        startCell.setFigure(null);
    }

    public void casterling(Cell startCell, Cell endCell) {
        //сделать рокировку в зависимости от цвета и стороны, жесткий хардкод
        if (endCell.getX() > startCell.getX()) {
            shortCasterling(startCell, endCell);
        } else {
            longCasterling(startCell, endCell);
        }

    }

    private void shortCasterling(Cell startCellKing, Cell endCellKing) {
        Cell startCellRook;
        Cell endCellRook;
        if (!color) {
            startCellRook = ChessBoard.getInstance().getCells()[7][0];
            endCellRook = ChessBoard.getInstance().getCells()[5][0];

        } else {
            startCellRook = ChessBoard.getInstance().getCells()[7][7];
            endCellRook = ChessBoard.getInstance().getCells()[5][7];
        }

        move(startCellKing, endCellKing);
        move(startCellRook, endCellRook);
    }

    private void longCasterling(Cell startCellKing, Cell endCellKing) {
        Cell startCellRook;
        Cell endCellRook;
        if (!color) {
            startCellRook = ChessBoard.getInstance().getCells()[0][0];
            endCellRook = ChessBoard.getInstance().getCells()[3][0];

        } else {
            startCellRook = ChessBoard.getInstance().getCells()[0][7];
            endCellRook = ChessBoard.getInstance().getCells()[3][7];

        }

        move(startCellKing, endCellKing);
        move(startCellRook, endCellRook);
    }

    public void takeOnTheAisle(Cell startCell, Cell endCell) {
        if (!color) {
            //при взятии на проходе вражеская пешка будет находится на строго определенных линиях
            int y = 4;
            deleteEnemyPawn(endCell, y);
        } else {
            int y = 3;
            deleteEnemyPawn(endCell, y);
        }

        endCell.setFigure(startCell.getFigure());
        startCell.setFigure(null);
    }

    private void deleteEnemyPawn(Cell endCell, int y) {
        //удаление вражеской пешки при взятии на проходе
        Cell[][] cells = ChessBoard.getInstance().getCells();
        for (int i = 0; i < 8; i++) {
            Figure figure = cells[i][y].getFigure();
            if (figure != null) {
                if (figure instanceof Pawn && figure.getColor() != color) {
                    if (cells[i][y].getX() == endCell.getX()) {
                        cells[i][y].setFigure(null);
                    }
                }
            }
        }
    }
}
