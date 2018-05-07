package view;

import model.abstract_.Figure;
import model.real.Cell;
import model.real.ChessBoard;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

/**
 * Created by kot on 05.04.18.
 */
public class ChessBoardView extends JPanel {
    private CellView[][] cellViews;

    public ChessBoardView(boolean color) throws IOException {
        cellViews = new CellView[8][8];
        setLayout(new GridLayout(8, 8, 0, 0));

        Cell[][] cells = ChessBoard.getInstance().getCells();

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (cells[j][i].getFigure() != null) {
                    cellViews[j][i] = new CellView(cells[j][i].isColor(),
                            new FigureView(cells[j][i].getFigure()));
                } else cellViews[j][i] = new CellView(cells[j][i].isColor());
            }

        }

        if (!color) {
            for (int i = 7; i > -1; i--) {
                for (int j = 0; j < 8; j++) {
                    add(cellViews[j][i]);
                }
            }
        } else {
            for (int i = 0; i < 8; i++) {
                for (int j = 7; j > -1; j--) {
                    add(cellViews[j][i]);
                }
            }
        }

    }

    public void cangeChessBoardWithMove(Cell startCell, Cell endCell) throws IOException {
        //переопределение доски

        changeChessBoardView(startCell, endCell);

        cellViews[startCell.getX()][startCell.getY()].setBackground(Color.LIGHT_GRAY);
        cellViews[endCell.getX()][endCell.getY()].setBackground(Color.darkGray);

        revalidate();
        repaint();
    }


    private void changeChessBoardView(Cell startCell, Cell endCell) throws IOException {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                CellView cellView = cellViews[j][i];
                cellView.setDefaultBackground();
            }
        }

        repaintCellView(startCell);
        repaintCellView(endCell);

    }

    private void repaintCellView(Cell cell) throws IOException {
        CellView cellView = cellViews[cell.getX()][cell.getY()];
        cellView.setFigureView(null);
        cellView.removeAll();
        Figure figure = cell.getFigure();
        if (figure != null) {
            cellView.setFigureView(new FigureView(figure));
        }
    }



    public void deleteRedBackground() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                CellView cellView = cellViews[j][i];

                if (cellView.getBackground() == Color.green) {
                    cellView.setDefaultBackground();
                }
            }
        }
    }
}
