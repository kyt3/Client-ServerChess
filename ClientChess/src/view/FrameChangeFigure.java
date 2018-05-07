package view;

import controller.Controller;
import model.real.Bishop;
import model.real.Knight;
import model.real.Queen;
import model.real.Rook;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by kot on 08.04.18.
 */
public class FrameChangeFigure extends JFrame implements MouseListener {
    private ArrayList<FigureView> figureViews = new ArrayList<>(4);

    public FrameChangeFigure(boolean color) throws HeadlessException, IOException {
        setLayout(new GridLayout(1, 0));
        figureViews.add(new FigureView(new Rook(color)));
        figureViews.add(new FigureView(new Knight(color)));
        figureViews.add(new FigureView(new Bishop(color)));
        figureViews.add(new FigureView(new Queen(color)));

        for (FigureView figureView : figureViews) {
            JPanel panel = new JPanel(new BorderLayout());
            panel.add(figureView);
            add(panel);
        }

        addMouseListener(Controller.getInstance());
        addMouseListener(this);

        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setSize(500, 200);
        setVisible(true);
    }

    @Override
    public void mouseClicked(MouseEvent mouseEvent) {
        dispose();
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
