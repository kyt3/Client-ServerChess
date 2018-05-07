package model.events;

/**
 * Created by kot on 08.04.18.
 */
public interface PawnHasComeToEndOfChessBoardListener {
    void changePawnToOtherFigure(PawnHasComeToEndOfChessBoardEvent event, boolean color);
}
