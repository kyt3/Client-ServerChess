package model.real;

import model.abstract_.Figure;

/**
 * Created by kot on 28.03.18.
 */
public class Rook extends Figure {
    private boolean isWasFirstMove = false;

    public Rook(boolean color) {
        super(color);
    }

    public boolean isWasFirstMove() {
        return isWasFirstMove;
    }

    public void setWasFirstMove(boolean wasFirstMove) {
        isWasFirstMove = wasFirstMove;
    }
}
