package view;

import model.real.Cell;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

/**
 * Created by kot on 05.04.18.
 */
public class MyFrame extends JFrame {
    private ChessBoardView chessBoardView;

    public MyFrame(boolean color) throws HeadlessException, IOException {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        chessBoardView = new ChessBoardView(color);
        add(BorderLayout.CENTER, chessBoardView);

        setSize(500, 500);
        setVisible(true);
    }


    public void deleteRedBackground() {
        chessBoardView.deleteRedBackground();
    }

    public void repaintWithMove(Cell startCell, Cell endCell) throws IOException {
        chessBoardView.cangeChessBoardWithMove(startCell, endCell);
        super.repaint();
    }
}
