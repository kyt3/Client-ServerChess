package view;

import controller.Controller;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * Created by kot on 05.04.18.
 */
public class CellView extends JPanel implements MouseListener {
    private boolean color;
    private FigureView figureView;

    public CellView(boolean color) {
        this.color = color;

        init();
    }

    public CellView(boolean color, FigureView figureView) {
        this.color = color;
        this.figureView = figureView;
        init();
    }

    public void setFigureView(FigureView figureView) {
        this.figureView = figureView;
    }

    public void init() {
        setLayout(new BorderLayout());
        setBackground();

        addMouseListener(this);
        addMouseListener(Controller.getInstance());
    }

    public void setBackground() {
        if (getBackground() == Color.green) {
            setBackground(Color.LIGHT_GRAY);
            return;
        }

        setDefaultBackground();
    }

    public void setDefaultBackground() {
        if (!color) {
            setBackground(new Color(230, 208, 148));
//            setBackground(Color.orange);
        } else {
            setBackground(new Color(162, 125, 72));
        }
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        if (figureView != null) {
            this.add(BorderLayout.CENTER, figureView);
        }

        super.paintComponent(graphics);
    }


    @Override
    public void mouseClicked(MouseEvent mouseEvent) {
        setBackground(Color.green);
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
