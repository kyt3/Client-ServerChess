package view;

import model.events.*;
import model.real.Cell;

import javax.swing.*;
import java.io.IOException;

/**
 * Created by kot on 05.04.18.
 */
public class View implements ModelChangedListener, GameFinishedListener,
        PawnHasComeToEndOfChessBoardListener, NotPossibleMoveListener {
    private MyFrame myFrame;
    public View(boolean color) {
        SwingUtilities.invokeLater(() -> {
            try {
                myFrame = new MyFrame(color);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void modelChangedActions(ModelChangedEvent event, Cell startCell, Cell endCell) {
        try {
            myFrame.repaintWithMove(startCell, endCell);
        } catch (IOException e) {
            e.printStackTrace();
        }

        myFrame.update(myFrame.getGraphics());
    }

    public MyFrame getMyFrame() {
        return myFrame;
    }

    @Override
    public void gameFinished(GameFinishedEvent event) {
        myFrame.setEnabled(false);
    }


    @Override
    public void changePawnToOtherFigure(PawnHasComeToEndOfChessBoardEvent event, boolean color) {
        try {
            new FrameChangeFigure(color);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void actionsWhenNotPossibleMove(NotPossibleMoveEvent event) {
        myFrame.deleteRedBackground();
    }

}
