package model.events;

import java.util.EventObject;

/**
 * Created by kot on 12.04.18.
 */
public class ModelChangedWithoutMoveEvent extends EventObject {
    public ModelChangedWithoutMoveEvent(Object o) {
        super(o);
    }
}
