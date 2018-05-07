package model.events;

import java.util.EventObject;

/**
 * Created by kot on 08.04.18.
 */
public class PawnHasComeToEndOfChessBoardEvent extends EventObject {
    public PawnHasComeToEndOfChessBoardEvent(Object o) {
        super(o);
    }
}
