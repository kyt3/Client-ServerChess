package model.events;

import java.util.EventObject;

/**
 * Created by kot on 05.04.18.
 */
public class GameFinishedEvent extends EventObject {
    public GameFinishedEvent(Object o) {
        super(o);
    }
}
