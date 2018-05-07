package controller;

import client.Client;
import model.real.Cell;
import model.real.ChessBoard;
import view.View;
import view.CellView;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;

/**
 * Created by kot on 01.04.18.
 */
public class Controller implements MouseListener {
    private Cell startCell = null;
    private Cell endCell = null;
    private final Client client;
    private final View view;
    private static Controller instance;

    private Controller(Client client, View view) {
        this.client = client;
        this.view = view;
    }

    public static Controller getInstance() {
        return instance;
    }

    public static Controller getInstance(Client client, View view) {
        if (instance == null) {
            instance = new Controller(client, view);
        }
        return instance;
    }

    private Cell getCell(int x, int y) {
        Cell[][] cells = ChessBoard.getInstance().getCells();

        return cells[x][y];
    }

    public void mouseClicked(MouseEvent mouseEvent) {
        if (mouseEvent.getSource() instanceof CellView) {
            JComponent component = (JComponent) mouseEvent.getComponent();
            int x = getIndexFromCoordinatesX(component.getX());
            int y = getIndexFromCoordinatesY(component.getY());

            if (startCell == null) {
                startCell = getCell(x, y);
            } else {
                endCell = getCell(x, y);
                try {
                    client.doMove(startCell, endCell);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                startCell = null;
                endCell = null;
            }
        } else {
            int x = mouseEvent.getX();
            int width = mouseEvent.getComponent().getSize().width;
            String nameFigure;
            if (x < width / 4) {
                nameFigure = "Rook";
            } else if (x < width / 2) {
                nameFigure = "Knight";
            } else if (x < 3 * width / 4) {
                nameFigure = "Bishop";
            } else {
                nameFigure = "Queen";
            }

            client.changePawnToFigure(nameFigure);
        }
    }

    private int getIndexFromCoordinatesX(int number) {
        boolean color = client.isColor();

        int width = view.getMyFrame().getSize().width;
        int howMuchPixelsOnCell = (int) (width / 8.3);
        int value = number / howMuchPixelsOnCell;
        if (!color) {
            return value;
        } else return reverseValue(value);
    }

    private int reverseValue(int value) {
        switch (value) {
            case 0:
                return 7;
            case 1:
                return 6;
            case 2:
                return 5;
            case 3:
                return 4;
            case 4:
                return 3;
            case 5:
                return 2;
            case 6:
                return 1;
            case 7:
                return 0;

        }
        return -1;
    }

    private int getIndexFromCoordinatesY(int number) {
        boolean color = client.isColor();
        int height = view.getMyFrame().getSize().height;
        int howMuchPixelsOnCell = (int) (height / 8.8);
        int value = number / howMuchPixelsOnCell;
        if (!color) {
            return reverseValue(value);
        } else return value;
    }

    @Override
    public void mousePressed(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseReleased(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseEntered(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseExited(MouseEvent mouseEvent) {

    }
}
